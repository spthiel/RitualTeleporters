package me.elspeth.ritualteleporters.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.inventory.LocationSelect;
import me.elspeth.ritualteleporters.teleporter.Teleporter;
import me.elspeth.ritualteleporters.teleporter.TeleporterStore;
import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Colors;

public class EventExecutor {
	
	private final TeleporterStore   store            = new TeleporterStore();
	private final Map<Player, Long> teleportCooldown = new HashMap<>();
	
	public void onLightCandle(
		@NotNull Player player,
		@NotNull Block candle
	) {
		
		var location = BlockUtils.getBuildTeleporter(candle);
		
		if (location == null) {
			return;
		}
		
		store.registerTeleporter(location, Colors.ofCandle(candle.getType()), player);
		
	}
	
	public void onColor(Block block, Item item, ItemStack dye) {
		
		Teleporter teleporter = store.getTeleporterOf(block);
		
		if (teleporter == null) {
			return;
		}
		
		Colors color = Colors.ofDye(dye);
		if (teleporter.getColor()
					  .equals(color) || color == null) {
			return;
		}
		teleporter.setColor(color);
		store.saveToDisk();
		
		if (dye.getAmount() > 1) {
			dye.setAmount(dye.getAmount() - 1);
			item.setItemStack(dye);
		} else {
			item.remove();
		}
	}
	
	public boolean onExtinguish(Player player, Block candle) {
		
		Teleporter teleporter = null;
		for (var direction : BlockUtils.diagonals) {
			teleporter = store.getTeleporterOf(candle.getRelative(direction));
			if (teleporter != null) {
				break;
			}
		}
		
		if (teleporter == null) {
			return true;
		}
		
		if (!teleporter.hasChangePermissions(player)) {
			return false;
		}
		
		store.removeTeleporter(teleporter);
		return true;
	}
	
	public void onPressurePlate(Player player, Block plate) {
		
		if (teleportCooldown.getOrDefault(player, 0L) > System.currentTimeMillis() - 1000) {
			return;
		}
		
		var currentTeleporter = store.getTeleporterOf(plate);
		
		if (currentTeleporter == null) {
			return;
		}
		
		var availableTeleporters = store.getTeleporters()
										.stream()
										.filter(teleporter -> teleporter.hasPermissions(player))
										.toList();
		
		var locationSelect = new LocationSelect(player, availableTeleporters);
		locationSelect.openWorldSelect();
		locationSelect.then(teleporter -> {
			teleporter.teleportTo(player);
			teleportCooldown.put(player, System.currentTimeMillis());
		});
		
	}
	
	/**
	 *
	 * @return Whether the block is allowed to be broken
	 */
	public boolean onBlockBreak(Block block) {
		
		if (BlockUtils.isLitCandle(block)) {
			for (var direction : BlockUtils.diagonals) {
				var teleporter = store.getTeleporterOf(block.getRelative(direction));
				if (teleporter != null) {
					return false;
				}
			}
			
			return true;
		}
		
		if (BlockUtils.isItemTriggerablePlate(block)) {
			return store.getTeleporterOf(block) == null;
		}
		
		var relative = block.getRelative(BlockFace.UP);
		
		if (BlockUtils.isPlanks(block)) {
			return store.getTeleporterOf(relative) == null;
		}
		
		if (BlockUtils.isWood(block)) {
			for (var direction : BlockUtils.woodDirection) {
				var teleporter = store.getTeleporterOf(relative.getRelative(direction));
				if (teleporter != null) {
					return false;
				}
			}
			
			return true;
		}
		
		return true;
	}
}
