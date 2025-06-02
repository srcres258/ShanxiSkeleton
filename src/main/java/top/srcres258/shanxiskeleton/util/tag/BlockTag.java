package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockTag extends BaseRegistryTag<Block> {
    public BlockTag(@NotNull HolderSet.Named<Block> holderSet) {
        super(holderSet);
    }

    @Override
    protected @NotNull Registry<Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }
}
