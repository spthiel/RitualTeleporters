package me.elspeth.ritualteleporters.commands.subcommands;


import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.teleporter.TeleporterStore;
import me.elspeth.ritualteleporters.utils.Messages;
import me.elspeth.ritualteleporters.utils.Permissions;

public class NameSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "name";
	}
	
	@Override
	public String getDescription() {
		
		return "Sets the name of a teleporter.";
	}
	
	@Override
	public Command.Builder<CommandSender> getCommand(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		
		return builder
			.senderType(Player.class)
			.argument(StringArgument.greedy("name"))
			.handler(context ->
						 manager.taskRecipe().begin(context)
								.synchronous(commandContext -> {
									this.run((Player)commandContext.getSender(), commandContext.get("name"));
								}).execute());
	}
	
	@Override
	public @Nullable Permissions getPermission() {
		
		return Permissions.CHANGE_NAME;
	}
	
	public void run(@NotNull Player player, String name) {
		
		var teleporter = this.getTeleporterInLineOfSight(player);
		
		if (teleporter == null) {
			return;
		}
		
		teleporter.setName(name);
		TeleporterStore.getInstance().saveToDisk();
		
		player.sendMessage(Messages.SUC_CHANGE_NAME.printf(name));
	}
}
