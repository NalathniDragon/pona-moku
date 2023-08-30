package io.github.nalathnidragon.pona_moku;

import io.github.nalathnidragon.pona_moku.config.FoodStatusConfig;
import io.github.nalathnidragon.pona_moku.config.PonaMokuConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PonaMoku implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pona moku");
	public static final String MODID = "pona_moku";
	public static final PonaMokuConfig config = PonaMokuConfig.instance;

	@Override
	public void onInitialize(ModContainer mod) {
		FoodStatusConfig.loadConfig();
	}
}
