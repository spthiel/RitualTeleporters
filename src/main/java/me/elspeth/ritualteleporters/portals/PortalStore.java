package me.elspeth.ritualteleporters.portals;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.elspeth.ritualteleporters.RitualTeleporter;
import me.elspeth.ritualteleporters.utils.Colors;
import me.elspeth.ritualteleporters.utils.SyntacticBukkitRunnable;

public class PortalStore {

	private static PortalStore instance;
	
	public static PortalStore getInstance() {
		return instance;
	}
	
	private final List<Portal> portals          = new ArrayList<>();
	private final File         portalConfigFile = new File(RitualTeleporter.plugin.getDataFolder(), "portals.yml");
	
	public PortalStore() {
	
		instance = this;
		
		final double speed = 0.15;
		
		var particleSpawner = new SyntacticBukkitRunnable(() -> {
			var phi = System.currentTimeMillis() * speed;
			var radPhi = Math.toRadians(phi);
			var dx = Math.cos(radPhi);
			var dz = Math.sin(radPhi);
			
			
			this.portals.forEach(portal -> portal.spawnParticle((float)dx, (float)dz));
		});
		
		particleSpawner.runTaskTimer(RitualTeleporter.plugin, 1L, 1L);
		
		this.loadFromDisk();
	}
	
	public Portal registerPortal(Location location, Colors color, Player owner) {
		var portal = new Portal(location, color, owner);
		portal.spawn();
		this.addPortal(portal);
		
		return portal;
	}
	
	public void addPortal(Portal portal) {
		portals.add(portal);
		saveToDisk();
	}
	
	public void removePortal(Portal portal) {
		portal.remove();
		portals.remove(portal);
		saveToDisk();
	}
	
	public @Nullable Portal getPortalOf(Block block) {
		return this.getPortalOf(block.getLocation());
	}
	
	public @Nullable Portal getPortalOf(Location location) {
		
		return this.portals.stream().filter(portal -> portal.getLocation().equals(location)).findFirst().orElse(null);
	}
	
	public void saveToDisk() {
		var config = YamlConfiguration.loadConfiguration(portalConfigFile);
		
		config.set("portals", this.portals);
		
		try {
			config.save(portalConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadFromDisk() {
		
		var config = YamlConfiguration.loadConfiguration(portalConfigFile);
		
		@SuppressWarnings("unchecked")
		List<Portal> portals = (List<Portal>)config.getList("portals");
		
		if (portals != null) {
			portals.forEach(this :: addPortal);
		}
		
	}
	
	public List<Portal> getPortals() {
		
		return this.portals;
	}
}
