package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.item.custom.renderer.WitherSkeletonSpecialRenderer;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(@NotNull PackOutput output) {
        super(output, ShanxiSkeleton.MOD_ID);
    }

    @Override
    protected void registerModels(
            @NotNull BlockModelGenerators blockModels,
            @NotNull ItemModelGenerators itemModels
    ) {
        registerBlockModels(blockModels);
        registerItemModels(itemModels);
    }

    private void registerBlockModels(@NotNull BlockModelGenerators blockModels) {
        createMachineBlock(blockModels, ModBlocks.WITHER_SKELETON_PRODUCER.get(), Blocks.COAL_BLOCK, Blocks.DIRT);
        createMachineBlock(blockModels, ModBlocks.WITHER_SKELETON_BREEDER.get(), Blocks.COAL_BLOCK, Blocks.IRON_BLOCK);
        createMachineBlock(blockModels, ModBlocks.WITHER_SKELETON_SLAUGHTERER.get(), Blocks.DIRT, Blocks.IRON_BLOCK);
        createMachineBlock(blockModels, ModBlocks.FRAME_BLOCK.get(), Blocks.IRON_BLOCK, Blocks.IRON_BLOCK);
    }

    private void registerItemModels(@NotNull ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.WITHER_SKELETON_CATCHER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TINY_COAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(ModItems.WITHER_SKELETON.get(),
                ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(ModItems.WITHER_SKELETON.get()),
                        new WitherSkeletonSpecialRenderer.Unbaked()));
    }

    private static void createMachineBlock(
            @NotNull BlockModelGenerators blockModels,
            @NotNull Block machineBlock,
            @NotNull Block frontTexture,
            @NotNull Block backTexture
    ) {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(machineBlock,
                ModModelTemplates.MACHINE.create(machineBlock, ModTextureMappings.machine(frontTexture, backTexture),
                        blockModels.modelOutput)));
    }
}
