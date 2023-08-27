package io.github.nalathnidragon.pona_moku.mixin;

import net.minecraft.entity.player.HungerConstants;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// For now, set player hunger to max every tick
@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {

	@Shadow
	public abstract void setFoodLevel(int foodLevel);

	@Shadow
	public abstract void setSaturationLevel(float saturationLevel);


	// Ignore whatever normally happens every tick and set hunger and saturation to max before the method returns
	// XXX: This should skip vanilla code instead? As is, it respects the 'naturalRegeneration' gamerule but that could
	// be done manually.
	@Inject(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("RETURN"))
	public void update(PlayerEntity player, CallbackInfo info) {
		setFoodLevel(HungerConstants.FULL_FOOD_LEVEL);
		setSaturationLevel(HungerConstants.MAX_SATURATION);
	}

	// always not full. Always ready to eat more.
	// TODO: actual requirements as detailed in issue #6
	// TODO: This seems to be working in PlayerEntityMixin#overrideConsume so can perhaps remove this
	@Inject(method = "isNotFull()Z", at = @At("RETURN"), cancellable = true)
	public void isNotFull(CallbackInfoReturnable<Boolean> returnable) {
		returnable.setReturnValue(true);
	}
}
