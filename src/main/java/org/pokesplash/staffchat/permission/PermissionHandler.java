package org.pokesplash.staffchat.permission;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;

public abstract class PermissionHandler {
	public static final String VIEW_PERMISSION = "staffchat.view";
	public static final String SEND_PERMISSION = "staffchat.send";
	public static final String RELOAD_PERMISSION = "staffchat.reload";

	public static boolean hasPermission(UUID player, String permission) {
		User lpPlayer = LuckPermsProvider.get().getUserManager().getUser(player);

		if (lpPlayer == null) {
			return false;
		}

		return lpPlayer.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
	}

}
