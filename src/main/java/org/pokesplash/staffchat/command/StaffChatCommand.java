package org.pokesplash.staffchat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.staffchat.StaffChat;
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

		String senderName;

		if (context.getSource().isExecutedByPlayer()) {
			senderName = context.getSource().getPlayer().getName().getString();
		} else {
			senderName = "Server";
		}

		String message = StringArgumentType.getString(context, "args");

		MessageHandler.sendMessage(context.getSource().getServer().getPlayerManager().getPlayerList(),
				senderName, message);
		return 1;
	}

	public int reload(CommandContext<ServerCommandSource> context) {
		StaffChat.config.init();

		context.getSource().sendMessage(Text.literal("StaffChat reloaded."));
		return 1;
	}

}
