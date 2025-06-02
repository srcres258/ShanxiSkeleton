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
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;

public class WitherSkeletonBreederBlock extends BaseMachineBlock {
    public static final MapCodec<WitherSkeletonBreederBlock> CODEC = simpleCodec(WitherSkeletonBreederBlock::new);

    public WitherSkeletonBreederBlock(@NotNull Properties properties) {
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
        return new WitherSkeletonBreederBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level level,
            @NotNull BlockState state,
            @NotNull BlockEntityType<T> blockEntityType
    ) {
        if (!level.isClientSide()) {
            return createTickerHelper(blockEntityType, ModBlockEntityTypes.WITHER_SKELETON_BREEDER.get(),
                    ((level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1)));
        }

        return null;
    }
}
