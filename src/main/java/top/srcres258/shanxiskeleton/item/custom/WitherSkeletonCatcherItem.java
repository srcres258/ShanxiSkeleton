package top.srcres258.shanxiskeleton.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.item.ModItems;

public class WitherSkeletonCatcherItem extends Item {
    public WitherSkeletonCatcherItem(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResult interactLivingEntity(
            @NotNull ItemStack stack,
            @NotNull Player player,
            @NotNull LivingEntity interactionTarget,
            @NotNull InteractionHand usedHand
    ) {
        Level level;
        ItemStack itemStack;
        Vec3 pos;

        level = player.level();
        pos = interactionTarget.position();
        // 注意：只能在服务端执行物品逻辑操作。
        if (level instanceof ServerLevel serverLevel && interactionTarget instanceof WitherSkeleton) {
            itemStack = new ItemStack(ModItems.WITHER_SKELETON.get());
            interactionTarget.discard();
            Containers.dropItemStack(level, pos.x, pos.y, pos.z, itemStack);
            stack.hurtAndBreak(1, serverLevel, player, item -> player.onEquippedItemBroken(item,
                    usedHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
