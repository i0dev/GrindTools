package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.OreConfig;
import com.i0dev.grindtools.entity.object.Ore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeOre extends TypeAbstractChoice<Ore> {
    private static final TypeOre i = new TypeOre();

    public static TypeOre get() {
        return i;
    }

    public TypeOre() {
        super(Ore.class);
    }

    public String getName() {
        return "text";
    }

    public Ore read(String arg, CommandSender sender) {
        return OreConfig.get().getOreById(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return OreConfig.get().ores.stream().map(Ore::getId).collect(Collectors.toList());
    }
}
