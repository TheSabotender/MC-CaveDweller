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

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		//LoadSettings();
		RegisterCommands();
		Config.getInstance(); // Load config file
		LOGGER.info("Hello Fabric underworld!");
	}

	void RegisterCommands(){
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-settings")
		.executes(context -> {
			context.getSource().sendFeedback(() -> Text.literal("Cave Dweller Settings"), false);
			context.getSource().sendFeedback(() -> Text.literal("sunlightBurns: " + Config.getSunlightBurns()), false);
			context.getSource().sendFeedback(() -> Text.literal("sunlightBurn: " + Config.getSunlightBurn()), false);
			context.getSource().sendFeedback(() -> Text.literal("sunlightBurnDuration: " + Config.getSunlightBurnDuration()), false);
			context.getSource().sendFeedback(() -> Text.literal("villagersFearPlayers: " + Config.getVillagersFearPlayers()), false);
			context.getSource().sendFeedback(() -> Text.literal("villagerFearLevel: " + (context.getSource().getWorld().getSeaLevel() + Config.getVillagerFearLevel()) + "(" + Config.getVillagerFearLevel()  + ")"), false);
			context.getSource().sendFeedback(() -> Text.literal("logFrameCount: " + Config.getLogFrameCount()), false);

			return 1;
		})));
	

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-list")
		.executes(context -> {
			context.getSource().sendFeedback(() -> Text.literal("Cave Dwellers:"), false);
            for(int i = 0; i < Config.getDwellers().length; i++) {
				final String dweller = Config.getDwellers()[i];
				context.getSource().sendFeedback(() -> Text.literal(dweller), false);
            }			
			return 1;
		})));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-add")
		.then(argument("value", StringArgumentType.word())
		.executes(context -> {			
			ServerPlayerEntity player = context.getSource().getPlayer();
			final String value = StringArgumentType.getString(context, "value");
              
			String[] newDwellers = new String[Config.getDwellers().length + 1];
			for(int i = 0; i < Config.getDwellers().length; i++) {
				newDwellers[i] = Config.getDwellers()[i];
			}
			newDwellers[Config.getDwellers().length] = value;
			Config.setDwellers(newDwellers);
			player.sendMessageToClient(Text.of("Added " + value + " to the dwellers list."), false);

			return 1;
		}))));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cd-remove")
		.then(argument("value", StringArgumentType.word())
		.executes(context -> {			
			ServerPlayerEntity player = context.getSource().getPlayer();
			final String value = StringArgumentType.getString(context, "value");
              
            String[] newDwellers = new String[Config.getDwellers().length - 1];
			int j = 0;
			for(int i = 0; i < Config.getDwellers().length; i++) {
				if(!Config.getDwellers()[i].equals(value)) {
					newDwellers[j] = Config.getDwellers()[i];
					j++;
				}
			}
			Config.setDwellers(newDwellers);
			player.sendMessageToClient(Text.of("Removed " + value + " from the dwellers list."), false);

			return 1;
		}))));

	}
}