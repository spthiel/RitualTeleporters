package me.elspeth.ritualteleporters.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public enum Messages {

	ERR_NOT_OWN_PORTAL("You don't own this portal.", RED),
	ERR_NOT_A_PLAYER("Only a player can use this command.", RED),
	ERR_INVALID_ID("%s is an invalid item id.", RED),
	ERR_INVALID_PLAYER("%s is an invalid player name.", RED),
	
	MSG_MEMBERS("Current members of portal %s:\n - %s"),
	
	SUC_CHANGE_DISPLAY("Successfully changed display of portal to %s.", GREEN),
	SUC_ADDRM_MEMBER("Successfully %s member %s to portal %s.", GREEN),
	SUC_CHANGE_NAME("Successfully changed name of portal to %s.", GREEN),
	
	;
	
	private final String    message;
	private final TextColor color;
	
	Messages(String message) {
		this(message, WHITE);
	}
	
	Messages(String message, TextColor color) {
		this.message = message;
		this.color = color;
	}
	
	public TextComponent printf(Object ...data) {
		return Component.text(String.format(this.message, data), color);
	}
	
}
