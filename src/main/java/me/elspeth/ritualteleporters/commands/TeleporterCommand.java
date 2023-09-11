package me.elspeth.ritualteleporters.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.*;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.elspeth.ritualteleporters.commands.subcommands.HelpSubcommand;
import me.elspeth.ritualteleporters.utils.ItemUtils;

public class TeleporterCommand implements TabExecutor {
	
	private final List<Subcommand> subcommands = new ArrayList<>();
	private final HelpSubcommand   helpCommand = new HelpSubcommand();
	
	public void addSubcommand(Subcommand subcommand) {
		
		this.subcommands.add(subcommand);
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		if (args.length == 0) {
			helpCommand.onCommand(sender, command, label, args);
			return true;
		}
		
		subcommands.stream()
				   .filter(subcommand -> subcommand.getName()
												   .equalsIgnoreCase(args[0]))
				   .findAny()
				   .orElse(helpCommand)
				   .onCommand(sender, command, label, args);
		
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		var list = new ArrayList<String>();
		var out  = new ArrayList<String>();
		
		if (args.length == 1) {
			if (sender.hasPermission("ritualteleporter.change.name")) {
				list.add("name");
			}
			if (sender.hasPermission("ritualteleporter.change.display")) {
				list.add("display");
			}
			if (sender.hasPermission("ritualteleporter.change.members")) {
				list.add("members");
			}
			StringUtil.copyPartialMatches(args[0], list, out);
			return out;
		}
		
		if (args.length == 2) {
			switch (args[0]) {
				case "name":
					list.add(args[1]);
					return list;
				case "display":
					String expanded = args[1];
					if (!"minecraft:".startsWith(expanded.toLowerCase()) && !StringUtil.startsWithIgnoreCase(expanded, "minecraft:")) {
						expanded = "minecraft:" + expanded;
					}
					StringUtil.copyPartialMatches(expanded, ItemUtils.getMinecraftIds(), list);
					return list;
				case "members":
					if (sender.hasPermission("ritualteleporter.change.members")) {
						list.add("add");
						list.add("remove");
						list.add("get");
					}
					StringUtil.copyPartialMatches(args[1], list, out);
					return out;
			}
		}
		
		return null;
	}
}
