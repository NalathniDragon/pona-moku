package io.github.nalathnidragon.pona_moku.config;

import io.github.nalathnidragon.pona_moku.PonaMoku;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.api.values.ValueMap;
import org.quiltmc.loader.api.config.v2.QuiltConfig;

import java.util.HashMap;
import java.util.Map;

public class FoodStatusConfig extends ReflectiveConfig {
	public static final FoodStatusConfig instance =
		QuiltConfig.create(
			PonaMoku.MODID, // what the folder is called ".../.minecraft/config/pona_moku/" in this case
			"food_status_effects", // what the file is called. "food_status_effects.toml" in this case
			FoodStatusConfig.class);

	public final TrackedValue<ValueMap<ValueMap<Integer>>> foods =
		value(
			ValueMap.builder(
				ValueMap.builder(0).build()
			).build()
		);

	public Map<Item, Map<StatusEffect, Integer>> getFoodStatus() {
		// TODO: libraries can do this with much simpler code. I should relearn how to use those :P
		Map<Item, Map<StatusEffect, Integer>> map = new HashMap<>();
		for(String itemID : foods.value().keySet()) {
			Item item = Registries.ITEM.get(new Identifier(itemID));
			// TODO: Throw an error? Warn the user somehow that the itemID was invalid
			if (item == Items.AIR) continue; // AIR seems to be the default. itemID was invalid or user wants to eat air
			// the above check is made redundant by the check below
			// but having 2 checks might make more meaningful error messages when those are implemented
			if (!item.isFood()) continue;
			for(String effectID : foods.value().get(itemID).keySet()){
				StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(effectID));
				// TODO: Throw an error? Warn the user somehow that the effectID was invalid
				// Luck is the default value. If the effect is luck but the effect id isn't luck, the entry is invalid
				if(effect == StatusEffects.LUCK && !effectID.equals("minecraft:luck")) continue;
				Integer amp = foods.value().get(itemID).get(effectID);
				if (amp == null) continue; // TODO: again, warn the user / throw and error
				Map<StatusEffect, Integer> effects = map.getOrDefault(item, new HashMap<>());
				effects.put(effect, amp);
				map.put(item, effects);
			}
		}
		return map;
	}
}
