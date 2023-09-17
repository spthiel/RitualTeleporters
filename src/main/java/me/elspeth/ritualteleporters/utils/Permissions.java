package me.elspeth.ritualteleporters.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public enum Permissions {

	MANAGE_CHANGE("ritualteleporters.manage.change"),
	MANAGE_TELEPORT("ritualteleporters.manage.teleport"),
	CHANGE_PUBLIC("ritualteleporters.change.public"),
	CHANGE_MEMBERS("ritualteleporters.change.members"),
	CHANGE_DISPLAY("ritualteleporters.change.display"),
	CHANGE_NAME("ritualteleporters.change.name"),
	
	;
	
	private final Permission permission;
	
	Permissions(String permissionString) {
		
		this.permission = Bukkit.getPluginManager().getPermission(permissionString);
	}
	
	public Permission get() {
		
		return permission;
	}
	
	public boolean has(CommandSender sender) {
		if (sender.isOp()) {
			return true;
		}
		
		if (sender.hasPermission("*")) {
			return true;
		}
		
		return sender.hasPermission(get());
	}
}
