package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ItemTag extends BaseRegistryTag<Item> {
    protected ItemTag(HolderSet.@NotNull Named<Item> holderSet) {
        super(holderSet);
    }

    @Override
    protected @NotNull Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }
}
