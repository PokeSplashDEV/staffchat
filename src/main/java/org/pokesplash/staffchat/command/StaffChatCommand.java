package org.pokesplash.staffchat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.staffchat.StaffChat;
import org.pokesplash.staffchat.discord.Webhook;
import org.pokesplash.staffchat.message.MessageHandler;
import org.pokesplash.staffchat.permission.PermissionHandler;

public class StaffChatCommand {
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
				.literal("staffchat")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return PermissionHandler.hasPermission(ctx.getPlayer().getUuid(),
								PermissionHandler.SEND_PERMISSION);
					} else {
						return true;
					}
				})
				.executes(this::usage)
				.then(CommandManager.argument("args", StringArgumentType.greedyString())
						.executes(this::run))
				.then(CommandManager.literal("reload")
						.requires(ctx -> {
							if (ctx.isExecutedByPlayer()) {
								return PermissionHandler.hasPermission(ctx.getPlayer().getUuid(),
										PermissionHandler.RELOAD_PERMISSION);
							} else {
								return true;
							}
						})
						.executes(this::reload));

		LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

		dispatcher.register(CommandManager.literal("sc").redirect(registeredCommand).executes(this::run));
	}

	public int usage(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.literal("Usage: /sc <message>"));
		return 1;
	}

	public int run(CommandContext<ServerCommandSource> context) {

		ServerPlayerEntity senderName;

		String message = StringArgumentType.getString(context, "args");

		if (context.getSource().isExecutedByPlayer()) {
			if (!PermissionHandler.hasPermission(context.getSource().getPlayer().getUuid(),
					PermissionHandler.SEND_PERMISSION)) {
				return 1;
			}
			senderName = context.getSource().getPlayer();
		} else {
			senderName = null;
		}

		MessageHandler.sendMessage(context.getSource().getServer().getPlayerManager().getPlayerList(),
				senderName, message);

		if (StaffChat.config.isUseDiscord() && context.getSource().isExecutedByPlayer()) {
			Webhook.sendMessage(context.getSource().getPlayer(), message);
		}

		return 1;
	}

	public int reload(CommandContext<ServerCommandSource> context) {
		StaffChat.config.init();

		context.getSource().sendMessage(Text.literal("StaffChat reloaded."));
		return 1;
	}

}
