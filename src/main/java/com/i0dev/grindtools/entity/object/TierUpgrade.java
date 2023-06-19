package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TierUpgrade {

    String id;

    Material material;
    String displayName;
    List<String> lore;
    boolean glow;

    List<String> applicableTiers;

    List<Tools> applicableTools;

    String tierToUpgradeTo;


    public ItemStack getItemStack() {
        ItemStack item = new ItemBuilder(material)
                .name(displayName)
                .lore(itemLore(lore))
                .addGlow(glow);

        GrindToolBuilder.applyTag(item, "tier-upgrade-item", UUID.randomUUID().toString());
        GrindToolBuilder.applyTag(item, "tier-upgrade-" + id, "true");

        return item;
    }

    public List<String> itemLore(List<String> lore) {
        List<String> newLore = new ArrayList<>(lore);
        newLore.replaceAll(s -> s
                .replace("%appliesTo%", applicableTools.toString()));
        return newLore;
    }

}
