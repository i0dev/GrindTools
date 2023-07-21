package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.UpgradeConfig;
import com.i0dev.grindtools.entity.object.Tier;
import com.i0dev.grindtools.entity.object.TierUpgrade;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TypeUpgradeAny extends TypeAbstractChoice<TierUpgrade> {
    private static final TypeUpgradeAny i = new TypeUpgradeAny();

    public static TypeUpgradeAny get() {
        return i;
    }

    public TypeUpgradeAny() {
        super(TierUpgrade.class);
    }

    public String getName() {
        return "text";
    }

    public TierUpgrade read(String arg, CommandSender sender) {
        return UpgradeConfig.get().tiers.stream().filter(tierUpgrade -> tierUpgrade.getId().equalsIgnoreCase(arg)).findFirst().orElse(null);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return UpgradeConfig.get().tiers.stream().map(TierUpgrade::getId).collect(Collectors.toList());
    }
}
