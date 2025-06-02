package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SingleElementTag<T> implements ITag<T> {
    private final ResourceLocation name;
    private final T element;
    private final List<T> list;

    public SingleElementTag(@NotNull ResourceLocation name, @NotNull T element) {
        this.name = name;
        this.element = element;
        this.list = Collections.singletonList(element);
    }

    @NotNull
    public T getElement() {
        return element;
    }

    @Override
    @NotNull
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public boolean contains(@NotNull T value) {
        return element == value;
    }

    @Override
    @NotNull
    public List<T> getAll() {
        return list;
    }
}
