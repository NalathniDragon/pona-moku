package io.github.nalathnidragon.pona_moku.config;

import io.github.nalathnidragon.pona_moku.PonaMoku;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.impl.lib.electronwill.nightconfig.core.CommentedConfig;
import org.quiltmc.loader.impl.lib.electronwill.nightconfig.core.Config;
import org.quiltmc.loader.impl.lib.electronwill.nightconfig.toml.TomlFormat;
import org.quiltmc.loader.impl.lib.electronwill.nightconfig.toml.TomlParser;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


public abstract class FoodStatusConfig {
	private static CommentedConfig config = null;

	// only getFoodStatus accesses this. It will generate this if needed.
	private static Map<Item, Map<StatusEffect, Integer>> foodStatus = null;

	public static boolean loadConfig() {
		foodStatus = null; // mark foodStatus for reconstruction

		ModContainer ponamoku = QuiltLoader.getModContainer(PonaMoku.MODID).get();
		try {
			File file = new File(
				QuiltLoader.getConfigDir().toFile() + File.separator + PonaMoku.MODID,
				"food_status_effects.toml"
			);

			if (!file.exists()){
				if (!file.createNewFile()) throw new RuntimeException("Cannot create file %s".formatted(file.toString()));
				try (
					InputStream input = new BufferedInputStream(ponamoku.getClass().getResourceAsStream("/config/food_status_effects.toml"));
					OutputStream output = new BufferedOutputStream(new FileOutputStream(file))
				){output.write(input.readAllBytes());}
			}

			try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
				config = new TomlParser().parse(reader);
			}
		} catch (Exception e) {
			PonaMoku.LOGGER.error("Error attempting to parse food_status_effects.toml. " +
				"Food will not have any status effects.", e);
			config = TomlFormat.instance().createConfig(); // blank
			return false;
		}
		return true;
	}

	public static Map<Item, Map<StatusEffect, Integer>> getFoodStatus() {
		if (foodStatus != null) return foodStatus; // No need to reconstruct our cached value
		if (config == null) loadConfig();

		Map<Item, Map<StatusEffect, Integer>> map = new HashMap<>();
		for(String itemID : config.valueMap().keySet()) {
			Config foodMap = config.get(itemID);
			Item item = Registries.ITEM.get(new Identifier(itemID));
			// TODO: Throw an error? Warn the user somehow that the itemID was invalid
			if (item == Items.AIR) continue; // AIR seems to be the default. itemID was invalid or user wants to eat air
			// the above check is made redundant by the check below
			// but having 2 checks might make more meaningful error messages when those are implemented
			if (!item.isFood()) continue;
			for(String effectID : foodMap.valueMap().keySet()) {
				StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(effectID));
				// TODO: Throw an error? Warn the user somehow that the effectID was invalid
				if(effect == null) continue;
				Integer amp = foodMap.get(effectID);
				if (amp == null) continue; // TODO: again, warn the user / throw and error
				Map<StatusEffect, Integer> effects = map.getOrDefault(item, new HashMap<>());
				effects.put(effect, amp);
				map.put(item, effects);
			}
		}
		return foodStatus = map;
	}
}
