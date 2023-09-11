package me.elspeth.ritualteleporters.utils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Candle;
import org.bukkit.plugin.PluginBase;

public class BlockUtils {
	
	public static final BlockFace[] diagonals = {BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST};
	
	public static boolean isWood(@Nullable Block block) {
		if (block == null) {
			return false;
		}
		
		return Tag.LOGS.isTagged(block.getType()) ||
			block.getType()
				 .equals(Material.BAMBOO_BLOCK) ||
			block.getType()
				 .equals(Material.STRIPPED_BAMBOO_BLOCK);
	}
	
	public static boolean isPlanks(@Nullable Block block) {
		if (block == null) {
			return false;
		}
		
		return Tag.PLANKS.isTagged(block.getType());
	}
	
	public static boolean isPressurePlate(@Nullable Block block) {
		if (block == null) {
			return false;
		}
		
		return Tag.PRESSURE_PLATES.isTagged(block.getType());
	}
	
	public static boolean isItemTriggerablePlate(@Nullable Block block) {
		if (block == null) {
			return false;
		}
		return isPressurePlate(block) && !Tag.STONE_PRESSURE_PLATES.isTagged(block.getType());
	}
	
	public static boolean isLitCandle(@Nullable Block block) {
		
		if (!isCandle(block)) {
			return false;
		}
		
		var state = block.getState();
		var data  = (Candle) state.getBlockData();
		
		return data.isLit();
	}
	
	public static boolean isCandle(@Nullable Block block) {
		if (block == null) {
			return false;
		}
		
		return Tag.CANDLES.isTagged(block.getType());
	}
	
	public static Location getBuildPortal(Block candle) {
		
		var northCandle = candle.getRelative(BlockFace.NORTH, 2);
		var eastCandle  = candle.getRelative(BlockFace.EAST, 2);
		var southCandle = candle.getRelative(BlockFace.SOUTH, 2);
		var westCandle  = candle.getRelative(BlockFace.WEST, 2);
		
		if (isCandle(northCandle) && isCandle(eastCandle)) {
			var center = candle.getRelative(BlockFace.NORTH_EAST);
			if (checkPortal(center)) {
				return center.getLocation();
			}
		}
		
		if (isCandle(eastCandle) && isCandle(southCandle)) {
			var center = candle.getRelative(BlockFace.SOUTH_EAST);
			if (checkPortal(center)) {
				return center.getLocation();
			}
		}
		
		if (isCandle(southCandle) && isCandle(westCandle)) {
			var center = candle.getRelative(BlockFace.SOUTH_WEST);
			if (checkPortal(center)) {
				return center.getLocation();
			}
			
		}
		
		if (isCandle(westCandle) && isCandle(northCandle)) {
			var center = candle.getRelative(BlockFace.NORTH_WEST);
			if (checkPortal(center)) {
				return center.getLocation();
			}
		}
		
		return null;
	}
	
	private static boolean checkPortal(Block center) {
		
		for (var dx = -1 ; dx <= 1 ; dx++) {
			for (var dz = -1 ; dz <= 1 ; dz++) {
				if (dx == 0 && dz == 0) {
					if (!isPlanks(center.getRelative(dx, -1, dz))) {
						return false;
					}
				} else {
					if (!isWood(center.getRelative(dx, -1, dz))) {
						return false;
					}
				}
			}
		}
		
		var litCandles = 0;
		
		if (isLitCandle(center.getRelative(BlockFace.NORTH_EAST))) {
			litCandles++;
		}
		
		if (isLitCandle(center.getRelative(BlockFace.NORTH_WEST))) {
			litCandles++;
		}
		
		if (isLitCandle(center.getRelative(BlockFace.SOUTH_EAST))) {
			litCandles++;
		}
		
		if (isLitCandle(center.getRelative(BlockFace.SOUTH_WEST))) {
			litCandles++;
		}
		
		return litCandles >= 3 &&
			isPressurePlate(center)
			;
	}
}
