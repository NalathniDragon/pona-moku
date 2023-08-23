package io.github.NalathniDragon.PonaMoku.mixin;

import io.github.NalathniDragon.PonaMoku.PonaMoku;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
		PonaMoku.LOGGER.info("This line is printed by an example mod mixin!");
	}
}
