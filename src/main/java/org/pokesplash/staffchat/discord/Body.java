package org.pokesplash.staffchat.discord;

public class Body {
	private String username;
	private String avatar_url;
	private String content;

	public Body(String username, String avatar_url, String content) {
		this.username = username;
		this.avatar_url = avatar_url;
		this.content = content;
	}
}
