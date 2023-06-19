package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.object.Tools;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class TypeTool extends TypeAbstractChoice<Tools> {
    private static final TypeTool i = new TypeTool();

    public static TypeTool get() {
        return i;
    }

    public TypeTool() {
        super(Tools.class);
    }

    public String getName() {
        return "text";
    }

    public Tools read(String arg, CommandSender sender) throws MassiveException {
        try {
            return EnumSet.allOf(Tools.class).stream().filter(tools -> tools.name().equalsIgnoreCase(arg)).findFirst().orElse(null);
        } catch (Exception e) {
            throw new MassiveException().addMessage("Invalid tool type: " + arg);
        }
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return EnumSet.allOf(Tools.class).stream().map(Enum::name).collect(Collectors.toList());
    }
}
