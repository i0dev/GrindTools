package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.object.TechChips;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class TypeTechChip extends TypeAbstractChoice<TechChips> {
    private static final TypeTechChip i = new TypeTechChip();

    public static TypeTechChip get() {
        return i;
    }

    public TypeTechChip() {
        super(TechChips.class);
    }

    public String getName() {
        return "text";
    }

    public TechChips read(String arg, CommandSender sender) throws MassiveException {
        try {
            return EnumSet.allOf(TechChips.class).stream().filter(tools -> tools.name().equalsIgnoreCase(arg)).findFirst().orElse(null);
        } catch (Exception e) {
            throw new MassiveException().addMessage("Invalid tool type: " + arg);
        }
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return EnumSet.allOf(TechChips.class).stream().map(Enum::name).collect(Collectors.toList());
    }
}
