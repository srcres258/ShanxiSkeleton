package top.srcres258.shanxiskeleton.block;

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

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ShanxiSkeleton.MOD_ID);

    /**
     * 凋灵骷髅生产器（Wither Skeleton Producer）
     */
    public static final DeferredBlock<Block> WITHER_SKELETON_PRODUCER = registerBlockWithItem("wither_skeleton_producer",
            () -> new WitherSkeletonProducerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    /**
     * 凋灵骷髅繁殖器（Wither Skeleton Breeder）
     */
    public static final DeferredBlock<Block> WITHER_SKELETON_BREEDER = registerBlockWithItem("wither_skeleton_breeder",
            () -> new WitherSkeletonBreederBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    /**
     * 凋灵骷髅屠宰器（Wither Skeleton Slaughterer）
     */
    public static final DeferredBlock<Block> WITHER_SKELETON_SLAUGHTERER = registerBlockWithItem("wither_skeleton_slaughterer",
            () -> new WitherSkeletonSlaughtererBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    /**
     * 框架方块（Frame Block）
     */
    public static final DeferredBlock<Block> FRAME_BLOCK = registerBlockWithItem("frame_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));

    @NotNull
    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(
            @NotNull String name,
            @NotNull Supplier<T> block
    ) {
        DeferredBlock<T> result;

        result = BLOCKS.register(name, block);
        registerBlockItem(name, result);

        return result;
    }

    private static <T extends Block> void registerBlockItem(
            @NotNull String name,
            @NotNull DeferredBlock<T> block
    ) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
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
