package me.elspeth.ritualteleporters.commands.subcommands;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.data.ProtoItemStack;
import cloud.commandframework.bukkit.parsers.ItemStackArgument;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.teleporter.TeleporterStore;
import me.elspeth.ritualteleporters.utils.Messages;
import me.elspeth.ritualteleporters.utils.Permissions;

public class DisplaySubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "display";
	}
	
	@Override
	public String getDescription() {
		
		return "Sets the name of the teleporter that is being looked at.";
	}
	
	@Override
	public Command.Builder<CommandSender> getCommand(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		
		return builder
			.senderType(Player.class)
			.argument(ItemStackArgument.of("item"), ArgumentDescription.of("Item to be used. NBT will be ignored"))
			.handler(context ->
						 manager.taskRecipe().begin(context)
								.synchronous(commandContext -> {
									this.run((Player)commandContext.getSender(), commandContext.get("item"));
								}).execute());
	}
	
	@Override
	public @Nullable Permissions getPermission() {
		
		return Permissions.CHANGE_DISPLAY;
	}
	
	public void run(@NotNull Player player, ProtoItemStack stack) {
		
		var material = stack.material();
		
		var teleporter = this.getTeleporterInLineOfSight(player);
		
		if (teleporter == null) {
			return;
		}
		
		teleporter.setItem(material);
		TeleporterStore.getInstance().saveToDisk();
		
		player.sendMessage(Messages.SUC_CHANGE_DISPLAY.printf(material.name().toLowerCase()));
		
	}
	
}
