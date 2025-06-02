package top.srcres258.shanxiskeleton.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public class ClientConfig extends BaseConfig {
    public final ModConfigSpec.BooleanValue renderBlockContents;
    public final ModConfigSpec.IntValue blockRenderingDistance;

    private final ModConfigSpec spec;

    public ClientConfig() {
        ModConfigSpec.Builder builder;

        builder = createConfigSpecBuilder();

        renderBlockContents = builder.comment("Whether to render the block contents of this mod in the world.")
                .worldRestart()
                .define("shanxiskeleton.render_block_contents", true);
        blockRenderingDistance = builder.comment("The distance in blocks at which the block contents are rendered.")
                .defineInRange("shanxiskeleton.block_rendering_distance", 32, 1, 256);

        spec = builder.build();
    }

    @Override
    @NotNull
    public ModConfigSpec getConfigSpec() {
        return spec;
    }
}
