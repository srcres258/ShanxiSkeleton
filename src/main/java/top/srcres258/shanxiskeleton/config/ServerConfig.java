package top.srcres258.shanxiskeleton.config;

import net.minecraft.world.item.Item;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;
import top.srcres258.shanxiskeleton.util.RegistryHelper;
import top.srcres258.shanxiskeleton.util.tag.ITag;
import top.srcres258.shanxiskeleton.util.tag.TagFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerConfig extends BaseConfig {
    /**
     * 凋灵骷髅生产器产生一次输出所需的时间。
     */
    public final ModConfigSpec.IntValue producerMaxTime;
    /**
     * 凋灵骷髅繁殖器产生一次输出所需的时间。
     */
    public final ModConfigSpec.IntValue breederMaxTime;
    /**
     * 凋灵骷髅屠宰器产生一次输出所需的时间。
     */
    public final ModConfigSpec.IntValue slaughtererMaxTime;
    /**
     * 凋灵骷髅生产器可以接受的玫瑰种类（可以用于生产凋灵玫瑰的物品种类，不是花也行，只要是物品或物品标签）。
     */
    public final ModConfigSpec.ConfigValue<List<? extends String>> producerRoseTypesSpec;
    /**
     * 凋灵骷髅生产器一次产出的煤炭数量的最大值。产出时数量将从 [0, 最大值] 闭区间中随机取值。
     */
    public final ModConfigSpec.IntValue producerCoalMaxAmount;
    /**
     * 凋灵骷髅生产器一次产出的小块煤炭数量的最大值。产出时数量将从 [0, 最大值] 闭区间中随机取值。
     */
    public final ModConfigSpec.IntValue producerTinyCoalMaxAmount;
    /**
     * 凋灵骷髅屠宰器一次产出的煤炭数量的最大值。产出时数量将从 [0, 最大值] 闭区间中随机取值。
     */
    public final ModConfigSpec.IntValue slaughtererCoalMaxAmount;
    /**
     * 凋灵骷髅屠宰器一次产出的小块煤炭数量的最大值。产出时数量将从 [0, 最大值] 闭区间中随机取值。
     */
    public final ModConfigSpec.IntValue slaughtererTinyCoalMaxAmount;
    /**
     * 凋灵骷髅屠宰器一次产出的骨头数量的最大值。产出时数量将从 [0, 最大值] 闭区间中随机取值。
     */
    public final ModConfigSpec.IntValue slaughtererBoneMaxAmount;
    /**
     * 凋灵骷髅屠宰器一次产出骨头的概率。可取 [0, 1] 闭区间内的浮点数。
     */
    public final ModConfigSpec.DoubleValue slaughtererSkullChance;
    /**
     * 本模组与凋灵骷髅有关的机器尝试产生凋灵骷髅空闲音效的间隔，单位为游戏刻（tick）。
     */
    public final ModConfigSpec.IntValue witherSkeletonAmbientSoundInterval;
    /**
     * 本模组与凋灵骷髅有关的机器每次尝试产生凋灵骷髅空闲音效的成功概率，可取 [0, 1] 闭区间内的浮点数。
     */
    public final ModConfigSpec.DoubleValue witherSkeletonAmbientSoundChance;
    /**
     * 是否当凋灵骷髅在物品栏中时产生凋灵骷髅音效。
     */
    public final ModConfigSpec.BooleanValue inventoryWitherSkeletonSound;

    @Nullable
    private List<ITag<Item>> producerRoseTypes = null;

    private final ModConfigSpec spec;

    public ServerConfig() {
        ModConfigSpec.Builder builder;

        builder = createConfigSpecBuilder();

        producerMaxTime = builder.comment("The time in ticks the producer takes to produce items from wither skeletons.")
                .defineInRange("shanxiskeleton.producer_max_time",
                        WitherSkeletonProducerBlockEntity.DEFAULT_MAX_PROGRESS,
                        1, Integer.MAX_VALUE);
        breederMaxTime = builder.comment("The time in ticks the breeder takes to breed wither skeletons.")
                .defineInRange("shanxiskeleton.breeder_max_time",
                        WitherSkeletonBreederBlockEntity.DEFAULT_MAX_PROGRESS,
                        1, Integer.MAX_VALUE);
        slaughtererMaxTime = builder.comment("The time in ticks the slaughterer takes to slaughter wither skeletons.")
                .defineInRange("shanxiskeleton.slaughterer_max_time",
                        WitherSkeletonSlaughtererBlockEntity.DEFAULT_MAX_PROGRESS,
                        1, Integer.MAX_VALUE);
        producerRoseTypesSpec = builder.comment("The rose types that the producer can accept to produce wither roses.")
                .defineList("shanxiskeleton.producer_rose_types", Arrays.stream(WitherSkeletonProducerBlockEntity.DEFAULT_ROSES.get())
                        .map(RegistryHelper::getBlockKeyString).toList(), () -> "", Objects::nonNull);
        producerCoalMaxAmount = builder.comment("The maximum amount of coals per time produced by the producer.")
                .defineInRange("shanxiskeleton.producer_coal_max_amount", 1, 0, 64);
        producerTinyCoalMaxAmount = builder.comment("The maximum amount of tiny coals per time produced by the producer.")
                .defineInRange("shanxiskeleton.producer_tiny_coal_max_amount", 5, 0, 64);
        slaughtererCoalMaxAmount = builder.comment("The maximum amount of coals per time produced by the slaughterer.")
                .defineInRange("shanxiskeleton.slaughterer_coal_max_amount", 2, 0, 64);
        slaughtererTinyCoalMaxAmount = builder.comment("The maximum amount of tiny coals per time produced by the slaughterer.")
                .defineInRange("shanxiskeleton.slaughterer_tiny_coal_max_amount", 10, 0, 64);
        slaughtererBoneMaxAmount = builder.comment("The maximum amount of bones per time produced by the slaughterer.")
                .defineInRange("shanxiskeleton.slaughterer_bone_max_amount", 2, 0, 64);
        slaughtererSkullChance = builder.comment("The chance of producing a skull per time produced by the slaughterer.")
                .defineInRange("shanxiskeleton.slaughterer_skull_chance", 0.25, 0.0, 1.0);
        witherSkeletonAmbientSoundInterval = builder.comment("The interval in ticks between each attempt to play the wither skeleton ambient sound.")
                .defineInRange("shanxiskeleton.wither_skeleton_ambient_sound_interval", 80, 1, Integer.MAX_VALUE);
        witherSkeletonAmbientSoundChance = builder.comment("The chance of playing the wither skeleton ambient sound per attempt.")
                .defineInRange("shanxiskeleton.wither_skeleton_ambient_sound_chance", 0.025, 0.0, 1.0);
        inventoryWitherSkeletonSound = builder.comment("Whether to play the wither skeleton ambient sound when there are wither skeletons in player's inventory.")
                .define("shanxiskeleton.inventory_wither_skeleton_sound", true);

        spec = builder.build();
    }

    @Override
    @NotNull
    public ModConfigSpec getConfigSpec() {
        return spec;
    }

    @Override
    public void onReload(@NotNull ModConfigEvent.Reloading event) {
        parseConfig();
    }

    private void parseConfig() {
        producerRoseTypes = producerRoseTypesSpec.get().stream()
                .map(name -> TagFactory.getItem(name, true))
                .filter(Objects::nonNull)
                .toList();
    }

    @NotNull
    public List<ITag<Item>> getProducerRoseTypes() {
        if (producerRoseTypes == null) {
            parseConfig();
        }

        return List.copyOf(Objects.requireNonNull(producerRoseTypes,
                "producerRoseTypes shouldn't be null since it's initialized in parseConfig."));
    }
}
