package io.github.nalathnidragon.pona_moku.mixin;

import net.minecraft.entity.player.HungerConstants;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

	// I don't think these needs to be serialized because they should be modified and reset in the same tick.
	// The queued saturation and food values are very much intended to be transient.
	@Unique
	private float queuedSaturation = 0;
	@Unique
	private int queuedFood = 0;

	@Inject(method = "add(IF)V", at = @At("RETURN"))
	public void add(int food, float saturationModifier, CallbackInfo info) {
		// kinda duplicated from update but I guess this keeps things from adding negative values
		setFoodLevel(HungerConstants.FULL_FOOD_LEVEL);
		setSaturationLevel(HungerConstants.MAX_SATURATION);
	}

	@Inject(method = "eat(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
	public void eat(Item item, ItemStack itemStack, CallbackInfo info) {
		if(item.isFood()) {
			FoodComponent food = item.getFoodComponent();
			if (food != null) { // it shouldn't if item.isFood() but meh.
				queuedFood += food.getHunger();
				queuedSaturation += food.getSaturationModifier();
			}
		}
	}

	// Ignore whatever normally happens every tick and set hunger and saturation to max before the method returns
	// TODO: This should skip vanilla code instead? As is, it respects the 'naturalRegeneration' gamerule but that could
	// be done manually.
	@Inject(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("RETURN"))
	public void update(PlayerEntity player, CallbackInfo info) {
		setFoodLevel(HungerConstants.FULL_FOOD_LEVEL);
		setSaturationLevel(HungerConstants.MAX_SATURATION);
		player.heal(queuedFood + queuedSaturation); // TODO: Configurable healing
		queuedSaturation = 0;
		queuedFood = 0;
	}

	@Inject(method = "isNotFull()Z", at = @At("RETURN"), cancellable = true)
	public void isNotFull(CallbackInfoReturnable<Boolean> returnable) {
		returnable.setReturnValue(true); // always not full. Always ready to eat more.
	}
}
