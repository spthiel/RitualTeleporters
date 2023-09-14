package me.elspeth.ritualteleporters.commands.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.portals.PortalStore;
import me.elspeth.ritualteleporters.utils.Messages;

public class MembersSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "members";
	}
	
	@Override
	public String getSyntax() {
		
		return "<add/remove/list> [player]";
	}
	
	@Override
	public String getDescription() {
		
		return "Adds or removes permissions for players to use the portal or shows a list of players who can use this portal.";
	}
	
	@Override
	protected String getPermissionString() {
		return "ritualteleporter.change.members";
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		if(!(sender instanceof Player player)) {
			sender.sendMessage(Messages.ERR_NOT_A_PLAYER.printf());
			return false;
		}
		
		if (args.length < 2) {
			return false;
		}
		
		var portal = this.getPortalInLineOfSight(player);
		
		if (portal == null) {
			return true;
		}
		
		if (args[1].equalsIgnoreCase("list")) {
			var members = portal.getMembers().stream()
								.map(Bukkit :: getOfflinePlayer)
								.map(OfflinePlayer ::getName)
								.collect(Collectors.joining(", "));
			sender.sendMessage(Messages.MSG_MEMBERS.printf(portal.getName(), members));
			return true;
		}
		
		var member = Bukkit.getOfflinePlayerIfCached(args[2]);
		
		if (member == null) {
			sender.sendMessage(Messages.ERR_INVALID_PLAYER.printf(args[2]));
			return true;
		}
		
		String verb;
		
		if (args[1].equalsIgnoreCase("add")) {
			portal.addMember(member);
			verb = "added";
		} else if (args[1].equalsIgnoreCase("remove")) {
			portal.addMember(member);
			verb = "removed";
		} else {
			return false;
		}
		PortalStore.getInstance().saveToDisk();
		
		sender.sendMessage(Messages.SUC_ADDRM_MEMBER.printf(verb, member.getName(), portal.getName()));
		
		return true;
	}
}
