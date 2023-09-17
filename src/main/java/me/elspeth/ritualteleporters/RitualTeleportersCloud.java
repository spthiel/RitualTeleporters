package me.elspeth.ritualteleporters;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import net.kyori.adventure.text.format.NamedTextColor;


import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;

public class RitualTeleportersCloud {

	private final JavaPlugin                          plugin;
	private       BukkitCommandManager<CommandSender> manager;
	private MinecraftHelp<CommandSender> minecraftHelp;
	
	public RitualTeleportersCloud(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void setup() {
		
		final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
			AsynchronousCommandExecutionCoordinator.<CommandSender>builder().build();
		
		final Function<CommandSender, CommandSender> mapperFunction = Function.identity();
		
		try {
			this.manager = new PaperCommandManager<>(
				plugin,
				executionCoordinatorFunction,
				mapperFunction,
				mapperFunction
			);
		} catch (final Exception e) {
			plugin.getLogger().severe("Failed to initialize the command this.manager");
			/* Disable the plugin */
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}
		
		plugin.getLogger().info("Initialized PaperCommandManager");
		
		this.minecraftHelp = new MinecraftHelp<>(
			"/teleporter help",
			sender -> sender,
			this.manager
		);
		
		if (this.manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
			this.manager.registerBrigadier();
		}
		
		if (this.manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
			((PaperCommandManager<CommandSender>) this.manager).registerAsynchronousCompletions();
		}
		
		new MinecraftExceptionHandler<CommandSender>()
			.withInvalidSyntaxHandler()
			.withInvalidSenderHandler()
			.withNoPermissionHandler()
			.withArgumentParsingHandler()
			.withCommandExecutionHandler()
			.withDecorator(
				component -> text()
					.append(text("[", NamedTextColor.DARK_GRAY))
					.append(text("Teleporter", NamedTextColor.GOLD))
					.append(text("] ", NamedTextColor.DARK_GRAY))
					.append(component).build()
			).apply(this.manager, commandSender -> commandSender);
		
	}
	
	public BukkitCommandManager<CommandSender> getManager() {
		
		return manager;
	}
	
	public MinecraftHelp<CommandSender> getMinecraftHelp() {
		
		return minecraftHelp;
	}
}
