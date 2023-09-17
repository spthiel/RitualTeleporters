package me.elspeth.ritualteleporters.events;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Entry;
import me.elspeth.ritualteleporters.utils.ItemUtils;

public class TeleporterListener implements Listener {

	private final EventExecutor                       eventExecutor;
	private final HashMap<Player, Entry<Block, Long>> lastInteract = new HashMap<>();
	
	public TeleporterListener() {
		eventExecutor = new EventExecutor();
	}
	
	@EventHandler
	public void interactEvent(PlayerInteractEvent event) {
		if (checkExtinguish(event)) {
			return;
		}
		
		if (!event.getAction().equals(Action.PHYSICAL)) {
			return;
		}
		
		Block block = event.getClickedBlock();
		
		if (!BlockUtils.isItemTriggerablePlate(block)) {
			return;
		}
		
		var entry = lastInteract.get(event.getPlayer());
		
		if (entry != null && entry.getKey().getLocation().equals(block.getLocation()) && entry.getValue() > System.currentTimeMillis() - 1000) {
			entry.setValue(System.currentTimeMillis());
			return;
		}
		
		lastInteract.put(event.getPlayer(), new Entry<>(block, System.currentTimeMillis()));
		
		eventExecutor.onPressurePlate(event.getPlayer(), block);
		
	}
	
	private boolean checkExtinguish(PlayerInteractEvent event) {
		
		if (!BlockUtils.isLitCandle(event.getClickedBlock())) {
			return false;
		}
		
		if (event.getItem() != null && !event.getItem().getType().equals(Material.AIR)) {
			return false;
		}
		
		var allowed = eventExecutor.onExtinguish(event.getPlayer(), event.getClickedBlock());
		
		if (!allowed) {
			event.setCancelled(true);
		}
		
		return true;
	}
	
	@EventHandler
	private void blockBreak(BlockBreakEvent event) {
		if(!eventExecutor.onBlockBreak(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockIgniteEvent(BlockIgniteEvent event) {
		
		if (event.getBlock().getBlockData() instanceof Candle && event.getPlayer() != null) {
			eventExecutor.onLightCandle(event.getPlayer(), event.getBlock());
		}
	}
	
	@EventHandler
	public void entityInteractEvent(EntityInteractEvent event) {
		
		if (!BlockUtils.isItemTriggerablePlate(event.getBlock())) {
			return;
		}
		
		if (!event.getEntityType().equals(EntityType.DROPPED_ITEM)) {
			return;
		}
		
		Item      item  = (Item)event.getEntity();
		ItemStack stack = item.getItemStack();
		
		if (!ItemUtils.isDye(stack)) {
			return;
		}
		
		eventExecutor.onColor(event.getBlock(), item, stack);
	}
	
}
