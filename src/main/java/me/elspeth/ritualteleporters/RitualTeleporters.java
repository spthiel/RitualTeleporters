package me.elspeth.ritualteleporters;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import me.elspeth.ritualteleporters.commands.TeleporterCommand;
import me.elspeth.ritualteleporters.commands.subcommands.*;
import me.elspeth.ritualteleporters.events.TeleporterListener;
import me.elspeth.ritualteleporters.inventory.InventoryListener;
import me.elspeth.ritualteleporters.teleporter.Teleporter;

public final class RitualTeleporters extends JavaPlugin {
	
	public static RitualTeleporters      plugin;
	private final RitualTeleportersCloud cloud;
	
	@SuppressWarnings("unused")
	public RitualTeleporters() {
		
		plugin = this;
		cloud = new RitualTeleportersCloud(this);
	}
	
	@Override
	public void onEnable() {
		
		ConfigurationSerialization.registerClass(Teleporter.class);
		
		this.getLogger()
			.info("Enabled plugin");
		getServer().getPluginManager()
				   .registerEvents(new TeleporterListener(), this);
		getServer().getPluginManager()
				   .registerEvents(new InventoryListener(), this);
		
		var command = new TeleporterCommand();
		command.addSubcommand(new DisplaySubcommand());
		command.addSubcommand(new MembersSubcommand());
		command.addSubcommand(new NameSubcommand());
		command.addSubcommand(new PublicSubcommand());
		command.addSubcommand(new HelpSubcommand());
		
		cloud.setup();
		
		var manager = cloud.getManager();
		@SuppressWarnings("SpellCheckingInspection")
		var builder = manager.commandBuilder("teleporter", "ritp", "tptr", "prtr");
		
		var commands = command.getCommands(manager, builder);
		
		for (var cloudCommand : commands) {
			cloud.getManager()
				 .command(cloudCommand);
		}
		
	}
	
	public RitualTeleportersCloud getCloud() {
		
		return cloud;
	}
	
	@Override
	public void onDisable() {
		
		this.getLogger()
			.info("Disabled plugin");
	}
}
