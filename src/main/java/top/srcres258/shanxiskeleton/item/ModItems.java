package top.srcres258.shanxiskeleton.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.item.custom.WitherSkeletonCatcherItem;
import top.srcres258.shanxiskeleton.item.custom.WitherSkeletonItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ShanxiSkeleton.MOD_ID);

    /**
     * 凋灵骷髅捕捉器（Wither Skeleton Catcher）
     */
    public static final DeferredItem<Item> WITHER_SKELETON_CATCHER = ITEMS.registerItem("wither_skeleton_catcher",
            WitherSkeletonCatcherItem::new, new Item.Properties().durability(256).stacksTo(1));
    /**
     * 凋灵骷髅（Wither Skeleton）
     */
    public static final DeferredItem<Item> WITHER_SKELETON = ITEMS.registerItem("wither_skeleton",
            WitherSkeletonItem::new, new Item.Properties().stacksTo(16));
    /**
     * 小块煤炭（Tiny Coal）
     */
    public static final DeferredItem<Item> TINY_COAL = ITEMS.registerItem("tiny_coal",
            Item::new, new Item.Properties().stacksTo(64));

    public static void register(@NotNull IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static void registerClientExtensions(@NotNull RegisterClientExtensionsEvent event) {
        event.registerItem(WitherSkeletonItem.ClientItemExtensions.INSTANCE, WITHER_SKELETON);
    }

    /**
     * 构建模组创造模式物品栏中的内容。
     */
    public static void buildModCreativeModeTab(
            @NotNull CreativeModeTab.ItemDisplayParameters parameters,
            @NotNull CreativeModeTab.Output output
    ) {
        output.accept(WITHER_SKELETON_CATCHER);
        output.accept(WITHER_SKELETON);
        output.accept(TINY_COAL);
    }
}
