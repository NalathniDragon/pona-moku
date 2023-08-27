package io.github.nalathnidragon.pona_moku;

import io.github.nalathnidragon.pona_moku.config.PonaMokuConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PonaMoku implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("pona moku");
	public static final String MODID = "pona_moku";
	public static final PonaMokuConfig CONFIG = PonaMokuConfig.INSTANCE;

	@Override
	public void onInitialize(ModContainer mod) {
		List<String> ls = new ArrayList<>();
		ls.add("general");
		ls.add("foodHealingMultiplier");
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		LOGGER.info("Value: %2.2f".formatted(CONFIG.general.foodHealingMultiplier.value()));
		LOGGER.info("Value: %2.2f".formatted((double)CONFIG.getValue(ls).value()));
	}
}
