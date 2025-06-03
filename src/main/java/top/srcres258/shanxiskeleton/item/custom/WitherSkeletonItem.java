package top.srcres258.shanxiskeleton.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.item.custom.renderer.WitherSkeletonItemRenderer;
import top.srcres258.shanxiskeleton.util.SoundHelper;

import java.util.Objects;

public class WitherSkeletonItem extends Item {
    public static class ClientItemExtensions implements IClientItemExtensions {
        public static final ClientItemExtensions INSTANCE = new ClientItemExtensions();

        private final WitherSkeletonItemRenderer renderer = new WitherSkeletonItemRenderer();

        private ClientItemExtensions() {}

        @Override
        @NotNull
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer.getRendererSupplier().get();
        }
    }

    private static final EntityType<WitherSkeleton> WITHER_SKELETON_ENTITY_TYPE = EntityType.WITHER_SKELETON;

    public WitherSkeletonItem(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        Level level;
        ItemStack itemStack;
        BlockPos blockPos, targetPos;
        Direction direction;
        BlockState blockState;
        BlockEntity blockEntity;
        Mob spawnedMob;
        Player player;

        player = context.getPlayer();
        level = context.getLevel();
        if (level instanceof ServerLevel serverLevel) { // 注意：只能在服务端执行实体生成相关操作。
            itemStack = context.getItemInHand();
            blockPos = context.getClickedPos();
            direction = context.getClickedFace();
            blockState = level.getBlockState(blockPos);
            blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity instanceof Spawner spawner) {
                spawner.setEntityId(WITHER_SKELETON_ENTITY_TYPE, level.getRandom());
                level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
                itemStack.shrink(1);
            } else {
                if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                    targetPos = blockPos;
                } else {
                    targetPos = blockPos.relative(direction);
                }

                spawnedMob = WITHER_SKELETON_ENTITY_TYPE.spawn(serverLevel, itemStack, player, targetPos,
                        EntitySpawnReason.SPAWN_ITEM_USE, true,
                        !Objects.equals(blockPos, targetPos) && direction == Direction.UP);
                if (spawnedMob != null) {
                    itemStack.shrink(1);
                    level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
                }
            }

            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(
            @NotNull ItemStack stack,
            @NotNull Level level,
            @NotNull Entity entity,
            int slotId,
            boolean isSelected
    ) {
        if (!(entity instanceof Player)) {
            return;
        }
        if (!ShanxiSkeleton.getInstance().serverConfig.inventoryWitherSkeletonSound.get()) {
            return;
        }
        if (SoundHelper.canPlayWitherSkeletonAmbientSound(level)) {
            SoundHelper.playSoundAtPlayer(((Player) entity), SoundEvents.WITHER_SKELETON_AMBIENT);
        }
    }
}
