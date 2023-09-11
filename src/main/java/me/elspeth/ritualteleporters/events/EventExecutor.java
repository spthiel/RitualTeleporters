package me.elspeth.ritualteleporters.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.inventory.LocationSelect;
import me.elspeth.ritualteleporters.portals.Portal;
import me.elspeth.ritualteleporters.portals.PortalStore;
import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Colors;

public class EventExecutor {
	
	private final PortalStore       store            = new PortalStore();
	private final Map<Player, Long> teleportCooldown = new HashMap<>();
	
	public void onLightCandle(
		@NotNull Player player,
		@NotNull Block candle
	) {
	
		var location = BlockUtils.getBuildPortal(candle);
		
		if(location == null) {
			return;
		}
		
		store.registerPortal(location, Colors.ofCandle(candle.getType()), player);
		
	}
	
	public void onColor(Block block, Item item, ItemStack dye) {
	
		Portal portal = store.getPortalOf(block);
		
		if (portal == null) {
			return;
		}
		
		Colors color = Colors.ofDye(dye);
		if (portal.getColor().equals(color) || color == null) {
			return;
		}
		portal.setColor(color);
		store.saveToDisk();
		
		if (dye.getAmount() > 1) {
			dye.setAmount(dye.getAmount() - 1);
			item.setItemStack(dye);
		} else {
			item.remove();
		}
	}
	
	public void onExtinguish(Block candle) {
		
		Portal portal = null;
		for (var direction : BlockUtils.diagonals) {
			portal = store.getPortalOf(candle.getRelative(direction));
			if (portal != null) {
				break;
			}
		}
		
		if (portal == null) {
			return;
		}
		
		store.removePortal(portal);
		
	}
	
	public void onPressurePlate(Player player, Block plate) {
		
		if (teleportCooldown.getOrDefault(player, 0L) > System.currentTimeMillis() - 1000) {
			return;
		}
		
		var currentPortal = store.getPortalOf(plate);
		
		var locationSelect = new LocationSelect(player, store.getPortals().stream().filter(portal -> portal != currentPortal).toList());
		locationSelect.openWorldSelect();
		locationSelect.then(portal -> {
			portal.teleportTo(player);
			teleportCooldown.put(player, System.currentTimeMillis());
		});
	
	}
}
