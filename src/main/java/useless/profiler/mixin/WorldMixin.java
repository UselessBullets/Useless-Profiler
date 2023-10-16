package useless.profiler.mixin;

import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.profiler.Profiler;

@Mixin(value = World.class, remap = false, priority = 0)
public class WorldMixin {
	@Inject(method = "tick()V", at = @At("HEAD"))
	private void tickHEAD(CallbackInfo ci){
		Profiler.methodStart("Minecraft", "worldTick");
	}
	@Inject(method = "tick()V", at = @At("TAIL"))
	private void tickTAIL(CallbackInfo ci){
		Profiler.methodEnd("Minecraft", "worldTick");
	}
}
