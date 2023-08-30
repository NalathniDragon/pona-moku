package io.github.nalathnidragon.pona_moku;

import io.github.nalathnidragon.pona_moku.config.FoodStatusConfig;
import io.github.nalathnidragon.pona_moku.mixin.HiddenEffectAccessorMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FoodProcessor {
	private static float HEALTH_SCALE = 1;
	private static float ABSORPTION_SCALE = 1;
	private static float MAX_ABSORPTION_FROM_FOOD = 20;

	private static int MIN_EAT_TIME = 8;
	private static float EAT_TIME_PER_ABSORPTION = 32/6f; //bread standard
	private static float EAT_TIME_PER_HEAL = 0; //defaults: less hearty foods are quicker to eat
	public static Map<Item,Map<StatusEffect,Integer>> staticFoodBuffs;
	public static Map<Item,FoodProxyInfo> proxies;
	static {
		staticFoodBuffs = FoodStatusConfig.getFoodStatus();
		proxies = new HashMap<>();
		proxies.put(Items.CAKE,new FoodProxyInfo(2, 0.1f, 6));
		// future: for item IDs missing from the config, derive statuses from ingredient effect strengths in recipe tree
	}
	public static void reloadConfig()
	{
		staticFoodBuffs = FoodStatusConfig.getFoodStatus();
	}

	public static Collection<StatusEffectInstance> getStatusInstancesFrom(ItemStack stack){
		Collection<StatusEffectInstance> statuses = new ArrayList<StatusEffectInstance>();
		//TODO: itemstack's dynamic statuses should preempt static statuses from food type
		Map<StatusEffect,Integer> staticEffects = staticFoodBuffs.get(stack.getItem());
		if(staticEffects != null){
			for(StatusEffect s:staticEffects.keySet())
			{
				if(staticEffects.get(s) >= 0) statuses.add(new StatusEffectInstance(s, StatusEffectInstance.INFINITE_DURATION,staticEffects.get(s),true,true));
			}
		}
		return statuses;
	}
	public static void eatProxy(LivingEntity eater, Item item)
	{
		FoodProxyInfo proxyInfo = proxies.get(item);
		if(proxyInfo != null) {
			clearFoodEffects(eater);
			applyHungerScaledHealth(eater, proxyInfo.hunger * HEALTH_SCALE, proxyInfo.hunger * proxyInfo.saturationMultiplier * 2 * ABSORPTION_SCALE);
			applyFoodStatusesToEntity(item.getDefaultStack(), eater);
		}
		else PonaMoku.LOGGER.error("tried to eat proxy for "+item.getName()+" but it wasn't in proxy map");
	}

	public static Text tooltipProxy(FoodProxyInfo info){
		float absorbHearts = info.hunger * info.saturationMultiplier * ABSORPTION_SCALE * 2 / 2; //double sat, half heart, for clarity
		float healHearts = info.hunger * HEALTH_SCALE / 2;
		MutableText label=Text.literal(String.format("❤+%.1f",healHearts)).formatted(Formatting.RED);
		label=label.append(Text.literal(String.format(" ❤%.1f",absorbHearts)).formatted(Formatting.YELLOW));
		label=label.append(Text.literal(String.format(" x%d",info.slices)).formatted(Formatting.GRAY));
		return label;
	}
	public static float healingFrom(FoodComponent food)
	{
		return food.getHunger() * HEALTH_SCALE;
	}
	public static float absorptionFrom(FoodComponent food)
	{
		//internal saturation modifier is half of what its effective value is, so we need to multiply by 2 here
		return Math.min(food.getHunger() * food.getSaturationModifier() * 2 * ABSORPTION_SCALE, MAX_ABSORPTION_FROM_FOOD);
	}

	public static int eatTime(FoodComponent food)
	{
		//divide by healing and absorption scale so they can be adjusted without also changing eat time
		return Math.round(Math.max(MIN_EAT_TIME, healingFrom(food) * EAT_TIME_PER_HEAL / HEALTH_SCALE + absorptionFrom(food) * EAT_TIME_PER_ABSORPTION / ABSORPTION_SCALE));
	}

	public static boolean isFoodEffect(StatusEffectInstance effect, LivingEntity statusHaver)
	{
		if(effect==null) return false;
		if(effect.isInfinite() && effect.isAmbient()) return true;
		return false;
	}



	public static void clearFoodEffects(LivingEntity entity)
	{
		Collection<StatusEffectInstance> clearedStatuses = new ArrayList<>();
		//need to clone the list to avoid ConcurrentModificationException
		Collection<StatusEffectInstance> activeStatuses = new ArrayList<>(entity.getStatusEffects());
		for (StatusEffectInstance status:activeStatuses) {
			if(isFoodEffect(status, entity)) {
				entity.removeStatusEffect(status.getEffectType());
				clearedStatuses.add(status);
			} else if (isFoodEffect(((HiddenEffectAccessorMixin)status).getHiddenEffect(),entity)){
				((HiddenEffectAccessorMixin)status).setHiddenEffect(null);
			}
		}
	}

	public static void applyHungerScaledHealth(LivingEntity target, float health, float absorption)
	{
		float scale=1;
		if(target.hasStatusEffect(StatusEffects.HUNGER)) {
			float hunger = target.getStatusEffect(StatusEffects.HUNGER).getAmplifier() + 1;
			scale /= (hunger+1);
		}
		if(target.hasStatusEffect(StatusEffects.SATURATION)) {
			float saturation = target.getStatusEffect(StatusEffects.SATURATION).getAmplifier() + 1;
			scale *= (saturation+1);
		}
		//If they have the absorption effect, clear it so that it doesn't mess with the food's absorption when it expires
		target.removeStatusEffect(StatusEffects.ABSORPTION);
		target.setAbsorptionAmount(scale*absorption);
		target.heal(scale*health);
	}
	public static boolean applyFoodHealthToEntity(Item item, LivingEntity eater)
	{
		if(item.isFood())
		{
			FoodComponent food = item.getFoodComponent();
			applyHungerScaledHealth(eater,healingFrom(food),absorptionFrom(food));
			return true;
		}
		else
		{
			PonaMoku.LOGGER.error("tried to apply food health from non-food item "+item);
			return false;
		}
	}
	public static void applyFoodStatusesToEntity(ItemStack stack, LivingEntity eater)
	{
		Collection<StatusEffectInstance> statuses = getStatusInstancesFrom(stack);
		for(StatusEffectInstance instance:statuses){
			eater.addStatusEffect(instance);
		}
	}
}
