package me.elspeth.ritualteleporters.utils;

import org.bukkit.scheduler.BukkitRunnable;

public class SyntacticBukkitRunnable extends BukkitRunnable {
	
	private final Runnable runnable;
	
	public SyntacticBukkitRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		this.runnable.run();
	}
}
