package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.custom.BaseMachineBlock;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public ModBlockStateProvider(
            @NotNull PackOutput output,
            @NotNull ExistingFileHelper exFileHelper
    ) {
        super(output, ShanxiSkeleton.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalFacingBlockWithItem(ModBlocks.WITHER_SKELETON_PRODUCER.get(), BaseMachineBlock.FACING,
                state -> models().withExistingParent("wither_skeleton_producer", modLoc("block/machine"))
                        .texture("front", mcLoc("block/coal_block"))
                        .texture("back", mcLoc("block/dirt")));
        horizontalFacingBlockWithItem(ModBlocks.WITHER_SKELETON_BREEDER.get(), BaseMachineBlock.FACING,
                state -> models().withExistingParent("wither_skeleton_breeder", modLoc("block/machine"))
                        .texture("front", mcLoc("block/coal_block"))
                        .texture("back", mcLoc("block/iron_block")));
        horizontalFacingBlockWithItem(ModBlocks.WITHER_SKELETON_SLAUGHTERER.get(), BaseMachineBlock.FACING,
                state -> models().withExistingParent("wither_skeleton_slaughterer", modLoc("block/machine"))
                        .texture("front", mcLoc("block/dirt"))
                        .texture("back", mcLoc("block/iron_block")));
        simpleBlockWithItem(ModBlocks.FRAME_BLOCK.get(),
                models().withExistingParent("frame_block", modLoc("block/machine"))
                        .texture("front", mcLoc("block/iron_block"))
                        .texture("back", mcLoc("block/iron_block")));
    }

    private void horizontalFacingBlockWithItem(
            @NotNull Block block,
            @NotNull DirectionProperty directionProperty,
            @NotNull Function<BlockState, ModelFile> modelFunc
    ) {
        BlockState defaultBlockState;

        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction direction;

                    direction = state.getValue(directionProperty);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationY((((int) direction.toYRot()) + DEFAULT_ANGLE_OFFSET) % 360)
                            .build();
                });
        defaultBlockState = block.defaultBlockState();
        simpleBlockItem(block, modelFunc.apply(defaultBlockState));
    }
}
