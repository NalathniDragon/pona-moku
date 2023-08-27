package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	//TODO temporary fix to allow always consuming for testing purposes, this check actually needs to be handled elsewhere
	@Inject(method = "canConsume", at = @At("HEAD"), cancellable=true)
	public void overrideConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
		cir.cancel();
	}
}
