package me.elspeth.ritualteleporters.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
	
	private static List<String> minecraftIds;
	
	public static List<String> getMinecraftIds() {
		if (minecraftIds == null) {
			minecraftIds = Arrays.stream(Material.values()).map(Material::name).map(String::toLowerCase).map(name -> "minecraft:" + name).toList();
		}
		return minecraftIds;
	}
	
	public static boolean isDye(ItemStack itemStack) {
		var type = itemStack.getType();
		return Colors.ofDye(itemStack) != null;
			
	}
	
}
