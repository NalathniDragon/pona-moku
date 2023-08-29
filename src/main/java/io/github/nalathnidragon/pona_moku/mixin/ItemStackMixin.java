package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isFood();

	@Shadow
	public abstract Item getItem();

	@Inject(method = "getTooltip", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void appendHealth(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List list){
		if(this.isFood())
		{
			float absorbHearts = FoodProcessor.absorptionFrom(this.getItem().getFoodComponent()) / 2;
			float healHearts = FoodProcessor.healingFrom(this.getItem().getFoodComponent()) / 2;
			list.add(Text.literal("❤+"+healHearts).formatted(Formatting.RED).append(Text.literal(" ❤"+healHearts).formatted(Formatting.YELLOW)));
		}
	}
}
