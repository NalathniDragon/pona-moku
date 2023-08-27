package io.github.nalathnidragon.pona_moku;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FoodProcessor {
	private static float HEALTH_SCALE = 1;
	private static float ABSORPTION_SCALE = 1;
	private static float MAX_ABSORPTION_FROM_FOOD = 20;

	private static int MIN_EAT_TIME = 8;
	private static float EAT_TIME_PER_ABSORPTION = 8;
	private static float EAT_TIME_PER_HEAL = 0; //defaults: less hearty foods are quicker to eat
	private static Map<Item,Map<StatusEffect,Integer>> staticFoodBuffs;

	static {
		staticFoodBuffs = new HashMap<>();
		//TODO: load these from a config file of e.g. {"minecraft:cookie": {"minecraft:speed": 1}}
		// future: for item IDs missing from the config, derive statuses from ingredient effect strengths in recipe tree
	}
	public static void reloadConfig()
	{
		HashMap<StatusEffect,Integer> rabbit_test = new HashMap<>();
		rabbit_test.put(StatusEffects.JUMP_BOOST, 0);
		staticFoodBuffs.put(Items.COOKED_RABBIT,rabbit_test);
		//TODO: figure out how to get Item and StatusEffect instances from IDs in the config file
	}

	public static Collection<StatusEffectInstance> getStatusInstancesFrom(ItemStack stack){
		Collection<StatusEffectInstance> statuses = new ArrayList<StatusEffectInstance>();
		//TODO: itemstack's dynamic statuses should preempt static statuses from food type
		Map<StatusEffect,Integer> staticEffects = staticFoodBuffs.get(stack.getItem());
		if(staticEffects != null){
			for(StatusEffect s:staticEffects.keySet())
			{
				statuses.add(new StatusEffectInstance(s, StatusEffectInstance.INFINITE_DURATION,staticEffects.get(s)));
			}
		}
		return statuses;
	}
	public static float healingFrom(FoodComponent food)
	{
		return food.getHunger() * HEALTH_SCALE;
	}
	public static float absorptionFrom(FoodComponent food)
	{
		return Math.min(food.getHunger() * food.getSaturationModifier() * ABSORPTION_SCALE, MAX_ABSORPTION_FROM_FOOD);
	}

	//TODO: inject into Item.getMaxUseTime
	public static int eatTime(FoodComponent food)
	{
		return Math.round(Math.max(MIN_EAT_TIME, healingFrom(food) * EAT_TIME_PER_HEAL + absorptionFrom(food) * EAT_TIME_PER_ABSORPTION));
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
			eater.setAbsorptionAmount(absorptionFrom(food));
			eater.heal(healingFrom(food));
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
