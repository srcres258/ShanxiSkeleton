package top.srcres258.shanxiskeleton.compat;

import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.compat.theoneprobe.ModTOPModule;

public class ModIMC {
    public static void enqueueIMC(@NotNull InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", ModTOPModule::new);
        }
    }
}
