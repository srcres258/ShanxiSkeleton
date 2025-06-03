package top.srcres258.shanxiskeleton;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.entity.ModBlockEntityTypes;
import top.srcres258.shanxiskeleton.compat.ModIMC;
import top.srcres258.shanxiskeleton.config.ClientConfig;
import top.srcres258.shanxiskeleton.config.ServerConfig;
import top.srcres258.shanxiskeleton.item.ModCreativeModeTabs;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.network.ModNetworks;
import top.srcres258.shanxiskeleton.screen.ModMenuTypes;

@Mod(ShanxiSkeleton.MOD_ID)
public class ShanxiSkeleton {
    public static final String MOD_ID = "shanxiskeleton";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static ShanxiSkeleton instance = null;

    public final ServerConfig serverConfig;
    public final ClientConfig clientConfig;

    public ShanxiSkeleton(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
        LOGGER.info("{} ({}, version {}) is initializing...", modContainer.getModInfo().getDisplayName(), MOD_ID,
                modContainer.getModInfo().getVersion().toString());

        instance = this;

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        serverConfig = new ServerConfig();
        clientConfig = new ClientConfig();
        modContainer.registerConfig(ModConfig.Type.SERVER, serverConfig.getConfigSpec());
        modContainer.registerConfig(ModConfig.Type.CLIENT, clientConfig.getConfigSpec());

        modEventBus.addListener(this::loadConfig);
        modEventBus.addListener(this::reloadConfig);
        modEventBus.addListener(this::unloadConfig);

        modEventBus.addListener(this::registerCapabilities);

        modEventBus.addListener(this::registerPayloadHandlers);

        modEventBus.addListener(this::enqueueIMC);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::registerClientExtensions);
            modEventBus.addListener(this::registerRenderers);
            modEventBus.addListener(this::registerMenuScreens);
        }
    }

    @NotNull
    public static ShanxiSkeleton getInstance() {
        if (instance == null) {
            throw new IllegalStateException("The mod instance has not been initialized yet!");
        }
        return instance;
    }

    private void loadConfig(@NotNull ModConfigEvent.Loading event) {
        serverConfig.onLoad(event);
        clientConfig.onLoad(event);
    }

    private void reloadConfig(@NotNull ModConfigEvent.Reloading event) {
        serverConfig.onReload(event);
        clientConfig.onReload(event);
    }

    private void unloadConfig(@NotNull ModConfigEvent.Unloading event) {
        serverConfig.onUnload(event);
        clientConfig.onUnload(event);
    }

    private void registerCapabilities(@NotNull RegisterCapabilitiesEvent event) {
        ModBlockEntityTypes.registerCapabilities(event);
    }

    private void registerPayloadHandlers(@NotNull RegisterPayloadHandlersEvent event) {
        ModNetworks.registerPayloadHandlers(event.registrar("1"));
    }

    private void enqueueIMC(@NotNull InterModEnqueueEvent event) {
        ModIMC.enqueueIMC(event);
    }

    private void registerClientExtensions(@NotNull RegisterClientExtensionsEvent event) {
        ModItems.registerClientExtensions(event);
    }

    private void registerRenderers(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        ModBlockEntityTypes.registerRenderers(event);
    }

    private void registerMenuScreens(@NotNull RegisterMenuScreensEvent event) {
        ModMenuTypes.registerMenuScreens(event);
    }
}
