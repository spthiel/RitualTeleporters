package me.elspeth.ritualteleporters.utils;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

@SuppressWarnings({"deprecation", "unused"})
public enum Colors {
	
	DEFAULT(
		Color.fromRGB(230, 185, 140),
		ChatColor.WHITE,
		NamedTextColor.WHITE,
		Material.CANDLE,
		Material.POTION
	),
	WHITE(
		Color.fromRGB(199, 211, 211),
		ChatColor.WHITE,
		NamedTextColor.WHITE,
		Material.WHITE_CANDLE,
		Material.WHITE_DYE
	),
	YELLOW(
		Color.fromRGB(197, 152, 37),
		ChatColor.YELLOW,
		NamedTextColor.YELLOW,
		Material.YELLOW_CANDLE,
		Material.YELLOW_DYE
	),
	PINK(
		Color.fromRGB(197, 89, 131),
		ChatColor.LIGHT_PURPLE,
		NamedTextColor.LIGHT_PURPLE,
		Material.PINK_CANDLE,
		Material.PINK_DYE
	),
	MAGENTA(
		Color.fromRGB(156, 40, 148),
		ChatColor.RED,
		NamedTextColor.RED,
		Material.MAGENTA_CANDLE,
		Material.MAGENTA_DYE
	),
	LIGHTBLUE(
		Color.fromRGB(32, 133, 197),
		ChatColor.AQUA,
		NamedTextColor.AQUA,
		Material.LIGHT_BLUE_CANDLE,
		Material.LIGHT_BLUE_DYE
	),
	LIME(
		Color.fromRGB(90, 165, 16),
		ChatColor.GREEN,
		NamedTextColor.GREEN,
		Material.LIME_CANDLE,
		Material.LIME_DYE
	),
	BROWN(
		Color.fromRGB(100, 61, 33),
		ChatColor.BLUE,
		NamedTextColor.BLUE,
		Material.BROWN_CANDLE,
		Material.BROWN_DYE
	),
	GRAY(
		Color.fromRGB(74, 89, 90),
		ChatColor.DARK_GRAY,
		NamedTextColor.DARK_GRAY,
		Material.GRAY_CANDLE,
		Material.GRAY_DYE
	),
	LIGHTGRAY(
		Color.fromRGB(106, 109, 98),
		ChatColor.GRAY,
		NamedTextColor.GRAY,
		Material.LIGHT_GRAY_CANDLE,
		Material.LIGHT_GRAY_DYE
	),
	ORANGE(
		Color.fromRGB(213, 89, 0),
		ChatColor.GOLD,
		NamedTextColor.GOLD,
		Material.ORANGE_CANDLE,
		Material.ORANGE_DYE
	),
	PURPLE(
		Color.fromRGB(98, 28, 156),
		ChatColor.DARK_PURPLE,
		NamedTextColor.DARK_PURPLE,
		Material.PURPLE_CANDLE,
		Material.PURPLE_DYE
	),
	RED(
		Color.fromRGB(144, 33, 32),
		ChatColor.DARK_RED,
		NamedTextColor.DARK_RED,
		Material.RED_CANDLE,
		Material.RED_DYE
	),
	CYAN(
		Color.fromRGB(15, 105, 104),
		ChatColor.DARK_AQUA,
		NamedTextColor.DARK_AQUA,
		Material.CYAN_CANDLE,
		Material.CYAN_DYE
	),
	GREEN(
		Color.fromRGB(63, 83, 18),
		ChatColor.DARK_GREEN,
		NamedTextColor.DARK_GREEN,
		Material.GREEN_CANDLE,
		Material.GREEN_DYE
	),
	BLUE(
		Color.fromRGB(53, 71, 154),
		ChatColor.DARK_BLUE,
		NamedTextColor.DARK_BLUE,
		Material.BLUE_CANDLE,
		Material.BLUE_DYE
	),
	BLACK(
		Color.fromRGB(26, 24, 39),
		ChatColor.BLACK,
		NamedTextColor.BLACK,
		Material.BLACK_CANDLE,
		Material.BLACK_DYE
	),
	;
	
	private final Color     color;
	private final ChatColor chatColor;
	private final NamedTextColor textColor;
	private final Material       candle;
	private final Material       dye;
	
	Colors(Color color, ChatColor chatColor, NamedTextColor textColor, Material candle, Material dye) {
		
		this.color = color;
		this.chatColor = chatColor;
		this.textColor = textColor;
		this.candle = candle;
		this.dye = dye;
	}
	
	public Color getColor() {
		
		return color;
	}
	
	public ChatColor getChatColor() {
		
		return chatColor;
	}
	
	public NamedTextColor getTextColor() {
		
		return textColor;
	}
	
	public Material getCandle() {
		
		return candle;
	}
	
	public static Colors ofChatColor(ChatColor chatColor) {
		
		return streamValues().filter(color -> color.chatColor.equals(chatColor))
							 .findFirst()
							 .orElse(null);
	}
	
	public static Colors ofNamedTextColor(NamedTextColor textColor) {
		
		return streamValues().filter(color -> color.textColor.equals(textColor))
							 .findFirst()
							 .orElse(null);
	}
	
	public static Colors ofCandle(Material candle) {
		
		return streamValues().filter(color -> color.candle.equals(candle))
							 .findFirst()
							 .orElse(null);
	}
	
	public static Colors ofDye(ItemStack dye) {
		Material material = dye.getType();
		if (material.equals(Material.POTION)) {
			var meta = (PotionMeta)dye.getItemMeta();
			if(!meta.getBasePotionData().getType().equals(PotionType.WATER)) {
				return null;
			}
		}
		return Colors.ofDye(material);
	}
	
	private static Colors ofDye(Material dye) {
		return streamValues().filter(color -> color.dye.equals(dye))
							 .findFirst()
							 .orElse(null);
	}
	
	private static Stream<Colors> streamValues() {
		
		return Arrays.stream(Colors.values());
	}
	
}
