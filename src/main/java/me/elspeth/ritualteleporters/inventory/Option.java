package me.elspeth.ritualteleporters.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.elspeth.ritualteleporters.RitualTeleporter;

public class Option {
	
	private static NamespacedKey key = new NamespacedKey(RitualTeleporter.plugin, "selectorValue");
	
	private Material material;
	private String value;
	private String name;
	private String description;
	
	public Option() {
		this.material = Material.GRASS_BLOCK;
		this.name = "Default Name";
		this.value = null;
		this.description = null;
	}
	
	public ItemStack asItemStack() {
		var out = new ItemStack(this.material, 1);
		var meta = out.getItemMeta();
		meta.displayName(Component.text(this.name));
		if (this.description != null) {
			var lore = new ArrayList<TextComponent>();
			lore.add(Component.text(this.description));
			meta.lore(lore);
		}
		
		if (this.value != null) {
			var container = meta.getPersistentDataContainer();
			container.set(key, PersistentDataType.STRING, this.value);
		}
		out.setItemMeta(meta);
		return out;
	}
	
	public Option material(Material material) {
		this.material = material;
		return this;
	}
	
	public Option name(String name) {
		this.name = name;
		return this;
	}
	
	public Option value(String value) {
		this.value = value;
		return this;
	}

	public Option description(String description) {
		this.description = description;
		return this;
	}
	
	public static String getValueOf(ItemStack stack) {
		var meta = stack.getItemMeta();
		var container = meta.getPersistentDataContainer();
		return container.get(key, PersistentDataType.STRING);
	}
	
}
