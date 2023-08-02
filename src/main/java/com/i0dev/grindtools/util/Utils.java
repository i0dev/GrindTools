package com.i0dev.grindtools.util;

import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

}
