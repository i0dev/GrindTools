package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.object.FishingCuboid;
import com.i0dev.grindtools.entity.object.Tools;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.sql.NClob;
import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class TypeFishingRegion extends TypeAbstractChoice<FishingCuboid> {
    private static final TypeFishingRegion i = new TypeFishingRegion();

    public static TypeFishingRegion get() {
        return i;
    }

    public TypeFishingRegion() {
        super(FishingCuboid.class);
    }

    public String getName() {
        return "text";
    }

    public FishingCuboid read(String arg, CommandSender sender) throws MassiveException {
        return MConf.get().fishingRegions.stream().filter(fishingCuboid -> fishingCuboid.getName().equalsIgnoreCase(arg)).findFirst().orElseThrow(() -> new MassiveException().addMessage("Fishing region not found"));
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return MConf.get().fishingRegions.stream().map(FishingCuboid::getName).collect(Collectors.toList());
    }
}
