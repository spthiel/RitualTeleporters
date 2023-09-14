package me.elspeth.ritualteleporters.portals;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Candle;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import me.elspeth.ritualteleporters.utils.BlockUtils;
import me.elspeth.ritualteleporters.utils.Colors;

public class Portal implements ConfigurationSerializable {
	
	private static int counter = 0;
	
	private final int id;
	private final Location location;
	private final Location centerLocation;
	private       Colors color;
	private final UUID   owner;
	private final World  world;
	
	private String name = "Portal";
	private Material     item    = Material.GRASS_BLOCK;
	private List<UUID> members = new ArrayList<>();
	
	public Portal(Location location, Colors color, Player owner) {
		this(location, color, owner.getUniqueId());
	}
	public Portal(Location location, Colors color, UUID owner) {
		this.id = counter++;
		this.owner = owner;
		this.location = location;
		this.world = location.getWorld();
		this.centerLocation = location.clone()
									  .add(0.5, 0, 0.5);
		this.color = color;
	}
	
	public Location getLocation() {
		
		return location;
	}
	
	public Colors getColor() {
		
		return color;
	}
	
	public int getId() {
		
		return id;
	}
	
	public World getWorld() {
		
		return world;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public void setItem(Material item) {
		
		this.item = item;
	}
	
	public Material getItem() {
		return this.item;
	}
	
	public void addMember(UUID uuid) {
		this.members.add(uuid);
	}
	
	public void addMember(OfflinePlayer player) {
		this.addMember(player.getUniqueId());
	}
	
	public void removeMember(UUID uuid) {
		this.members.remove(uuid);
	}
	
	public void removeMember(OfflinePlayer player) {
		this.removeMember(player.getUniqueId());
	}
	
	public void spawn() {
		
		changeCandles();
		this.world.playSound(centerLocation, Sound.ENTITY_WITHER_SPAWN, 0.25f, 2.5f);
		
	}
	
	public void remove() {
		
		this.world.playSound(centerLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f);
//		this.blockDisplay.remove();
		var directions = BlockUtils.diagonals;
		
		Block center = location.getBlock();
		
		for (var direction : directions) {
			var block        = center.getRelative(direction);
			var originalData = (Candle) block.getBlockData();
			var newData = color.getCandle()
							   .createBlockData(data -> {
								   Candle blockdata = (Candle) data;
								   blockdata.setLit(false);
								   blockdata.setCandles(originalData.getCandles());
							   });
			block.setBlockData(newData);
		}
	}
	
	public void setColor(Colors color) {
		
		this.color = color;
//		this.blockDisplay.setGlowColorOverride(color.getColor());
		changeCandles();
	}
	
	private void changeCandles() {
		
		var directions = BlockUtils.diagonals;
		
		Block center = location.getBlock();
		
		for (var direction : directions) {
			var block        = center.getRelative(direction);
			var originalData = (Candle) block.getBlockData();
			var newData = color.getCandle()
							   .createBlockData(data -> {
								   Candle blockdata = (Candle) data;
								   blockdata.setLit(true);
								   blockdata.setCandles(originalData.getCandles());
							   });
			block.setBlockData(newData);
		}
	}
	
	public void spawnParticle(float dx, float dz) {
		
		if (this.centerLocation.isChunkLoaded()) {
			dx *= 1.4;
			dz *= 1.4;
			var loc1 = this.centerLocation.clone()
										  .add(dx, 0.5, dz);
			var loc2 = this.centerLocation.clone()
										  .add(-dx, 0.5, -dz);
			
			this.world.spawnParticle(Particle.REDSTONE, loc1, 3, new Particle.DustOptions(color.getColor(), 1));
			this.world.spawnParticle(Particle.REDSTONE, loc2, 3, new Particle.DustOptions(color.getColor(), 1));
			this.world.spawnParticle(Particle.PORTAL, this.centerLocation, 1, 0.5, 0, 0.5, 0.2);
			
		}
	}
	
	@Override
	public @NotNull Map<String, Object> serialize() {
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("location", this.location);
		data.put("color", this.color.name());
		data.put("owner", this.owner.toString());
		data.put("name", this.name);
		data.put("display", this.item.name());
		data.put("members", this.members.stream().map(UUID::toString));
		
		return data;
	}
	
	public static Portal deserialize(Map<String, Object> data) {
		var location = (Location) data.get("location");
		var colorName = (String) data.get("color");
		var colors = Colors.valueOf(colorName);
		var ownerUUID = UUID.fromString((String)data.get("owner"));
		
		var portal = new Portal(location, colors, ownerUUID);
		
		portal.setName((String)data.getOrDefault("name", "Portal"));
		portal.setItem(Material.valueOf((String)data.getOrDefault("display", "GRASS_BLOCK")));
		
		//noinspection unchecked
		for (var member : (Iterable<String>)data.getOrDefault("members", new ArrayList<String>())) {
			
			var memberUUID = UUID.fromString(member);
			portal.addMember(memberUUID);
			
		}
		
		return portal;
	}
	
	
	public void teleportTo(Player player) {
		player.teleport(this.centerLocation);
	}
	
	public UUID getOwner() {
		
		return this.owner;
	}
	
	public List<UUID> getMembers() {
		
		return members;
	}
}
