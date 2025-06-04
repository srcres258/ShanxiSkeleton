package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;

public class ModModelTemplates {
    public static final ModelTemplate MACHINE = ModelTemplates.create(String.format("%s:machine",
            ShanxiSkeleton.MOD_ID), ModTextureSlots.FRONT, ModTextureSlots.BACK);
}
