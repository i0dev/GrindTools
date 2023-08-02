package com.i0dev.grindtools.integration;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.MPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI(GrindToolsPlugin plugin) {
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "i0dev";
    }

    @Override
    public String getIdentifier() {
        return "grindtools";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    NumberFormat format = NumberFormat.getInstance();

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("currency")) {
            return format.format(MPlayer.get(player).getCurrency());
        }


        return null;
    }

}