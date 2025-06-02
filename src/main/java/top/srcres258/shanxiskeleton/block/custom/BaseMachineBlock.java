package top.srcres258.shanxiskeleton.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.block.entity.IDroppable;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;

import java.util.Objects;

public abstract class BaseMachineBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected BaseMachineBlock(@NotNull Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @NotNull
    protected abstract MapCodec<? extends BaseMachineBlock> codec();

    @Override
    @NotNull
    protected RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;   // 必须重写该方法，否则方块将无法渲染
    }

    @Override
    @NotNull
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onRemove(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState newState,
            boolean movedByPiston
    ) {
        BlockEntity blockEntity;

        if (state.getBlock() != newState.getBlock()) {
            blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IDroppable droppable) {
                droppable.drops();
            }
        }

        // From NeoForge's official documentation:
        //
        // To make sure that caches can correctly update their stored capability, modders must call
        // level.invalidateCapabilities(pos) whenever a capability changes, appears, or disappears.
        level.invalidateCapabilities(pos);

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    @NotNull
    protected ItemInteractionResult useItemOn(
            @NotNull ItemStack stack,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hitResult
    ) {
        BlockEntity entity;

        entity = level.getBlockEntity(pos);
        if (entity instanceof BaseMachineBlockEntity machine) {
            if (machine.hasMenu()) {
                if (!level.isClientSide()) {
                    player.openMenu(Objects.requireNonNull(machine.createMenuProvider(), "The BaseMachineBlockEntity " +
                            "has a menu but its createMenuProvider method returned null."), pos);
                    return ItemInteractionResult.CONSUME;
                }
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        throw new IllegalStateException(String.format("The BlockEntity at %s is not an instance of " +
                "BaseMachineBlockEntity.", pos));
    }
}
