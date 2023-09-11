package me.elspeth.ritualteleporters.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.Subcommand;

public class DisplaySubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "display";
	}
	
	@Override
	protected String getPermissionString() {
		return "ritualteleporter.change.display";
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		return false;
	}
}
