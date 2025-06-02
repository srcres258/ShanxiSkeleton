package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.block.ModBlocks;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(@NotNull HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.WITHER_SKELETON_PRODUCER.get());
        dropSelf(ModBlocks.WITHER_SKELETON_BREEDER.get());
        dropSelf(ModBlocks.WITHER_SKELETON_SLAUGHTERER.get());
        dropSelf(ModBlocks.FRAME_BLOCK.get());
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
