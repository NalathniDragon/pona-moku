package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import io.github.nalathnidragon.pona_moku.config.PonaMokuConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	public abstract boolean isUsingItem();

	@Shadow
	public abstract ItemStack getActiveItem();

	@Shadow
	public abstract void stopUsingItem();

	@Inject(method = "applyFoodEffects", at = @At("HEAD"), cancellable=true)
	private void applyFoodHealing(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		FoodProcessor.clearFoodEffects(targetEntity);
		FoodProcessor.applyFoodHealthToEntity(stack.getItem(),targetEntity);
		FoodProcessor.applyFoodStatusesToEntity(stack,targetEntity);
	}

	@Inject(method="damage",at=@At("HEAD"))
	private void interruptEating(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		float threshold = PonaMokuConfig.instance.interrupt_eating_threshold.value();
		if(threshold == 0) return;
		if(amount >= threshold && isUsingItem() && getActiveItem().isFood()) stopUsingItem();
	}
}
