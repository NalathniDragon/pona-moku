package io.github.nalathnidragon.pona_moku.mixin;

import net.minecraft.entity.player.HungerConstants;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// For now, set player hunger to max every tick
@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {

	@Shadow
	private int foodLevel;

	@Shadow
	private float foodSaturationLevel;

	@Inject(method = "add(IF)V", at = @At("RETURN"))
	public void add(CallbackInfo info) {
		foodLevel = HungerConstants.FULL_FOOD_LEVEL;
		foodSaturationLevel = HungerConstants.MAX_SATURATION;
	}

	// Ignore whatever normally happens every tick and set hunger and saturation to max before the method returns
	@Inject(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("RETURN"))
	public void update(CallbackInfo info) {
		foodLevel = HungerConstants.FULL_FOOD_LEVEL;
		foodSaturationLevel = HungerConstants.MAX_SATURATION;
	}
}
