package top.srcres258.shanxiskeleton.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public abstract class BaseConfig {
    @NotNull
    protected static ModConfigSpec.Builder createConfigSpecBuilder() {
        return new ModConfigSpec.Builder();
    }

    public void onLoad(@NotNull ModConfigEvent.Loading event) {}

    public void onReload(@NotNull ModConfigEvent.Reloading event) {}

    public void onUnload(@NotNull ModConfigEvent.Unloading event) {}

    @NotNull
    public abstract ModConfigSpec getConfigSpec();
}
