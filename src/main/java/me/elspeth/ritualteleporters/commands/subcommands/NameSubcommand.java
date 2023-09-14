package me.elspeth.ritualteleporters.commands.subcommands;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.portals.PortalStore;
import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Messages;

public class NameSubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "name";
	}
	
	@Override
	public String getSyntax() {
		
		return "<name>";
	}
	
	@Override
	public String getDescription() {
		
		return "Sets the name of the portal you are looking at";
	}
	
	@Override
	protected String getPermissionString() {
		return "ritualteleporter.change.name";
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
			this.errorNotPortal(player);
			return true;
		}
		
		portal.setName(args[1]);
		PortalStore.getInstance().saveToDisk();
		
		sender.sendMessage(Messages.SUC_CHANGE_NAME.printf(args[1]));
		
		return true;
	}
}
