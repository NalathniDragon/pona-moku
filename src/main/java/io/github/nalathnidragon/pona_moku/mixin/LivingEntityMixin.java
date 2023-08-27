package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "applyFoodEffects", at = @At("HEAD"), cancellable=true)
	public void applyFoodHealing(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		FoodProcessor.clearFoodEffects(targetEntity);
		FoodProcessor.applyFoodHealthToEntity(stack.getItem(),targetEntity);
		FoodProcessor.applyFoodStatusesToEntity(stack,targetEntity);
	}
}
