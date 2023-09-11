package me.elspeth.ritualteleporters.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {
	
	public static InventoryListener instance;
	private       List<Selector>    selectors = new ArrayList<>();
	
	public InventoryListener() {
		
		instance = this;
	}
	
	public void addSelector(Selector selector) {
		
		this.selectors.add(selector);
	}
	
	public void removeSelector(Selector selector) {
		
		this.selectors.remove(selector);
	}
	
	private boolean isPlayersSelector(Selector selector, Player player) {
		
		return selector.getPlayer()
					   .getUniqueId()
					   .equals(player.getUniqueId());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!(event.getWhoClicked() instanceof Player player)) {
			return;
		}
		
		this.selectors.stream()
					  .filter(selector -> isPlayersSelector(selector, player))
					  .findFirst()
					  .ifPresent(selector -> {
						  event.setCancelled(true);
						  selector.onClick(event.getRawSlot());
					  });
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		
		if (!(event.getPlayer() instanceof Player player)) {
			return;
		}
		
		if (event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) {
			return;
		}
		
		this.selectors.stream()
					  .filter(selector -> isPlayersSelector(selector, player))
					  .findFirst()
					  .ifPresent(Selector :: abort);
	}
}
