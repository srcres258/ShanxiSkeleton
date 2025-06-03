package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class TagFactory {
    public static final ITag<Block> EMPTY_BLOCK_TAG =
            new SingleElementTag<>(BuiltInRegistries.BLOCK.getKey(Blocks.AIR), Blocks.AIR);
    public static final ITag<Item> EMPTY_ITEM_TAG =
            new SingleElementTag<>(BuiltInRegistries.ITEM.getKey(Items.AIR), Items.AIR);
    public static final ITag<Fluid> EMPTY_FLUID_TAG =
            new SingleElementTag<>(BuiltInRegistries.FLUID.getKey(Fluids.EMPTY), Fluids.EMPTY);

    @Nullable
    public static ITag<Block> getBlock(@NotNull String name, boolean nullIfNotFound) {
        ResourceLocation id;
        Optional<HolderSet.Named<Block>> tag;

        if (name.startsWith("#")) {
            id = ResourceLocation.tryParse(name.substring(1));
            if (id == null) {
                return nullIfNotFound ? null : EMPTY_BLOCK_TAG;
            }
            tag = BuiltInRegistries.BLOCK.get(TagKey.create(Registries.BLOCK, id));
            if (tag.isEmpty()) {
                return nullIfNotFound ? null : EMPTY_BLOCK_TAG;
            }
            return new BlockTag(tag.get());
        }
        id = ResourceLocation.tryParse(name);
        if (id == null) {
            return nullIfNotFound ? null : EMPTY_BLOCK_TAG;
        }
        if (!BuiltInRegistries.BLOCK.containsKey(id)) {
            return nullIfNotFound ? null : EMPTY_BLOCK_TAG;
        }
        return new SingleElementTag<>(id, BuiltInRegistries.BLOCK.getValue(id));
    }

    @NotNull
    public static ITag<Block> getBlock(@NotNull String name) {
        return Objects.requireNonNull(getBlock(name, false), genNullPointerMessage("getBlock"));
    }

    @Nullable
    public static ITag<Block> getBlockTag(@NotNull ResourceLocation name, boolean nullIfNotFound) {
        Optional<HolderSet.Named<Block>> tag;

        tag = BuiltInRegistries.BLOCK.get(BlockTags.create(name));
        if (tag.isEmpty()) {
            return nullIfNotFound ? null : EMPTY_BLOCK_TAG;
        }
        return new BlockTag(tag.get());
    }

    @NotNull
    public static ITag<Block> getBlockTag(@NotNull ResourceLocation name) {
        return Objects.requireNonNull(getBlockTag(name, false), genNullPointerMessage("getBlockTag"));
    }

    @Nullable
    public static ITag<Item> getItem(@NotNull String name, boolean nullIfNotFound) {
        ResourceLocation id;
        Optional<HolderSet.Named<Item>> tag;

        if (name.startsWith("#")) {
            id = ResourceLocation.tryParse(name.substring(1));
            if (id == null) {
                return nullIfNotFound ? null : EMPTY_ITEM_TAG;
            }
            tag = BuiltInRegistries.ITEM.get(TagKey.create(Registries.ITEM, id));
            if (tag.isEmpty()) {
                return nullIfNotFound ? null : EMPTY_ITEM_TAG;
            }
            return new ItemTag(tag.get());
        }
        id = ResourceLocation.tryParse(name);
        if (id == null) {
            return nullIfNotFound ? null : EMPTY_ITEM_TAG;
        }
        if (!BuiltInRegistries.ITEM.containsKey(id)) {
            return nullIfNotFound ? null : EMPTY_ITEM_TAG;
        }
        return new SingleElementTag<>(id, BuiltInRegistries.ITEM.getValue(id));
    }

    @NotNull
    public static ITag<Item> getItem(@NotNull String name) {
        return Objects.requireNonNull(getItem(name, false), genNullPointerMessage("getItem"));
    }

    @Nullable
    public static ITag<Item> getItemTag(@NotNull ResourceLocation name, boolean nullIfNotFound) {
        Optional<HolderSet.Named<Item>> tag;

        tag = BuiltInRegistries.ITEM.get(TagKey.create(Registries.ITEM, name));
        if (tag.isEmpty()) {
            return nullIfNotFound ? null : EMPTY_ITEM_TAG;
        }
        return new ItemTag(tag.get());
    }

    @NotNull
    public static ITag<Item> getItemTag(@NotNull ResourceLocation name) {
        return Objects.requireNonNull(getItemTag(name, false), genNullPointerMessage("getItemTag"));
    }

    @Nullable
    public static ITag<Fluid> getFluid(@NotNull String name, boolean nullIfNotFound) {
        ResourceLocation id;
        Optional<HolderSet.Named<Fluid>> tag;

        if (name.startsWith("#")) {
            id = ResourceLocation.tryParse(name.substring(1));
            if (id == null) {
                return nullIfNotFound ? null : EMPTY_FLUID_TAG;
            }
            tag = BuiltInRegistries.FLUID.get(TagKey.create(Registries.FLUID, id));
            if (tag.isEmpty()) {
                return nullIfNotFound ? null : EMPTY_FLUID_TAG;
            }
            return new FluidTag(tag.get());
        }
        id = ResourceLocation.tryParse(name);
        if (id == null) {
            return nullIfNotFound ? null : EMPTY_FLUID_TAG;
        }
        if (!BuiltInRegistries.FLUID.containsKey(id)) {
            return nullIfNotFound ? null : EMPTY_FLUID_TAG;
        }
        return new SingleElementTag<>(id, BuiltInRegistries.FLUID.getValue(id));
    }

    @NotNull
    public static ITag<Fluid> getFluid(@NotNull String name) {
        return Objects.requireNonNull(getFluid(name, false), genNullPointerMessage("getFluid"));
    }

    @Nullable
    public static ITag<Fluid> getFluidTag(@NotNull ResourceLocation name, boolean nullIfNotFound) {
        Optional<HolderSet.Named<Fluid>> tag;

        tag = BuiltInRegistries.FLUID.get(TagKey.create(Registries.FLUID, name));
        if (tag.isEmpty()) {
            return nullIfNotFound ? null : EMPTY_FLUID_TAG;
        }
        return new FluidTag(tag.get());
    }

    @NotNull
    public static ITag<Fluid> getFluidTag(@NotNull ResourceLocation name) {
        return Objects.requireNonNull(getFluidTag(name, false), genNullPointerMessage("getFluidTag"));
    }

    @NotNull
    private static String genNullPointerMessage(@NotNull String method) {
        return String.format("%s with nullIfNotFound = false must not return null.", method);
    }
}
