package useless.profiler.mixin;

import net.minecraft.client.render.RenderEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.profiler.Profiler;

@Mixin(value = RenderEngine.class, remap = false, priority = 0)
public class RenderEngineMixin {
	@Inject(method = "updateDynamicTextures()V", at = @At("HEAD"))
	private void updateDynamicHEAD(CallbackInfo ci){
		Profiler.methodStart("Minecraft", "updateDynamicTextures");
	}
	@Inject(method = "updateDynamicTextures()V", at = @At("TAIL"))
	private void updateDynamicTAIL(CallbackInfo ci){
		Profiler.methodEnd("Minecraft", "updateDynamicTextures");
	}
}
