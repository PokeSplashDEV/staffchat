package org.pokesplash.staffchat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.staffchat.command.StaffChatCommand;
import org.pokesplash.staffchat.config.Config;

public class StaffChat implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("StaffChat");
	public static Config config = new Config();

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((a, b, c) -> {
			new StaffChatCommand().register(a);
		});
		config.init();
	}
}
