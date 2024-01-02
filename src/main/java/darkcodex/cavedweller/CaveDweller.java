package darkcodex.cavedweller;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class CaveDweller implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("cave-dweller");

	//TODO move settings to config file, and add a config gui with Cloth Config
	public static String[] dwellers;
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
		
		LoadSettings();
		RegisterCommands();
		LOGGER.info("Hello Fabric underworld!");
	}

	void LoadSettings() {
		//Default settings
		dwellers = new String[0];
		sunlightBurns = true;
		sunlightBurn = 1;
		sunlightBurnDuration = 1;
		villagersFearPlayers = true;
		villagerFearLevel = 0;
		logFrameCount = 30;
	}

	void RegisterCommands(){
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-settings")
		.executes(context -> {
			context.getSource().sendFeedback(() -> Text.literal("Cave Dweller Settings"), false);
			context.getSource().sendFeedback(() -> Text.literal("sunlightBurns: " + CaveDweller.sunlightBurns), false);
			context.getSource().sendFeedback(() -> Text.literal("sunlightBurn: " + CaveDweller.sunlightBurn), false);
			context.getSource().sendFeedback(() -> Text.literal("sunlightBurnDuration: " + CaveDweller.sunlightBurnDuration), false);
			context.getSource().sendFeedback(() -> Text.literal("villagersFearPlayers: " + CaveDweller.villagersFearPlayers), false);
			context.getSource().sendFeedback(() -> Text.literal("villagerFearLevel: " + (context.getSource().getWorld().getSeaLevel() + CaveDweller.villagerFearLevel) + "(" + CaveDweller.villagerFearLevel  + ")"), false);
			context.getSource().sendFeedback(() -> Text.literal("logFrameCount: " + CaveDweller.logFrameCount), false);

			return 1;
		})));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-list")
		.executes(context -> {
			context.getSource().sendFeedback(() -> Text.literal("Cave Dwellers:"), false);
            for(int i = 0; i < CaveDweller.dwellers.length; i++) {
				final String dweller = CaveDweller.dwellers[i];
				context.getSource().sendFeedback(() -> Text.literal(dweller), false);
            }			
			return 1;
		})));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-add")
		.then(argument("value", StringArgumentType.word())
		.executes(context -> {			
			ServerPlayerEntity player = context.getSource().getPlayer();
			final String value = StringArgumentType.getString(context, "value");
              
			String[] newDwellers = new String[CaveDweller.dwellers.length + 1];
			for(int i = 0; i < CaveDweller.dwellers.length; i++) {
				newDwellers[i] = CaveDweller.dwellers[i];
			}
			newDwellers[CaveDweller.dwellers.length] = value;
			CaveDweller.dwellers = newDwellers;
			player.sendMessageToClient(Text.of("Added " + value + " to the dwellers list."), false);

			return 1;
		}))));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-remove")
		.then(argument("value", StringArgumentType.word())
		.executes(context -> {			
			ServerPlayerEntity player = context.getSource().getPlayer();
			final String value = StringArgumentType.getString(context, "value");
              
            String[] newDwellers = new String[CaveDweller.dwellers.length - 1];
			int j = 0;
			for(int i = 0; i < CaveDweller.dwellers.length; i++) {
				if(!CaveDweller.dwellers[i].equals(value)) {
					newDwellers[j] = CaveDweller.dwellers[i];
					j++;
				}
			}
			CaveDweller.dwellers = newDwellers;
			player.sendMessageToClient(Text.of("Removed " + value + " from the dwellers list."), false);

			return 1;
		}))));
	}
}