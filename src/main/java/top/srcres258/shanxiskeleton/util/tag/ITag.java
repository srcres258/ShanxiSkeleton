package top.srcres258.shanxiskeleton.util.tag;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ITag<T> {
    @NotNull
    ResourceLocation getName();

    boolean contains(@NotNull T value);

    @NotNull
    List<T> getAll();
}
