package me.elspeth.ritualteleporters.inventory;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.elspeth.ritualteleporters.RitualTeleporter;
import me.elspeth.ritualteleporters.utils.SyntacticBukkitRunnable;

public class Selector implements Listener {
	
	private static final int itemsPerPage     = 3 * 7;
	private static final int previousPageSlot = 9 * 2;
	private static final int nextPageSlot     = 9 * 2 + 8;
	
	private static final ItemStack decoration   = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
	private static final ItemStack nextPage     = new ItemStack(Material.PLAYER_HEAD);
	private static final ItemStack previousPage = new ItemStack(Material.PLAYER_HEAD);
	
	private static SkullMeta setSkullUrl(ItemMeta meta, String url) {
		var skullMeta = (SkullMeta)meta;
		var profile = Bukkit.getOfflinePlayer(UUID.fromString("7b33a0b4-7678-4ea0-9b6f-931bdf90e745")).getPlayerProfile();
		var textures = profile.getTextures();
		try {
			textures.setSkin(new URL(url));
			profile.setTextures(textures);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//noinspection deprecation
		skullMeta.setOwnerProfile(profile);
		return skullMeta;
	}
	
	static {
		var nextMeta = (SkullMeta)nextPage.getItemMeta();
		nextMeta.displayName(Component.text("Next Page"));
		nextMeta = setSkullUrl(nextMeta, "http://textures.minecraft.net/texture/682ad1b9cb4dd21259c0d75aa315ff389c3cef752be3949338164bac84a96e");
		nextPage.setItemMeta(nextMeta);
		
		var previousMeta = (SkullMeta)previousPage.getItemMeta();
		previousMeta.displayName(Component.text("Previous Page"));
		previousMeta = setSkullUrl(previousMeta, "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645");
		previousPage.setItemMeta(previousMeta);
	}
	
	private       Inventory        inventory;
	private final Player           player;
	private       Consumer<String> onSuccess;
	private       Runnable         onClose;
	private final List<Option>     options = new ArrayList<>();
	private       int              page    = 0;
	private       boolean          open    = false;
	
	public Selector(Player player) {
		
		this.inventory = Bukkit.createInventory(null, 5 * 9);
		this.player = player;
	}
	
	public void then(Consumer<String> onSuccess) {
		
		this.onSuccess = onSuccess;
	}
	
	public void then(Consumer<String> onSuccess, Runnable onClose) {
		
		this.onSuccess = onSuccess;
		this.onClose = onClose;
	}
	
	public Player getPlayer() {
		
		return player;
	}
	
	public Inventory getInventory() {
		
		return inventory;
	}
	
	public void addOption(Option option) {
		
		this.options.add(option);
	}
	
	public void open() {
		
		InventoryListener.instance.addSelector(this);
		decorate();
		this.page = 0;
		this.showPage();
		
		// WHY DO WE HAVE TO DO THIS??????
		var runnable = new SyntacticBukkitRunnable(() -> {
			player.openInventory(inventory);
			this.open = true;
		});
		runnable.runTask(RitualTeleporter.plugin);
	}
	
	private void decorate() {
		
		for (int i = 0 ; i < 9 ; i++) {
			inventory.setItem(i, decoration);
			inventory.setItem(i + 36, decoration);
		}
		for (int i = 1 ; i < 4 ; i++) {
			inventory.setItem(i * 9, decoration);
			inventory.setItem(i * 9 + 8, decoration);
		}
	}
	
	private void showPage() {
		
		var pageOptions = options.subList(page * itemsPerPage, Math.min((page + 1) * itemsPerPage, options.size()));
		
		var slot = 9;
		for (var i = 0 ; i < itemsPerPage ; i++) {
			var option = i < pageOptions.size() ? pageOptions.get(i) : null;
			slot++;
			if (slot % 9 == 8) {
				slot += 2;
			}
			if (option != null) {
				inventory.setItem(slot, option.asItemStack());
			} else {
				inventory.setItem(slot, new ItemStack(Material.AIR));
			}
		}
		
		inventory.setItem(nextPageSlot, hasNextPage() ? nextPage : decoration);
		inventory.setItem(previousPageSlot, hasPreviousPage() ? previousPage : decoration);
		
	}
	
	public void close() {
		
		InventoryListener.instance.removeSelector(this);
		player.closeInventory();
		this.open = false;
	}
	
	public void abort() {
		
		if (!this.open) {
			return;
		}
		
		this.open = false;
		
		InventoryListener.instance.removeSelector(this);
		
		if (this.onClose != null) {
			this.onClose.run();
		}
		
	}
	
	public void onClick(int slot) {
		
		if (slot == nextPageSlot) {
			this.nextPage();
			return;
		}
		
		if (slot == previousPageSlot) {
			this.previousPage();
			return;
		}
		
		if (isDecorationSlot(slot)) {
			return;
		}
		
		ItemStack stack = inventory.getItem(slot);
		
		if (stack == null) {
			return;
		}
		
		var value = Option.getValueOf(stack);
		this.onSuccess.accept(value);
	}
	
	private boolean hasNextPage() {
		
		return this.page * itemsPerPage < this.options.size() - itemsPerPage;
	}
	
	private boolean hasPreviousPage() {
		
		return this.page > 0;
	}
	
	private boolean isDecorationSlot(int slot) {
		
		if (slot <= 9) {
			return true;
		}
		if (slot >= 9 * 4) {
			return true;
		}
		
		if (slot % 9 == 0) {
			return true;
		}
		
		if (slot % 9 == 8) {
			return true;
		}
		
		return false;
	}
	
	private void nextPage() {
		
		if (!hasNextPage()) {
			return;
		}
		
		this.page++;
		this.showPage();
	}
	
	private void previousPage() {
		
		if (!hasPreviousPage()) {
			return;
		}
		
		this.page--;
		this.showPage();
	}
	
}
