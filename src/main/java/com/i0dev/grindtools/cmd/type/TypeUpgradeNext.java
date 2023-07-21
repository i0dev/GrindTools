package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.UpgradeConfig;
import com.i0dev.grindtools.entity.object.TierUpgrade;
import com.i0dev.grindtools.entity.object.TierUpgradeNext;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeUpgradeNext extends TypeAbstractChoice<TierUpgradeNext> {
    private static final TypeUpgradeNext i = new TypeUpgradeNext();

    public static TypeUpgradeNext get() {
        return i;
    }

    public TypeUpgradeNext() {
        super(TierUpgradeNext.class);
    }

    public String getName() {
        return "text";
    }

    public TierUpgradeNext read(String arg, CommandSender sender) {
        return UpgradeConfig.get().tiersNext.stream().filter(tierUpgrade -> tierUpgrade.getId().equalsIgnoreCase(arg)).findFirst().orElse(null);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return UpgradeConfig.get().tiersNext.stream().map(TierUpgradeNext::getId).collect(Collectors.toList());
    }
}
