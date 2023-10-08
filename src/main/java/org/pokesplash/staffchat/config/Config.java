package org.pokesplash.staffchat.config;

import com.google.gson.Gson;
import org.pokesplash.staffchat.StaffChat;
import org.pokesplash.staffchat.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Config {
	private String message;
	private boolean useDiscord;
	private String discordUrl;

	public Config() {
		message = "<dark_aqua>[StaffChat] {player}: {message}";
		useDiscord = false;
		discordUrl = "";
	}

	public String getMessage() {
		return message;
	}

	public boolean isUseDiscord() {
		return useDiscord;
	}

	public String getDiscordUrl() {
		return discordUrl;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead =
				Utils.readFileAsync("/config/staffchat/", "config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					message = cfg.getMessage();
					discordUrl = cfg.getDiscordUrl();
					useDiscord = cfg.isUseDiscord();
				});

		if (!futureRead.join()) {
			StaffChat.LOGGER.info("No config found for StaffChat, attempting to generate one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync("/config/staffchat/",
					"config.json", data);

			if (!futureWrite.join()) {
				StaffChat.LOGGER.fatal("Could not write file for StaffChat");
			}
			return;
		}
		StaffChat.LOGGER.info("StaffChat config read successfully.");
	}
}
