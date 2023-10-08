package org.pokesplash.staffchat.discord;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.staffchat.StaffChat;
import org.pokesplash.staffchat.util.Utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class Webhook {
	public static void sendMessage(ServerPlayerEntity player, String message) {
		JsonObject body = new JsonObject();
		body.addProperty("username", player.getName().getString());
		body.addProperty("avatar_url", "https://mc-heads.net/avatar/" + player.getUuid());
		body.addProperty("content", message);

		try {
			Gson gson = Utils.newGson();
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(StaffChat.config.getDiscordUrl()))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Body(
							player.getName().getString(),
							"https://mc-heads.net/avatar/" + player.getUuid(),
							message
					))))
					.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
