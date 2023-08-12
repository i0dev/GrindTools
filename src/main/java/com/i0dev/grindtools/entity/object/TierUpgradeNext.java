package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TierUpgradeNext {

    String id;

    Material material;
    String displayName;
    List<String> lore;
    boolean glow;

    List<Tools> applicableTools;


    public ItemStack getItemStack() {
        ItemStack item = new ItemBuilder(material)
                .name(displayName)
                .lore(itemLore(lore))
                .addGlow(glow);


        GrindToolBuilder.hideAllAttributes(item);


        GrindToolBuilder.applyTag(item, "tier-upgrade-item-next", UUID.randomUUID().toString());
        GrindToolBuilder.applyTag(item, "tier-upgrade-next-" + id, "true");

        return item;
    }

    public List<String> itemLore(List<String> lore) {
        List<String> newLore = new ArrayList<>(lore);
        newLore.replaceAll(s -> s
                .replace("%appliesTo%", applicableTools.toString()));
        return newLore;
    }


}
