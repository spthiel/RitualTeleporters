package me.elspeth.ritualteleporters.commands.subcommands;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.RitualTeleporters;
import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.utils.Permissions;

public class HelpSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "help";
	}
	
	@Override
	public String getDescription() {
		
		return "Shows the help of specified command or a short list of all possible commands.";
	}
	
	@Override
	public Command.Builder<CommandSender> getCommand(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		
		return builder.argument(StringArgument.optional("query", StringArgument.StringMode.GREEDY))
					  .handler(context -> manager.taskRecipe()
												 .begin(context)
												 .synchronous(commandContext -> {
													 this.run(commandContext.getSender(), commandContext.getOrDefault("query", ""));
												 }).execute());
	}
	
	@Override
	public @Nullable Permissions getPermission() {
		
		return null;
	}
	
	public void run(@NotNull CommandSender sender, String query) {
		
		RitualTeleporters.plugin.getCloud()
								.getMinecraftHelp()
								.queryCommands(query, sender);
	}
	
}
