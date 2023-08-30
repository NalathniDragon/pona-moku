package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import io.github.nalathnidragon.pona_moku.config.PonaMokuConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin{
	//TODO temporary fix to allow always consuming for testing purposes, this check actually needs to be handled elsewhere
	@Inject(method = "canConsume", at = @At("HEAD"), cancellable=true)
	public void overrideConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
		if(PonaMokuConfig.instance.nausea_prevents_eating.value()) {
			cir.setReturnValue(!((PlayerEntity) (Object) this).hasStatusEffect(StatusEffects.NAUSEA));
		}
		else {
			cir.setReturnValue(true);
		}
		cir.cancel();
	}
}
