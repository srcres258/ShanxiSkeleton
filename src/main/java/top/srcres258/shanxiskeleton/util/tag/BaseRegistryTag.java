package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseRegistryTag<T> implements ITag<T> {
    protected final HolderSet.Named<T> holderSet;

    protected BaseRegistryTag(@NotNull HolderSet.Named<T> holderSet) {
        this.holderSet = holderSet;
    }

    @Override
    @NotNull
    public ResourceLocation getName() {
        return holderSet.key().location();
    }

    @Override
    public boolean contains(@NotNull T value) {
        Registry<T> registry;

        registry = getRegistry();

        return registry.getHolder(registry.getId(value))
                .map(ref -> ref.is(holderSet.key())).orElse(false);

    }

    @Override
    @NotNull
    public List<T> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }

    @NotNull
    protected abstract Registry<T> getRegistry();
}
