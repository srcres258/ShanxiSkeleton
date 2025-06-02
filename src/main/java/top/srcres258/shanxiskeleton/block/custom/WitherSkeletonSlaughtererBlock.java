package top.srcres258.shanxiskeleton.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.block.entity.ModBlockEntityTypes;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;

public class WitherSkeletonSlaughtererBlock extends BaseMachineBlock {
    public static final MapCodec<WitherSkeletonSlaughtererBlock> CODEC = simpleCodec(WitherSkeletonSlaughtererBlock::new);

    public WitherSkeletonSlaughtererBlock(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseMachineBlock> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WitherSkeletonSlaughtererBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level level,
            @NotNull BlockState state,
            @NotNull BlockEntityType<T> blockEntityType
    ) {
        if (!level.isClientSide()) {
            return createTickerHelper(blockEntityType, ModBlockEntityTypes.WITHER_SKELETON_SLAUGHTERER.get(),
                    ((level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1)));
        }

        return null;
    }
}
