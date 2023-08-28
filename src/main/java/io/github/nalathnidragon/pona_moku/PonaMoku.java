package io.github.nalathnidragon.pona_moku;

import com.google.gson.*;
import io.github.nalathnidragon.pona_moku.config.FoodStatusConfig;
import io.github.nalathnidragon.pona_moku.config.PonaMokuConfig;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PonaMoku implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("pona moku");
	public static final String MODID = "pona_moku";
	public static final PonaMokuConfig config = PonaMokuConfig.instance;
	public static final FoodStatusConfig foodConfig = FoodStatusConfig.instance;

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		LOGGER.info(foodConfig.getFoodStatus()
			.get(Items.RABBIT_STEW)
			.get(StatusEffects.JUMP_BOOST)
			.toString());
	}
}
