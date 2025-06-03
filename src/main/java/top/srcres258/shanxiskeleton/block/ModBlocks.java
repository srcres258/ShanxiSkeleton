package top.srcres258.shanxiskeleton.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonBreederBlock;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonProducerBlock;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonSlaughtererBlock;
import top.srcres258.shanxiskeleton.item.ModItems;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ShanxiSkeleton.MOD_ID);

    /**
     * 凋灵骷髅生产器（Wither Skeleton Producer）
     */
    public static final DeferredBlock<Block> WITHER_SKELETON_PRODUCER = registerBlockWithItem("wither_skeleton_producer",
            name -> BLOCKS.registerBlock(name, WitherSkeletonProducerBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    /**
     * 凋灵骷髅繁殖器（Wither Skeleton Breeder）
     */
    public static final DeferredBlock<Block> WITHER_SKELETON_BREEDER = registerBlockWithItem("wither_skeleton_breeder",
            name -> BLOCKS.registerBlock(name, WitherSkeletonBreederBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    /**
     * 凋灵骷髅屠宰器（Wither Skeleton Slaughterer）
     */
    public static final DeferredBlock<Block> WITHER_SKELETON_SLAUGHTERER = registerBlockWithItem("wither_skeleton_slaughterer",
            name -> BLOCKS.registerBlock(name, WitherSkeletonSlaughtererBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    /**
     * 框架方块（Frame Block）
     */
    public static final DeferredBlock<Block> FRAME_BLOCK = registerBlockWithItem("frame_block",
            name -> BLOCKS.registerBlock(name, Block::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));

    @NotNull
    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(
            @NotNull String name,
            @NotNull Function<String, DeferredBlock<T>> blockFunc
    ) {
        DeferredBlock<T> result;

        result = blockFunc.apply(name);
        registerBlockItem(name, result);

        return result;
    }

    private static <T extends Block> void registerBlockItem(
            @NotNull String name,
            @NotNull DeferredBlock<T> block
    ) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                .useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,
                        ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID, name)))));
    }

    public static void register(@NotNull IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    /**
     * 构建模组创造模式物品栏中的内容。
     */
    public static void buildModCreativeModeTab(
            @NotNull CreativeModeTab.ItemDisplayParameters parameters,
            @NotNull CreativeModeTab.Output output
    ) {
        output.accept(WITHER_SKELETON_PRODUCER);
        output.accept(WITHER_SKELETON_BREEDER);
        output.accept(WITHER_SKELETON_SLAUGHTERER);
        output.accept(FRAME_BLOCK);
    }
}
