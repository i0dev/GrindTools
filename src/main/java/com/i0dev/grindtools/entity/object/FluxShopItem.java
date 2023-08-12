package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.entity.ExtractShopConf;
import com.i0dev.grindtools.entity.ExtractShopData;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class FluxShopItem {

    String id;
    Material material;
    String displayName;
    List<String> lore;
    int amount;
    boolean glow;
    List<String> commands;
    long price;
    int slot;

    public ItemStack getItemStack() {
        List<String> newLore = MUtil.list();
        ItemBuilder itemBuilder = new ItemBuilder(material)
                .name(displayName)
                .addGlow(glow)
                .amount(amount);

        for (String line : lore) {
            newLore.add(line.replace("%cost%", String.valueOf(price)));
        }
        itemBuilder.lore(newLore);
        GrindToolBuilder.hideAllAttributes(itemBuilder);
        return itemBuilder;
    }


}
