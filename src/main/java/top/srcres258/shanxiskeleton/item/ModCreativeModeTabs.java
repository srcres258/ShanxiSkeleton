package top.srcres258.shanxiskeleton.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShanxiSkeleton.MOD_ID);

    public static final Holder<CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mod_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.shanxiskeleton.mod_tab"))
                    .icon(() -> new ItemStack(ModItems.WITHER_SKELETON.get()))
                    .displayItems(((parameters, output) -> {
                        ModItems.buildModCreativeModeTab(parameters, output);
                        ModBlocks.buildModCreativeModeTab(parameters, output);
                    }))
                    .build());

    public static void register(@NotNull IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
