package me.elspeth.ritualteleporters.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.elspeth.ritualteleporters.RitualTeleporter;
import me.elspeth.ritualteleporters.portals.Portal;
import me.elspeth.ritualteleporters.utils.SyntacticBukkitRunnable;

public class LocationSelect {
	
	private static List<LocationSelect> selects = new ArrayList<>();
	
	private final Player           player;
	private final List<Portal>     portals;
	private       Consumer<Portal> onSuccess;
	
	LocationSelect() {
		
		this.player = null;
		this.portals = null;
	}
	
	public LocationSelect(Player player, List<Portal> portals) {
		
		this.player = player;
		this.portals = portals;
		selects.add(this);
	}
	
	public void then(Consumer<Portal> onSuccess) {
		
		this.onSuccess = onSuccess;
	}
	
	public void openWorldSelect() {
		
		var worlds = portals.stream()
							.map(Portal :: getLocation)
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
		
		var worldPortals = portals.stream()
								  .filter(portal -> portal.getWorld()
														  .equals(world)
								  )
								  .map(portal -> new Option().value(String.valueOf(portal.getId()))
															 .name(portal.getName())
															 .material(portal.getItem())
															 .description(String.format("[ %d, %d, %d]", portal.getLocation().getBlockX(), portal.getLocation().getBlockY(), portal.getLocation().getBlockZ()))
								  );
		
		var selector = new Selector(player);
		selector.then((id) -> {
			
			selector.close();
			
			var selectedPortal = portals.stream()
										.filter(portal -> String.valueOf(portal.getId())
																.equals(id))
										.findFirst()
										.orElse(null);
			
			if (selectedPortal == null) {
				player.sendMessage("Something went wrong trying to teleport, the selected portal was not found.");
				return;
			}
			
			this.onSuccess.accept(selectedPortal);
			
		}, this :: openWorldSelect);
		
		worldPortals.forEach(selector :: addOption);
		
		selector.open();
		
	}
	
}
