package com.i0dev.grindtools.entity.object;

import org.bukkit.Material;

import java.util.List;

public class TierUpgrade {

    Material material;
    String displayName;
    List<String> lore;
    boolean glow;

    List<Tools> applicableTools;

    String tierToUpgradeTo;

}
