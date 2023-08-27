package io.github.nalathnidragon.pona_moku.config;

import io.github.nalathnidragon.pona_moku.PonaMoku;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.loader.api.config.v2.QuiltConfig;

public final class PonaMokuConfig extends ReflectiveConfig {
	public static final PonaMokuConfig INSTANCE =
		QuiltConfig.create(
			PonaMoku.MODID, // what the folder is called ".minecraft/config/pona_moku/" in this case
			"config", // what the file is called. "config.toml" in this case
			PonaMokuConfig.class);

	@Comment("A general section. For things. In general.")
	public final GeneralConfig general = new GeneralConfig();

	public static class GeneralConfig extends Section {
		@Comment("What number should the food level of food be multiplied by when contributing to healing." +
			"\nHealing formula is (foodHealingMultiplier * foodLevel) + (saturationHealingMultiplier * saturation)" +
			" = half_hearts_healed")
		@FloatRange(min = -10.0, max = 10.0)
		public final TrackedValue<Double> foodHealingMultiplier = value(1.0);

		@Comment("What number should the saturation level of food be multiplied by when contributing to healing." +
			"\nHealing formula is (foodHealingMultiplier * foodLevel) + (saturationHealingMultiplier * saturation)" +
			" = half_hearts_healed")
		@FloatRange(min = -10.0, max = 10.0)
		public final TrackedValue<Double> saturationHealingMultiplier = value(1.0);
	}
}
