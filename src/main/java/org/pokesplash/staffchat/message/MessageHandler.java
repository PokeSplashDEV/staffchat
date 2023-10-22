package org.pokesplash.staffchat.message;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.pokesplash.staffchat.StaffChat;
import org.pokesplash.staffchat.permission.PermissionHandler;

import java.util.List;

public abstract class MessageHandler {
	public static void sendMessage(List<ServerPlayerEntity> players, ServerPlayerEntity sender, String message) {

		String[] split = StaffChat.config.getMessage().split("\\{player\\}");

		if (split.length > 0) {
			MutableText newMessage = Text.literal(split[0]);

			for (int x=1; x < split.length; x++) {
				String newSub = split[x].replaceAll("\\{message\\}", message);
				newMessage.append(sender.getDisplayName());
				newMessage.append(newSub);
			}

			for (ServerPlayerEntity player : players) {
				if (PermissionHandler.hasPermission(player.getUuid(),
						PermissionHandler.VIEW_PERMISSION)) {
					player.sendMessage(newMessage);
					StaffChat.LOGGER.info(newMessage.getString());
				}
			}
		}
	}
}