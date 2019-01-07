package me.calebbassham.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class WhitelistCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            if (List.of("on", "enable", "true").contains(args[0].toLowerCase())) {
                Bukkit.setWhitelist(true);
                sender.sendMessage(ChatColor.AQUA + "Whitelist has been enabled.");
                return true;
            }

            if (List.of("off", "disable", "false").contains(args[0].toLowerCase())) {
                Bukkit.setWhitelist(false);
                sender.sendMessage(ChatColor.AQUA + "Whitelist has been disabled.");
                return true;
            }

            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.setWhitelisted(true));
                sender.sendMessage(ChatColor.AQUA + "All players have been added to the whitelist.");
                return true;
            }

            if (args[0].equalsIgnoreCase("clear")) {
                Bukkit.getWhitelistedPlayers().forEach(player -> player.setWhitelisted(false));
                sender.sendMessage(ChatColor.AQUA + "The whitelist has been cleared.");
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (Bukkit.getWhitelistedPlayers().size() == 0) {
                    sender.sendMessage(ChatColor.AQUA + "Whitelist is empty.");
                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getName).collect(Collectors.joining(", ")));
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                var target = Bukkit.getOfflinePlayer(args[1]);
                sender.sendMessage(String.format(ChatColor.AQUA + "%s has been added to the whitelist.", args[1]));
                target.setWhitelisted(true);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                var target = Bukkit.getOfflinePlayer(args[1]);
                sender.sendMessage(String.format(ChatColor.AQUA + "%s has been removed from the whitelist.", args[1]));
                target.setWhitelisted(false);
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("enable", "disable", "all", "clear", "list", "add", "remove").stream()
                    .filter(s -> s.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(player -> !player.isWhitelisted())
                        .map(Player::getName)
                        .filter(s -> s.startsWith(args[1]))
                        .collect(Collectors.toList());
            }

            if (args[0].equalsIgnoreCase("remove")) {
                return Bukkit.getWhitelistedPlayers().stream()
                        .map(OfflinePlayer::getName)
                        .filter(s -> s.startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }

    static void register() {
        var pc = Bukkit.getPluginCommand("whitelist");
        var cmd = new WhitelistCmd();
        pc.setExecutor(cmd);
        pc.setTabCompleter(cmd);
    }
}
