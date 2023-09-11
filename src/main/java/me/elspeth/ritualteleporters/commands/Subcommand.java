package me.elspeth.ritualteleporters.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

public abstract class Subcommand implements CommandExecutor {

	private Permission permission;
	
	public Subcommand() {
		var permission = this.getPermissionString();
		if (permission != null) {
			this.permission = Bukkit.getPluginManager()
									.getPermission(this.getPermissionString());
		}
	}
	
	abstract public String getName();
	
	public final @Nullable Permission getPermission() {
		return this.permission;
	}
	
	public final boolean hasPermission(Player player) {
		var permission = this.getPermission();
		
		if (permission == null) {
			return true;
		}
		
		return player.hasPermission(this.getPermission());
	}
	
	abstract protected String getPermissionString();
	
}
