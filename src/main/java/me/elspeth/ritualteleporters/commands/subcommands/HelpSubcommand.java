package me.elspeth.ritualteleporters.commands.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.elspeth.ritualteleporters.commands.IHasSubcommands;
import me.elspeth.ritualteleporters.commands.Subcommand;

public class HelpSubcommand extends Subcommand {
	
	private IHasSubcommands parent;
	
	public HelpSubcommand(IHasSubcommands parent) {
		
		this.parent = parent;
	}
	
	@Override
	public String getName() {
		
		return "help";
	}
	
	@Override
	public String getSyntax() {
		
		return "[command]";
	}
	
	@Override
	public String getDescription() {
		
		return "Shows the help of specified command or a short list of all possible commands.";
	}
	
	@Override
	protected String getPermissionString() {
		
		return null;
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		if (args.length <= 1) {
			sendHelp(sender);
			return true;
		}
		
		var subcommand = parent.getSubcommands().stream().filter(cmd -> cmd.getName().equalsIgnoreCase(args[1])).findAny().orElse(null);
		
		if (subcommand == null) {
			sendHelp(sender);
			return true;
		}
		
		sendHelp(sender, subcommand);
		
		return true;
	}
	
	private void sendHelp(CommandSender sender) {
		
		var out = writeHeader("Teleporter help");
		
		for (var subcommand : parent.getSubcommands()) {
			out.appendNewline()
			   .append(formatSyntax(subcommand));
		}
		
		out.append(writeHeader(""));
		
		sender.sendMessage(out);
	}
	
	private void sendHelp(CommandSender sender, Subcommand subcommand) {
		
		
		var out = writeHeader(formatSyntax(subcommand));
		
		out.appendNewline()
		   .append(Component.text(subcommand.getDescription(), NamedTextColor.GRAY));
		
		sender.sendMessage(out);
	}
	
	private static final int    HEADER_HALF_LENGTH = 20;
	private static final String TEMPLATE           = "=".repeat(HEADER_HALF_LENGTH);
	
	private Component writeHeader(String content) {
		
		return writeHeader(Component.text(content, NamedTextColor.WHITE));
	}
	
	private Component writeHeader(TextComponent content) {
		
		var contentLength = content.content().length();
		
		var leftRemove  = contentLength / 2;
		var rightRemove = contentLength - leftRemove;
		
		leftRemove++;
		rightRemove++;
		
		var leftText  = TEMPLATE.substring(leftRemove);
		var rightText = TEMPLATE.substring(rightRemove);
		
		return Component.text(leftText, NamedTextColor.WHITE)
						.append(content)
						.append(Component.text(rightText, NamedTextColor.WHITE));
	}
	
	private TextComponent formatSyntax(Subcommand subcommand) {
		
		return Component
			.text("/teleport " + subcommand.getName(), NamedTextColor.WHITE)
			.append(formatComponents(subcommand.getSyntax()));
		
	}
	
	private TextComponent formatComponents(String syntax) {
		
		var elements = syntax.split(" ");
		var out      = Component.empty();
		for (var element : elements) {
			
			TextColor color = null;
			
			if (element.startsWith("<") && element.endsWith(">")) {
				color = NamedTextColor.BLUE;
			} else if (element.startsWith("[") && element.endsWith("]")) {
				color = NamedTextColor.YELLOW;
			} else {
				color = NamedTextColor.WHITE;
			}
			
			out = out.append(Component.text(element, color));
		}
		
		return out;
	}
}
