package me.elspeth.ritualteleporters.teleporter;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.elspeth.ritualteleporters.RitualTeleporters;
import me.elspeth.ritualteleporters.utils.Colors;
import me.elspeth.ritualteleporters.utils.SyntacticBukkitRunnable;

public class TeleporterStore {
	
	private static TeleporterStore instance;
	
	public static TeleporterStore getInstance() {
		
		return instance;
	}
	
	private final List<Teleporter> teleporters          = new ArrayList<>();
	private final File             teleporterConfigFile = new File(RitualTeleporters.plugin.getDataFolder(), "teleporters.yml");
	
	public TeleporterStore() {
		
		instance = this;
		
		final double speed = 0.15;
		
		var particleSpawner = new SyntacticBukkitRunnable(() -> {
			var phi    = System.currentTimeMillis() * speed;
			var radPhi = Math.toRadians(phi);
			var dx     = Math.cos(radPhi);
			var dz     = Math.sin(radPhi);
			
			
			this.teleporters.forEach(teleporter -> teleporter.spawnParticle((float) dx, (float) dz));
		});
		
		particleSpawner.runTaskTimer(RitualTeleporters.plugin, 1L, 1L);
		
		this.loadFromDisk();
	}
	
	public void registerTeleporter(Location location, Colors color, Player owner) {
		
		var teleporter = new Teleporter(location, color, owner);
		teleporter.spawn();
		this.addTeleporter(teleporter);
	}
	
	public void addTeleporter(Teleporter teleporter) {
		
		teleporters.add(teleporter);
		saveToDisk();
	}
	
	public void removeTeleporter(Teleporter teleporter) {
		
		teleporter.remove();
		teleporters.remove(teleporter);
		saveToDisk();
	}
	
	public @Nullable Teleporter getTeleporterOf(Block block) {
		
		return this.getTeleporterOf(block.getLocation());
	}
	
	public @Nullable Teleporter getTeleporterOf(Location location) {
		
		return this.teleporters.stream()
							   .filter(teleporter -> teleporter.getLocation()
															   .equals(location))
							   .findFirst()
							   .orElse(null);
	}
	
	public void saveToDisk() {
		
		var config = YamlConfiguration.loadConfiguration(teleporterConfigFile);
		
		config.set("teleporters", this.teleporters);
		
		try {
			config.save(teleporterConfigFile);
		} catch (IOException e) {
			RitualTeleporters.plugin.getLogger().throwing(TeleporterStore.class.getName(), "saveToDisk", e);
		}
	}
	
	public void loadFromDisk() {
		
		var config = YamlConfiguration.loadConfiguration(teleporterConfigFile);
		
		@SuppressWarnings("unchecked")
		List<Teleporter> teleporters = (List<Teleporter>) config.getList("teleporters");
		
		if (teleporters != null) {
			teleporters.forEach(this :: addTeleporter);
		}
		
	}
	
	public List<Teleporter> getTeleporters() {
		
		return this.teleporters;
	}
}
