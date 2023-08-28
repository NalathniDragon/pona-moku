package io.github.nalathnidragon.pona_moku.config;

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
	}
}
