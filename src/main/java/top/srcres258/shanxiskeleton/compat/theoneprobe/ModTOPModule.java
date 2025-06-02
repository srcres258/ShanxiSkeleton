package top.srcres258.shanxiskeleton.compat.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModTOPModule implements Function<ITheOneProbe, Void> {
    @Override
    @Nullable
    public Void apply(@NotNull ITheOneProbe top) {
        top.registerProvider(new ModProbeInfoProvider());
        return null;
    }
}
