package me.elspeth.ritualteleporters.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.Subcommand;
import me.elspeth.ritualteleporters.portals.PortalStore;
import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Messages;

public class DisplaySubcommand extends Subcommand {
	
	@Override
	public String getName() {
		
		return "display";
	}
	
	@Override
	public String getSyntax() {
		
		return "<name>";
	}
	
	@Override
	public String getDescription() {
		
		return "Sets the name of the portal that is being looked at.";
	}
	
	@Override
	protected String getPermissionString() {
		return "ritualteleporter.change.display";
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
		
		var material = Material.matchMaterial(args[1]);
		
		if (material == null) {
			sender.sendMessage(Messages.ERR_INVALID_ID.printf(args[1]));
			return true;
		}
		
		portal.setItem(material);
		PortalStore.getInstance().saveToDisk();
		
		sender.sendMessage(Messages.SUC_CHANGE_DISPLAY.printf(material.name().toLowerCase()));
		
		return true;
	}
	
}
