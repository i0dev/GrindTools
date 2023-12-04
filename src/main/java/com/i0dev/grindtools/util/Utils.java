package com.i0dev.grindtools.util;

import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.MPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    @SafeVarargs
    public static void msg(CommandSender recipient, String message, Pair<String, String>... pairs) {
        recipient.sendMessage(prefixAndColor(message, pairs));
    }

    @SafeVarargs
    public static void msg(MPlayer recipient, String message, Pair<String, String>... pairs) {
        recipient.msg(prefixAndColor(message, pairs));
    }

    @SafeVarargs
    public static String prefixAndColor(String message, Pair<String, String>... pairs) {
        return color(pair(message.replace("%prefix%", MLang.get().prefix), pairs));
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', translateHex(message));
    }

    public static String translateHex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, String.valueOf(net.md_5.bungee.api.ChatColor.of(color)));
            matcher = pattern.matcher(message);

        }
        return message;
    }

    public static String[] prefixColorFormat(List<String> in, Pair<String, String>... pairs) {

        String[] msg = new String[in.size()];
        for (int i = 0; i < in.size(); i++) {
            msg[i] = prefixAndColor(in.get(i));
        }

        return msg;
    }

    @SafeVarargs
    public static String pair(String msg, Pair<String, String>... pairs) {
        for (Pair<String, String> pair : pairs) {
            msg = msg.replace(pair.getKey(), pair.getValue());
        }
        return msg;
    }

    /**
     * Run a list of commands as console
     *
     * @param commands list of commands
     * @param player   player to replace %player% with
     */
    public static void runCommands(List<String> commands, Player player) {
        commands.forEach(command -> runCommand(command, player));
    }

    /**
     * Run a command as console
     *
     * @param command command to run
     * @param player  player to replace %player% with
     */
    public static void runCommand(String command, Player player) {
        command = command.replace("%player%", player == null ? "unknown" : player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }


    /**
     * Sends a title and a subtitle to a player.
     *
     * @param player The player to send the title to.
     * @param title The main title text.
     * @param subtitle The subtitle text.
     * @param fadeIn The time in ticks for the title to fade in.
     * @param stay The time in ticks for the title to stay on the screen.
     * @param fadeOut The time in ticks for the title to fade out.
     */
    public static void sendTitleToPlayer(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * Sends an action bar message to a player.
     *
     * @param player The player to send the action bar message to.
     * @param message The message text to be displayed in the action bar.
     */
    public static void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color(message)));
    }
}
