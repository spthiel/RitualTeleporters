package me.elspeth.ritualteleporters.commands.subcommands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.Subcommand;

public class NameSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "name";
	}
	
	@Override
	protected String getPermissionString() {
		return "ritualteleporter.change.name";
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		return false;
	}
}
