package me.elspeth.ritualteleporters.commands;


import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.permission.PredicatePermission;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.teleporter.Teleporter;
import me.elspeth.ritualteleporters.teleporter.TeleporterStore;
import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Messages;
import me.elspeth.ritualteleporters.utils.Permissions;

public abstract class Subcommand implements PredicatePermission<CommandSender> {

	abstract public String getName();
	
	abstract public String getDescription();
	
	public Command.Builder<CommandSender> getCommand(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		return builder;
	}
	
	public List<Command.Builder<CommandSender>> getCommands(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		var out = new ArrayList<Command.Builder<CommandSender>>();
		out.add(this.getCommand(manager, builder).meta(CommandMeta.DESCRIPTION, getDescription()));
		return out;
	}
	
	abstract public @Nullable Permissions getPermission();
	
	@Override
	public final boolean hasPermission(CommandSender sender) {
		var permission = this.getPermission();
		
		if (permission == null) {
			return true;
		}
		
		return permission.has(sender);
	}
	
	protected Teleporter getTeleporterInLineOfSight(Player player) {
		
		var target = player.getTargetBlockExact(5);
		
		if (!BlockUtils.isItemTriggerablePlate(target)) {
			player.sendMessage(Messages.ERR_NOT_A_TELEPORTER.printf());
			return null;
		}
		
		var teleporter = TeleporterStore.getInstance().getTeleporterOf(target);
		
		if (teleporter == null) {
			player.sendMessage(Messages.ERR_NOT_A_TELEPORTER.printf());
			return null;
		}
		
		if (!teleporter.getOwner().equals(player.getUniqueId())) {
			if (!Permissions.MANAGE_CHANGE.has(player)) {
				
				player.sendMessage(Messages.ERR_NOT_OWN_TELEPORTER.printf());
				return null;
			}
		}
		
		return teleporter;
	}
	
}
