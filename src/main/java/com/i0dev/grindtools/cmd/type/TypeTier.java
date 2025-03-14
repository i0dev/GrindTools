package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.Tier;
import com.i0dev.grindtools.entity.object.Tools;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class TypeTier extends TypeAbstractChoice<String> {
    private static final TypeTier i = new TypeTier();

    public static TypeTier get() {
        return i;
    }

    public TypeTier() {
        super(String.class);
    }

    public String getName() {
        return "text";
    }

    public String read(String arg, CommandSender sender) {
        return arg;
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        List<String> ret = new ArrayList<>();
        ret.addAll(HoeConfig.get().getTiers().stream().map(Tier::getId).collect(Collectors.toList()));
        ret.addAll(PickaxeConfig.get().getTiers().stream().map(Tier::getId).collect(Collectors.toList()));
        ret.addAll(RodConfig.get().getTiers().stream().map(Tier::getId).collect(Collectors.toList()));
        ret.addAll(SwordConfig.get().getTiers().stream().map(Tier::getId).collect(Collectors.toList()));
        return ret;
    }
}
