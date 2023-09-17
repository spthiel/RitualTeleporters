package me.elspeth.ritualteleporters.utils;

import org.bukkit.inventory.ItemStack;

public class ItemUtils {
	
	public static boolean isDye(ItemStack itemStack) {
		return Colors.ofDye(itemStack) != null;
	}
	
}
