package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.TechChipConfig;
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
public class TechChipConfigEntry {

    Material material;
    String displayName;
    String description;
    boolean glow;
    List<Tools> applicableTools;

    List<MultiplierLevel> levels;
    int maxLevel;

    public ItemStack getItemStack(String id, int level) {
        ItemStack item = new ItemBuilder(material)
                .name(displayName.replace("%level%", String.valueOf(level)))
                .lore(itemLore(TechChipConfig.get().getItemStackLore()))
                .addGlow(glow);

        GrindToolBuilder.applyTag(item, "techchip-item", UUID.randomUUID().toString());
        GrindToolBuilder.applyTag(item, "techchip-" + id, String.valueOf(level));

        return item;
    }

    public ItemStack getUpgradeItemStack(int level, int price) {
        List<String> lore;
        Material material = this.material;
        if (level == 0) {
            lore = itemLore(TechChipConfig.get().getUpgradeLevel0Lore());
            material = TechChipConfig.get().getUpgradeLevel0Material();
        } else if (level >= maxLevel) {
            lore = itemLore(TechChipConfig.get().getMaxLevelUpgradeLore());
        } else {
            lore = upgradeLore(TechChipConfig.get().getUpgradeItemLore(), level, price);
        }

        return new ItemBuilder(material)
                .amount(level == 0 ? 1 : level)
                .name(displayName.replace("%level%", String.valueOf(level)))
                .lore(lore)
                .addGlow(glow);
    }

    public List<String> upgradeLore(List<String> lore, int level, int price) {
        List<String> newLore = new ArrayList<>(lore);
        newLore.replaceAll(s -> s
                .replace("%price%", String.valueOf(price))
                .replace("%nextlevel%", String.valueOf(level + 1))
                .replace("%level%", String.valueOf(level)));
        return itemLore(newLore);
    }


    public List<String> itemLore(List<String> lore) {
        List<String> newLore = new ArrayList<>(lore);
        newLore.replaceAll(s -> s
                .replace("%description%", description)
                .replace("%appliesTo%", applicableTools.toString()));
        return newLore;
    }


}
