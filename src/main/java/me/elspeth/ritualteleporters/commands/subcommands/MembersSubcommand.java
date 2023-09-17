package me.elspeth.ritualteleporters.commands.subcommands;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument;
import cloud.commandframework.meta.CommandMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.teleporter.TeleporterStore;
import me.elspeth.ritualteleporters.utils.Messages;
import me.elspeth.ritualteleporters.utils.Permissions;

public class MembersSubcommand extends Subcommand {
	
	private enum Verbs {
		add(),
		remove()
	}
	
	@Override
	public String getName() {
		
		return "members";
	}
	
	@Override
	public String getDescription() {
		
		return null;
	}
	
	@Override
	public List<Command.Builder<CommandSender>> getCommands(BukkitCommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
		
		var out = new ArrayList<Command.Builder<CommandSender>>();
		
		builder = builder.senderType(Player.class);
		
		var verb = EnumArgument.<CommandSender, Verbs> builder(Verbs.class, "verb")
							   .withDefaultDescription(() -> "add/remove")
							   .build();
		
		out.add(builder.argument(verb)
					   .meta(CommandMeta.DESCRIPTION, "Adds or removes a member from a teleporter.")
					   .argument(OfflinePlayerArgument.of("player"))
					   .handler(context -> manager.taskRecipe()
												  .begin(context)
												  .synchronous(commandContext -> {
													  Verbs type = commandContext.get("verb");
													  if (type.equals(Verbs.add)) {
														  this.add((Player) commandContext.getSender(), commandContext.get("player"));
													  } else {
														  this.remove((Player) commandContext.getSender(), commandContext.get("player"));
													  }
												  })
												  .execute()));
		
		out.add(builder.literal("list")
					   .meta(CommandMeta.DESCRIPTION, "Lists all members from a teleporter.")
					   .handler(context -> manager.taskRecipe()
												  .begin(context)
												  .synchronous(commandContext -> {
													  this.list((Player) commandContext.getSender());
												  })
												  .execute()));
		
		
		return out;
	}
	
	@Override
	public @Nullable Permissions getPermission() {
		
		return Permissions.CHANGE_MEMBERS;
	}
	
	private void add(Player player, OfflinePlayer member) {
		
		var teleporter = this.getTeleporterInLineOfSight(player);
		
		if (teleporter == null) {
			return;
		}
		
		var memberUUID = member.getUniqueId();
		
		if (teleporter.getOwner()
					  .equals(memberUUID)) {
			if (member.getUniqueId()
					  .equals(memberUUID)) {
				player.sendMessage(Messages.ERR_YOU_ARE_OWNER.printf());
			} else {
				player.sendMessage(Messages.ERR_PLAYER_IS_OWNER.printf(member.getName()));
			}
			return;
		}
		
		if (teleporter.getMembers()
					  .contains(memberUUID)) {
			player.sendMessage(Messages.ERR_ALREADY_MEMBER.printf(member.getName()));
			return;
		}
		
		teleporter.addMember(member);
		
		TeleporterStore.getInstance()
					   .saveToDisk();
		
		player.sendMessage(Messages.SUC_ADDRM_MEMBER.printf("added", member.getName(), teleporter.getName()));
	}
	
	private void remove(Player player, OfflinePlayer member) {
		
		var teleporter = this.getTeleporterInLineOfSight(player);
		
		if (teleporter == null) {
			return;
		}
		
		var memberUUID = member.getUniqueId();
		
		if (!teleporter.getMembers()
					   .contains(memberUUID)) {
			player.sendMessage(Messages.ERR_NOT_MEMBER.printf(member.getName()));
			return;
		}
		
		teleporter.removeMember(member);
		
		TeleporterStore.getInstance()
					   .saveToDisk();
		
		player.sendMessage(Messages.SUC_ADDRM_MEMBER.printf("removed", member.getName(), teleporter.getName()));
	}
	
	private void list(Player player) {
		
		var teleporter = this.getTeleporterInLineOfSight(player);
		
		if (teleporter == null) {
			return;
		}
		
		var members = teleporter.getMembers()
								.stream()
								.map(Bukkit :: getOfflinePlayer)
								.map(OfflinePlayer :: getName)
								.collect(Collectors.joining(", "));
		player.sendMessage(Messages.MSG_MEMBERS.printf(teleporter.getName(), members));
	}
	
}
