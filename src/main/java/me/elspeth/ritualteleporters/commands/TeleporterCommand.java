package me.elspeth.ritualteleporters.commands;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class TeleporterCommand {
	
	private final List<Subcommand> subcommands = new ArrayList<>();
	
	public void addSubcommand(Subcommand subcommand) {
		
		this.subcommands.add(subcommand);
	}
	
	public List<Command.Builder<CommandSender>> getCommands(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		
		var out = new ArrayList<Command.Builder<CommandSender>>();
		
		for (var subcommand : this.subcommands) {
			var subcommandBuilder = builder.literal(subcommand.getName())
										   .permission(subcommand);
			
			out.addAll(subcommand.getCommands(manager, subcommandBuilder));
		}
		
		return out;
	}
	
}
