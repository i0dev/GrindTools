package com.i0dev.grindtools.cmd.type;

import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeOfflinePlayer extends TypeAbstractChoice<OfflinePlayer> {
    private static final TypeOfflinePlayer i = new TypeOfflinePlayer();

    public static TypeOfflinePlayer get() {
        return i;
    }

    private TypeOfflinePlayer() {
        super(OfflinePlayer.class);
    }

    public OfflinePlayer read(String arg, CommandSender sender) {
        Player player = Bukkit.getPlayer(arg);

        if (player != null) return player;

        return Bukkit.getOfflinePlayer(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }


}