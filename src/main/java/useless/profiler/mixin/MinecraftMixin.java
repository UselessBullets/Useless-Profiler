package useless.profiler.mixin;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.profiler.Profiler;

@Mixin(value = Minecraft.class, remap = false, priority = 0)
public class MinecraftMixin {
	@Shadow
	private int ticksRan;

	@Inject(method = "runTick()V", at = @At("HEAD"))
	private void checkProfileTimes(CallbackInfo ci){
		if (ticksRan % Profiler.poolDelayTicks == 0){
			Profiler.printTimesInRespectToID(Profiler.MOD_ID_TO_MEASURE, Profiler.METHOD_ID_TO_MEASURE);
			Profiler.getLongestTimes().clear();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_BACK)){
			Profiler.LOGGER.info("CLEARING TIMES");
			Profiler.clearTimes();
		}
		Profiler.methodStart("Minecraft","tick");
	}
	@Inject(method = "runTick()V", at = @At("TAIL"))
	private void tickEnd(CallbackInfo ci){
		Profiler.methodEnd("Minecraft", "tick");
	}
}
