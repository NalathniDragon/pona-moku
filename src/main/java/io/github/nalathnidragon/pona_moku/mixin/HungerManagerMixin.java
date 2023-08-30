package io.github.nalathnidragon.pona_moku.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
	@Inject(method = "update",at=@At("HEAD"), cancellable = true)
	private void pona_moku$dontUpdate(PlayerEntity player, CallbackInfo ci)
	{
		ci.cancel();
	}
}
