package me.elspeth.ritualteleporters.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public enum Messages {

	ERR_NOT_OWN_TELEPORTER("You don't own this teleporter.", RED),
	ERR_NOT_A_TELEPORTER("You are not looking at a teleporter.", RED),
	ERR_YOU_ARE_OWNER("You are the owner of this teleporter.", RED),
	ERR_PLAYER_IS_OWNER("%s is the owner of this teleporter.", RED),
	ERR_ALREADY_MEMBER("%s is already a member of this teleporter.", RED),
	ERR_NOT_MEMBER("%s is not a member of this teleporter.", RED),
	
	MSG_MEMBERS("Current members of teleporter %s:\n - %s"),
	
	SUC_CHANGE_DISPLAY("Successfully changed display of teleporter to %s.", GREEN),
	SUC_ADDRM_MEMBER("Successfully %s member %s to teleporter %s.", GREEN),
	SUC_CHANGE_NAME("Successfully changed name of teleporter to %s.", GREEN),
	SUC_CHANGE_PUBLIC("Successfully set teleporter %s %s", GREEN),
	
	;
	
	private final String    message;
	private final TextColor color;
	
	@SuppressWarnings("SameParameterValue")
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
