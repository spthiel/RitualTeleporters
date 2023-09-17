package me.elspeth.ritualteleporters.inventory;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.elspeth.ritualteleporters.teleporter.Teleporter;

public class LocationSelect {
	
	private final Player               player;
	private final List<Teleporter>     teleporters;
	private       Consumer<Teleporter> onSuccess;
	
	public LocationSelect(Player player, List<Teleporter> teleporters) {
		
		this.player = player;
		this.teleporters = teleporters;
	}
	
	public void then(Consumer<Teleporter> onSuccess) {
		
		this.onSuccess = onSuccess;
	}
	
	public void openWorldSelect() {
		
		var worlds = teleporters.stream()
								.map(Teleporter :: getLocation)
								.map(Location :: getWorld)
								.distinct()
								.map(World :: getName)
								.toList();
		
		var selector = new Selector(player);
		
		selector.then((world) -> {
			var bukkitWorld = Bukkit.getWorld(world);
			selector.close();
			openLocationSelect(bukkitWorld);
		});
		
		worlds.stream()
			  .map(world -> new Option().name(world)
										.value(world))
			  .forEach(selector :: addOption);
		
		selector.open();
	}
	
	public void openLocationSelect(World world) {
		
		var worldTeleporters = teleporters.stream()
									  .filter(teleporter -> teleporter.getWorld()
														  .equals(world)
								  )
									  .map(teleporter -> new Option().value(String.valueOf(teleporter.getId()))
															 .name(teleporter.getName())
															 .material(teleporter.getItem())
															 .description(String.format("[ %d, %d, %d]\n\nOwner: %s", teleporter.getLocation().getBlockX(), teleporter.getLocation().getBlockY(), teleporter.getLocation().getBlockZ(), Bukkit.getOfflinePlayer(teleporter.getOwner()).getName()))
								  );
		
		var selector = new Selector(player);
		selector.then((id) -> {
			
			selector.close();
			
			var selectedTeleporter = teleporters.stream()
											.filter(teleporter -> String.valueOf(teleporter.getId())
																.equals(id))
											.findFirst()
											.orElse(null);
			
			if (selectedTeleporter == null) {
				player.sendMessage("Something went wrong trying to teleport, the selected teleporter was not found.");
				return;
			}
			
			this.onSuccess.accept(selectedTeleporter);
			
		}, this :: openWorldSelect);
		
		worldTeleporters.forEach(selector :: addOption);
		
		selector.open();
		
	}
	
}
