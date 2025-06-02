package top.srcres258.shanxiskeleton.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.block.entity.IDroppable;
import top.srcres258.shanxiskeleton.block.entity.ITickable;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.network.custom.ClientboundSyncMachineInputPayload;
import top.srcres258.shanxiskeleton.util.SingleTypeItemHandler;

import java.util.function.Consumer;

public abstract class BaseMachineBlockEntity extends BlockEntity implements ITickable, IDroppable {
    public static class CapabilityProvider implements ICapabilityProvider<BaseMachineBlockEntity, Direction, IItemHandler> {
        @Override
        @Nullable
        public IItemHandler getCapability(@NotNull BaseMachineBlockEntity blockEntity, @Nullable Direction side) {
            IItemHandler inputHandler, outputHandler;

            inputHandler = blockEntity.getMachineInputItemHandler();
            outputHandler = blockEntity.getMachineOutputItemHandler();

            if (side == null) {
                return inputHandler;
            }
            if (side == Direction.DOWN) {
                return outputHandler;
            }
            return inputHandler;
        }
    }

    protected BaseMachineBlockEntity(
            @NotNull BlockEntityType<?> type,
            @NotNull BlockPos pos,
            @NotNull BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    @NotNull
    protected abstract ItemStackHandler[] getHandlers();

    @Override
    public void drops() {
        ItemStackHandler[] handlers;
        SimpleContainer container;
        int slots, i;

        handlers = getHandlers();
        if (level != null) {
            for (var handler : handlers) {
                slots = handler.getSlots();
                container = new SimpleContainer(slots);
                for (i = 0; i < slots; i++) {
                    container.setItem(i, handler.getStackInSlot(i));
                }
                Containers.dropContents(level, worldPosition, container);
            }
        }
    }

    public boolean hasMenu() {
        return false;
    }

    @Nullable
    public MenuProvider createMenuProvider() {
        return null;
    }

    /**
     * Get the input item handler for this machine. Returns null if this machine does not have inputs.
     */
    @Nullable
    public IItemHandler getMachineInputItemHandler() {
        return null;
    }

    /**
     * Get the output item handler for this machine. Returns null if this machine does not have outputs.
     */
    @Nullable
    public IItemHandler getMachineOutputItemHandler() {
        return null;
    }

    @NotNull
    protected SingleTypeItemHandler createSyncSkeletonInputItemHandler(int size) {
        return createWitherSkeletonInputItemHandler(size, slot -> {
            setChanged();
            if (level != null && !level.isClientSide()) {
                syncInputChangeToClient();
            }
        });
    }

    @NotNull
    protected static SingleTypeItemHandler createWitherSkeletonInputItemHandler(int size) {
        return createWitherSkeletonInputItemHandler(size, null);
    }

    @NotNull
    protected static SingleTypeItemHandler createWitherSkeletonInputItemHandler(
            int size,
            @Nullable Consumer<Integer> contentsChangedCallback
    ) {
        return new SingleTypeItemHandler.InputOnly(size, ModItems.WITHER_SKELETON.get(), contentsChangedCallback);
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @NotNull
    protected RandomSource getRandom() {
        return level == null ? RandomSource.create() : level.random;
    }

    @NotNull
    public abstract ContainerData getContainerData();

    public void syncInputChangeToClient() {
        IItemHandler inputHandler;
        int i;
        ItemStack stack;

        inputHandler = getMachineInputItemHandler();
        if (inputHandler == null) {
            return;
        }

        for (i = 0; i < inputHandler.getSlots(); i++) {
            stack = inputHandler.getStackInSlot(i);
            ClientboundSyncMachineInputPayload.sendToAllPlayers(worldPosition, i, stack.getCount(),
                    BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        }
    }
}
