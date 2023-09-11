package me.elspeth.ritualteleporters.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.Subcommand;

public class HelpSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "help";
	}
	
	@Override
	protected String getPermissionString() {
		return null;
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		return false;
	}
}
