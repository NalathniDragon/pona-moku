package io.github.nalathnidragon.pona_moku.mixin;

import io.github.nalathnidragon.pona_moku.FoodProcessor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
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
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isFood();

	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract int getMaxUseTime();

	@Inject(method = "getTooltip", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void appendHealth(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List list){
		if(this.isFood())
		{
			FoodComponent food=this.getItem().getFoodComponent();
			float absorbHearts = FoodProcessor.absorptionFrom(food) / 2;
			float healHearts = FoodProcessor.healingFrom(food) / 2;
			MutableText label=Text.literal(String.format("❤+%.1f",healHearts)).formatted(Formatting.RED);
			label=label.append(Text.literal(String.format(" ❤%.1f",absorbHearts)).formatted(Formatting.YELLOW));
			label=label.append(Text.literal(String.format(" %.1fs",this.getMaxUseTime()/20.0)).formatted(Formatting.GRAY));
			list.add(label);
		}
		Map<StatusEffect,Integer> statuses = FoodProcessor.staticFoodBuffs.get(this.getItem());
		if(statuses != null)
		{
			for (StatusEffect status:statuses.keySet())
			{
				int amplifier = statuses.get(status);
				MutableText label = Text.empty().append(status.getName());
				if(amplifier>0) label= label.append(" ").append(Text.translatable("potion.potency." +amplifier));

				if(amplifier<0) label=Text.literal("As ingredient: ").append(label).formatted(Formatting.ITALIC,Formatting.DARK_GRAY);
				else label=label.formatted(status.getType().getFormatting());
				list.add(label);
			}
		}
	}
}
