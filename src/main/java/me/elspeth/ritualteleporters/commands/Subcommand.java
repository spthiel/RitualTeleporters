package me.elspeth.ritualteleporters.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.portals.Portal;
import me.elspeth.ritualteleporters.portals.PortalStore;
import me.elspeth.ritualteleporters.utils.BlockUtils;

public abstract class Subcommand implements CommandExecutor {

	private Permission permission;
	
	public Subcommand() {
		var permission = this.getPermissionString();
		if (permission != null) {
			this.permission = Bukkit.getPluginManager()
									.getPermission(this.getPermissionString());
		}
	}
	
	abstract public String getName();
	abstract public String getSyntax();
	abstract public String getDescription();
	
	public final @Nullable Permission getPermission() {
		return this.permission;
	}
	
	public final boolean hasPermission(Player player) {
		var permission = this.getPermission();
		
		if (permission == null) {
			return true;
		}
		
		return player.hasPermission(this.getPermission());
	}
	
	abstract protected String getPermissionString();
	
	protected Portal getPortalInLineOfSight(Player player) {
		
		var target = player.getTargetBlockExact(5);
		
		if (!BlockUtils.isItemTriggerablePlate(target)) {
			this.errorNotPortal(player);
			return null;
		}
		
		var portal = PortalStore.getInstance().getPortalOf(target);
		
		if (portal == null) {
			this.errorNotPortal(player);
			return null;
		}
		
		if (!portal.getOwner().equals(player.getUniqueId())) {
			this.errorNoPermissions(player);
			return null;
		}
		
		return portal;
	}
	
	protected void errorNotPortal(CommandSender sender) {
		sender.sendMessage("You are not looking at a portal");
	}
	
	protected void errorNoPermissions(CommandSender sender) {
		sender.sendMessage("You do not own this portal");
	}
}
