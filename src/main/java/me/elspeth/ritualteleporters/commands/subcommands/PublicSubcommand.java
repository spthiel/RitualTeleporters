package me.elspeth.ritualteleporters.commands.subcommands;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.teleporter.TeleporterStore;
import me.elspeth.ritualteleporters.utils.Messages;
import me.elspeth.ritualteleporters.utils.Permissions;

public class PublicSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "public";
	}
	
	@Override
	public String getDescription() {
		
		return "Toggles whether this teleporter can be access by everyone.";
	}
	
	@Override
	public Command.Builder<CommandSender> getCommand(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		
		return builder
			.senderType(Player.class)
			.argument(BooleanArgument.optional("value"))
			.handler(context -> manager.taskRecipe().begin(context)
								   .synchronous(commandContext -> {
					   this.run((Player)commandContext.getSender(), commandContext.getOptional("value"));
				   }).execute())
			;
	}
	
	@Override
	public @Nullable Permissions getPermission() {
		
		return Permissions.CHANGE_PUBLIC;
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public void run(Player player, Optional<Boolean> value) {
		
		var teleporter = this.getTeleporterInLineOfSight(player);
		
		if (teleporter == null) {
			return;
		}
		
		boolean setPublic = value.orElse(!teleporter.isPublicTeleporter());
		
		teleporter.setPublicTeleporter(setPublic);
		TeleporterStore.getInstance()
					   .saveToDisk();
		
		player.sendMessage(Messages.SUC_CHANGE_PUBLIC.printf(teleporter.getName(), setPublic ? "public" : "private"));
		
	}
	
}
