package me.frosty.command;

import me.frosty.BowsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BowCommand implements CommandExecutor {

    // Just pass the plugin through so I dont need to call BowsPlugin#getInstance!
    private final BowsPlugin plugin;

    public BowCommand(BowsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // This just sets the toggle to what it isnt currently.
            this.plugin.getEnabled().set(!this.plugin.getEnabled().get());
            sender.sendMessage(ChatColor.GREEN + "Bows plguin is now: " + ChatColor.DARK_AQUA + this.plugin.getEnabled() + ".");
            return true;
        } else {
            //L?
            sender.sendMessage(ChatColor.RED + "Too many arguments.");
        }
        return false;
    }
}
