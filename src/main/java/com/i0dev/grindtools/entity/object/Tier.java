package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@AllArgsConstructor
public class Tier {

    // primary
    String id;
    int priority;

    // Item
    Material material;
    String displayName;
    String description;
    boolean glow;

    // modifiers
    double dropRatesMultiplier;
    double everythingMultiplier;

    // other
    boolean upgradeable;

    List<TechChips> defaultTechChips;

}
