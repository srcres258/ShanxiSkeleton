package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ShanxiSkeleton.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void onGatherData(@NotNull GatherDataEvent.Client event) {
        DataGenerator generator;
        PackOutput packOutput;
        CompletableFuture<HolderLookup.Provider> lookupProvider;
        ModBlockTagsProvider blockTagsProvider;
        ModItemTagsProvider itemTagsProvider;

        generator = event.getGenerator();
        packOutput = generator.getPackOutput();
        lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new LootTableProvider(packOutput, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new,
                        LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));

        blockTagsProvider = new ModBlockTagsProvider(packOutput, lookupProvider);
        itemTagsProvider = new ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter());
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, itemTagsProvider);

        generator.addProvider(true, new ModDataMapProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ModModelProvider(packOutput));

        generator.addProvider(true, new ModDatapackProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModGlobalLootModifierProvider(packOutput, lookupProvider));
    }
}
