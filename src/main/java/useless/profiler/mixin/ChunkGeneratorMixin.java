package useless.profiler.mixin;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.profiler.Profiler;

@Mixin(value = ChunkGenerator.class, remap = false, priority = 0)
public class ChunkGeneratorMixin {
	@Inject(method = "decorate(Lnet/minecraft/core/world/chunk/Chunk;)V", at = @At("HEAD"))
	private void decorateHEAD(Chunk chunk, CallbackInfo ci){
		Profiler.methodStart("Minecraft","ChunkDecorate");
	}
	@Inject(method = "decorate(Lnet/minecraft/core/world/chunk/Chunk;)V", at = @At("TAIL"))
	private void decorateTAIL(Chunk chunk, CallbackInfo ci){
		Profiler.methodEnd("Minecraft","ChunkDecorate");
	}
}
