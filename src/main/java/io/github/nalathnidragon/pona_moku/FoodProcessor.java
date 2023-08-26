package io.github.nalathnidragon.pona_moku;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FoodProcessor {
	public static float HEALTH_SCALE = 1;
	public static float ABSORPTION_SCALE = 1;

	public static Map<Item,Map<StatusEffect,Integer>> staticFoodBuffs;

	static {
		staticFoodBuffs = new HashMap<>();
		//TODO: load these from a config file of e.g. {"minecraft:cookie": {"minecraft:speed": 1}}
	}

	public static boolean isFoodEffect(StatusEffectInstance effect, LivingEntity statusHaver)
	{
		return effect.isInfinite();
	}

	//TODO: make a mixin to inject calls to clearFoodEffects and applyFoodHealthToEntity at the top of LivingEntity.applyFoodEffects
	// (we will probably replace applyFoodEffects entirely at some point in order to account for dynamic foods)

	public static Collection<StatusEffectInstance> clearFoodEffects(LivingEntity entity)
	{
		Collection<StatusEffectInstance> clearedStatuses = new ArrayList<>();
		Collection<StatusEffectInstance> activeStatuses = entity.getStatusEffects();
		for (StatusEffectInstance status:activeStatuses) {
			if(isFoodEffect(status, entity)) {
				entity.removeStatusEffect(status.getEffectType());
				clearedStatuses.add(status);
			}
		}
		return clearedStatuses;
	}
	public static boolean applyFoodHealthToEntity(Item item, LivingEntity eater)
	{
		if(item.isFood())
		{
			FoodComponent food = item.getFoodComponent();
			//If they have the absorption effect, clear it so that it doesn't mess with the food's absorption when it expires
			eater.removeStatusEffect(StatusEffects.ABSORPTION);
			eater.setAbsorptionAmount(food.getHunger() * food.getSaturationModifier() * ABSORPTION_SCALE);
			eater.heal(food.getHunger() * HEALTH_SCALE);
			return true;
		}
		else
		{
			PonaMoku.LOGGER.error("tried to apply food health from non-food item "+item);
			return false;
		}
	}
}
