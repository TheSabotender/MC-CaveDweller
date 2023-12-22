package darkcodex.cavedweller;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaveDweller implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("cave-dweller");

	//TODO move settings to config file, and add a config gui with Cloth Config
	public static boolean sunlightBurns = true;
	public static float sunlightBurn = 1;
	public static int sunlightBurnDuration = 1;
	public static boolean villagersFearPlayers = true;
	public static int villagerFearLevel = 0;
	public static int logFrameCount = 30;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric underworld!");
	}
}