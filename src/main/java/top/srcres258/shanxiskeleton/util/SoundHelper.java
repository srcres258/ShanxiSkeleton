package top.srcres258.shanxiskeleton.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;

public class SoundHelper {
    public static void playSoundAtBlock(
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull SoundEvent soundEvent
    ) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundEvent,
                SoundSource.BLOCKS, 1F, 1F);
    }

    public static void playSoundAtPlayer(@NotNull Player player, @NotNull SoundEvent soundEvent) {
        player.playNotifySound(soundEvent, SoundSource.NEUTRAL, 1F, 1F);
    }

    public static boolean canPlayWitherSkeletonAmbientSound(@NotNull Level level) {
        int interval;
        double chanceBound;

        interval = ShanxiSkeleton.getInstance().serverConfig.witherSkeletonAmbientSoundInterval.get();
        chanceBound = ShanxiSkeleton.getInstance().serverConfig.witherSkeletonAmbientSoundChance.get();
        return level.getGameTime() % interval == 0 && level.random.nextDouble() < chanceBound;
    }
}
