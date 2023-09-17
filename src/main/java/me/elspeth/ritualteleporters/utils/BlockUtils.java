package me.elspeth.ritualteleporters.utils;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Candle;

public class BlockUtils {
	
	public static final BlockFace[] diagonals = {BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST};
	public static final BlockFace[] woodDirection = {
		BlockFace.NORTH,
		BlockFace.NORTH_EAST,
		BlockFace.EAST,
		BlockFace.SOUTH_EAST,
		BlockFace.SOUTH,
		BlockFace.SOUTH_WEST,
		BlockFace.WEST,
		BlockFace.NORTH_WEST
	};
	
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
	
	public static Location getBuildTeleporter(Block candle) {
		
		var northCandle = isCandle(candle.getRelative(BlockFace.NORTH, 2));
		var eastCandle  = isCandle(candle.getRelative(BlockFace.EAST, 2));
		var southCandle = isCandle(candle.getRelative(BlockFace.SOUTH, 2));
		var westCandle  = isCandle(candle.getRelative(BlockFace.WEST, 2));
		
		if (northCandle && eastCandle) {
			var center = candle.getRelative(BlockFace.NORTH_EAST);
			if (checkTeleporter(center)) {
				return center.getLocation();
			}
		}
		
		if (eastCandle && southCandle) {
			var center = candle.getRelative(BlockFace.SOUTH_EAST);
			if (checkTeleporter(center)) {
				return center.getLocation();
			}
		}
		
		if (southCandle && westCandle) {
			var center = candle.getRelative(BlockFace.SOUTH_WEST);
			if (checkTeleporter(center)) {
				return center.getLocation();
			}
			
		}
		
		if (westCandle && northCandle) {
			var center = candle.getRelative(BlockFace.NORTH_WEST);
			if (checkTeleporter(center)) {
				return center.getLocation();
			}
		}
		
		return null;
	}
	
	private static boolean checkTeleporter(Block center) {
		
		if (!isItemTriggerablePlate(center)) {
			return false;
		}
		
		if (!isPlanks(center.getRelative(BlockFace.DOWN))) {
			return false;
		}
		
		for (var direction : woodDirection) {
			if (!isWood(center.getRelative(direction).getRelative(BlockFace.DOWN))) {
				return false;
			}
		}
		
		var litCandles = 0;
		
		for (var direction : diagonals) {
			if (isLitCandle(center.getRelative(direction))) {
				litCandles++;
			}
		}
		
		return litCandles >= 3;
	}
}
