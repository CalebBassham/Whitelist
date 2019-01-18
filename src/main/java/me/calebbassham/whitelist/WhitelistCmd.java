package me.calebbassham.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

import static me.calebbassham.pluginmessageformat.PluginMessageFormat.getMainColorPallet;
import static me.calebbassham.pluginmessageformat.PluginMessageFormat.getPrefix;

public class WhitelistCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            if (List.of("on", "enable", "true").contains(args[0].toLowerCase())) {
                Bukkit.setWhitelist(true);
                sender.sendMessage(getPrefix() + getMainColorPallet().getHighlightTextColor() + "Whitelist" +
                        getMainColorPallet().getPrimaryTextColor() + " has been " + getMainColorPallet().getValueTextColor() + "enabled" +
                        getMainColorPallet().getPrimaryTextColor() + ".");
                return true;
            }

            if (List.of("off", "disable", "false").contains(args[0].toLowerCase())) {
                Bukkit.setWhitelist(false);
                sender.sendMessage(getPrefix() + getMainColorPallet().getHighlightTextColor() + "Whitelist" +
                        getMainColorPallet().getPrimaryTextColor() + " has been " + getMainColorPallet().getValueTextColor() + "disabled" +
                        getMainColorPallet().getPrimaryTextColor() + ".");
                return true;
            }

            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.setWhitelisted(true));
                sender.sendMessage(getPrefix() + getMainColorPallet().getValueTextColor() + "All players" +
                        getMainColorPallet().getPrimaryTextColor() + " have been " + getMainColorPallet().getHighlightTextColor() + "added" +
                        getMainColorPallet().getPrimaryTextColor() + " to the " + getMainColorPallet().getHighlightTextColor() + "whitelist" +
                        getMainColorPallet().getPrimaryTextColor() + ".");
                return true;
            }

            if (args[0].equalsIgnoreCase("clear")) {
                Bukkit.getWhitelistedPlayers().forEach(player -> player.setWhitelisted(false));
                sender.sendMessage(getPrefix() + "The " + getMainColorPallet().getHighlightTextColor() + "whitelist" +
                        getMainColorPallet().getPrimaryTextColor() + " has been " + getMainColorPallet().getHighlightTextColor() + "cleared" +
                        getMainColorPallet().getPrimaryTextColor() + ".");
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (Bukkit.getWhitelistedPlayers().size() == 0) {
                    sender.sendMessage(getPrefix() + getMainColorPallet().getHighlightTextColor() + "Whitelist" +
                            getMainColorPallet().getPrimaryTextColor() + " is " + getMainColorPallet().getValueTextColor() +
                            "empty" + getMainColorPallet().getPrimaryTextColor() + ".");
                    return true;
                }

                sender.sendMessage(getPrefix() + Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getName).collect(Collectors.joining(", ")));
                return true;
            }

            if (args[0].equalsIgnoreCase("status")) {
                sender.sendMessage(getPrefix() + getMainColorPallet().getHighlightTextColor() + "Whitelist " + getMainColorPallet().getPrimaryTextColor() +
                        "is currently " + getMainColorPallet().getValueTextColor() + (Bukkit.hasWhitelist() ? "enabled" : "disabled") +
                        getMainColorPallet().getPrimaryTextColor() + ".");
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                var target = Bukkit.getOfflinePlayer(args[1]);

                sender.sendMessage(getPrefix() + getMainColorPallet().getValueTextColor() + getDisplayName(target) + getMainColorPallet().getPrimaryTextColor() +
                        " has been " + getMainColorPallet().getHighlightTextColor() + "whitelisted" + getMainColorPallet().getPrimaryTextColor() + ".");

                target.setWhitelisted(true);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                var target = Bukkit.getOfflinePlayer(args[1]);

                sender.sendMessage(getPrefix() + getMainColorPallet().getValueTextColor() + getDisplayName(target) + getMainColorPallet().getPrimaryTextColor() +
                        " has been " + getMainColorPallet().getHighlightTextColor() + "unwhitelisted" + getMainColorPallet().getPrimaryTextColor() + ".");

                target.setWhitelisted(false);
                return true;
            }
        }

        return false;
    }

    private String getDisplayName(OfflinePlayer offlinePlayer) {
        var player = offlinePlayer.getPlayer();

        if (player != null) {
            return player.getDisplayName();
        } else {
            return offlinePlayer.getName();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("enable", "disable", "all", "clear", "list", "add", "remove", "status").stream()
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
