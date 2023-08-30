package io.github.nalathnidragon.pona_moku.config;

import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import io.github.nalathnidragon.pona_moku.PonaMoku;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.api.values.ValueList;
import org.quiltmc.config.api.values.ValueMap;
import org.quiltmc.loader.api.config.v2.QuiltConfig;

public final class PonaMokuConfig extends ReflectiveConfig {
	public static final PonaMokuConfig instance =
		QuiltConfig.create(
			PonaMoku.MODID, // what the folder is called ".../.minecraft/config/pona_moku/" in this case
			"config", // what the file is called. "config.toml" in this case
			PonaMokuConfig.class);
	@Comment("Food's nourishment is multiplied by this to determine the health it restores.")
	public final TrackedValue<Float> health_scale = value(1f);
	@Comment("Food's saturation is multiplied by this to determine the amount of absorption it grants.")
	public final TrackedValue<Float> absorption_scale = value(1f);

	@Comment("Maximum absorption that food can provide, in half-hearts.")
	public final TrackedValue<Float> max_absorption_from_food = value(20f);

	@Comment("Time it takes to eat food based on the absorption provided, in ticks. Default matches bread in vanilla.")
	public final TrackedValue<Float> eat_time_per_absorption = value(5.33f);

	@Comment("Time it takes to eat food based on the health it heals.")
	public final TrackedValue<Float> eat_time_per_health = value(0f);

	@Comment("Minimum time taken for food to be eaten, in ticks.")
	public final TrackedValue<Integer> min_eat_time = value(8);

	@Comment("Whether the Nausea status should prevent the player from being able to eat.")
	public final TrackedValue<Boolean> nausea_prevents_eating = value(true);
/*
	@Comment("Test Value")
	public final TrackedValue<Double> test = value(1.0);
	@Comment("Test List")
	@FloatRange(min = -10.0, max = 10.0)
	// The first value, 0.0, sets the default value. The created list is: test2 = [1.0, -2.0]
	public final TrackedValue<ValueList<Double>> test2 = value(ValueList.create(0.0, 1.0, -2.0));

	@Comment("Test Map")
	public final TrackedValue<ValueMap<Integer>> test3 = value(ValueMap
		.builder(0) // default value
		.put("one", 1)
		.put("two", 2)
		.put("three", 3)
		.build());

	// Note: Default values aren't automatically used.
	// test2.value().get(5) will throw an indexOutOfBoundsException
	// test3.value().get("five") evaluates to null

	public final GeneralConfig general = new GeneralConfig();

	public static class GeneralConfig extends Section {
		@Comment("Test Value 1")
		@FloatRange(min = -10.0, max = 10.0)
		public final TrackedValue<Double> test = value(1.0);
	}*/
}
