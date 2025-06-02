package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class FluidTag extends BaseRegistryTag<Fluid> {
    protected FluidTag(@NotNull HolderSet.Named<Fluid> holderSet) {
        super(holderSet);
    }

    @Override
    @NotNull
    protected Registry<Fluid> getRegistry() {
        return BuiltInRegistries.FLUID;
    }
}
