package me.elspeth.ritualteleporters;

import java.util.logging.Logger;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import me.elspeth.ritualteleporters.commands.subcommands.DisplaySubcommand;
import me.elspeth.ritualteleporters.commands.subcommands.MembersSubcommand;
import me.elspeth.ritualteleporters.commands.subcommands.NameSubcommand;
import me.elspeth.ritualteleporters.commands.TeleporterCommand;
import me.elspeth.ritualteleporters.events.TeleporterListener;
import me.elspeth.ritualteleporters.inventory.InventoryListener;
import me.elspeth.ritualteleporters.portals.Portal;

public final class RitualTeleporter extends JavaPlugin {

    public static RitualTeleporter plugin;
    
    private Logger logger = PluginLogger.getLogger(this.getName());
    
    public RitualTeleporter() {
        plugin = this;
    }
    
    @Override
    public void onEnable() {
        
        ConfigurationSerialization.registerClass(Portal.class);
        
        logger.info("Enabled plugin");
        getServer().getPluginManager().registerEvents(new TeleporterListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        
        var command = new TeleporterCommand();
        command.addSubcommand(new DisplaySubcommand());
        command.addSubcommand(new MembersSubcommand());
        command.addSubcommand(new NameSubcommand());
        getCommand("teleporter").setExecutor(command);
    }

    @Override
    public void onDisable() {
        logger.info("Disabled plugin");
    }
}
