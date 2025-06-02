package top.srcres258.shanxiskeleton.screen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.screen.custom.*;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, ShanxiSkeleton.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<WitherSkeletonProducerMenu>>
    WITHER_SKELETON_PRODUCER = registerContainerMenu("wither_skeleton_producer",
            (containerId, inv, data) -> new WitherSkeletonProducerMenu(containerId, inv, inv.player.level(), data));
    public static final DeferredHolder<MenuType<?>, MenuType<WitherSkeletonBreederMenu>>
    WITHER_SKELETON_BREEDER = registerContainerMenu("wither_skeleton_breeder",
            (containerId, inv, data) -> new WitherSkeletonBreederMenu(containerId, inv, inv.player.level(), data));
    public static final DeferredHolder<MenuType<?>, MenuType<WitherSkeletonSlaughtererMenu>>
    WITHER_SKELETON_SLAUGHTERER = registerContainerMenu("wither_skeleton_slaughterer",
            (containerId, inv, data) -> new WitherSkeletonSlaughtererMenu(containerId, inv, inv.player.level(), data));

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerContainerMenu(
            @NotNull String name,
            @NotNull IContainerFactory<T> factory
    ) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(@NotNull IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

    public static void registerMenuScreens(@NotNull RegisterMenuScreensEvent event) {
        event.register(WITHER_SKELETON_PRODUCER.get(), WitherSkeletonProducerScreen::new);
        event.register(WITHER_SKELETON_BREEDER.get(), WitherSkeletonBreederScreen::new);
        event.register(WITHER_SKELETON_SLAUGHTERER.get(), WitherSkeletonSlaughtererScreen::new);
    }
}
