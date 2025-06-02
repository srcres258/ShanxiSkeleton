package top.srcres258.shanxiskeleton.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface ITickable {
    void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state);
}
