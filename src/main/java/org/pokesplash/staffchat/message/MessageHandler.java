package org.pokesplash.staffchat.message;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.staffchat.StaffChat;
import org.pokesplash.staffchat.permission.PermissionHandler;

import java.util.List;

public abstract class MessageHandler {
	public static void sendMessage(List<ServerPlayerEntity> players, String senderName, String message) {

		String newMessage = StaffChat.config.getMessage().replaceAll("\\{player\\}", senderName)
				.replaceAll("\\{message\\}", message);


		for (ServerPlayerEntity player : players) {
			if (PermissionHandler.hasPermission(player.getUuid(),
					PermissionHandler.VIEW_PERMISSION)) {
				player.sendMessage(Text.literal(newMessage));
			}
		}
	}
}
