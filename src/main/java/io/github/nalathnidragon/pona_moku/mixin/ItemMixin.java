package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import io.github.nalathnidragon.pona_moku.PonaMoku;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
	@Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable=true)
	public void eatDelay(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if(stack.isFood()) {
			cir.setReturnValue(FoodProcessor.eatTime(stack.getItem().getFoodComponent()));
			cir.cancel();
		}
	}
}
