package fudge.notenoughcrashes.mixins.client;

import fudge.notenoughcrashes.NotEnoughCrashes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.ReportType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.nio.file.Path;

/**
 * Only applied to client side because we aim to change the functionality of the integrated server (and not the dedicated one)
 */
@Mixin(MinecraftServer.class)
public class MixinMinecraftServerClientOnly {
    /**
     * We write the log anyway using CrashUtils.outputReport in
     * {@link fudge.notenoughcrashes.mixinhandlers.InGameCatcher#displayCrashScreen(CrashReport, int, boolean)}
     */
    @Redirect(method = "runServer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;writeToFile(Ljava/nio/file/Path;Lnet/minecraft/util/crash/ReportType;)Z"))
    private boolean disableIntegratedServerWriteToFileOnCrash(CrashReport instance, Path path, ReportType type) {
        if (NotEnoughCrashes.enableGameloopCatching()) return true;
        else return instance.writeToFile(path, type);
    }
}
