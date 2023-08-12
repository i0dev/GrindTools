package com.i0dev.grindtools.entity.object;

import com.massivecraft.massivecore.util.MUtil;
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
    List<String> description;
    boolean glow;

    // modifiers
    double dropRatesMultiplier;
    double everythingMultiplier;

    // other
    boolean upgradeable;

    List<TechChips> defaultTechChips;


    public Tier(String id, int priority, Material material, String displayName, String description, boolean glow, double dropRatesMultiplier, double everythingMultiplier, boolean upgradeable, List<TechChips> defaultTechChips) {
        this.id = id;
        this.priority = priority;
        this.material = material;
        this.displayName = displayName;
        this.glow = glow;
        this.dropRatesMultiplier = dropRatesMultiplier;
        this.everythingMultiplier = everythingMultiplier;
        this.upgradeable = upgradeable;
        this.defaultTechChips = defaultTechChips;
        this.description = MUtil.list(description);
    }
}
