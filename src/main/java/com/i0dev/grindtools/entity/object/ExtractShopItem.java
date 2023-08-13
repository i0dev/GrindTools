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
public class ExtractShopItem {

    String id;
    Material material;
    String displayName;
    List<String> lore;
    int amount;
    int weight;
    boolean glow;
    List<String> commands;
    int limit;
    Map<String, Long> cost;


    public ItemStack getItemStack(Player observer) {
        List<String> newLore = MUtil.list();
        ItemBuilder itemBuilder = new ItemBuilder(material)
                .name(displayName)
                .addGlow(glow)
                .amount(amount);

        for (String line : lore) {
            if (line.contains("%balances%")) {
                cost.forEach((id, amount) -> newLore.add(
                        ExtractShopConf.get().getBalanceLoreFormat()
                                .replace("%id%", id)
                                .replace("%amount%", String.valueOf(amount))
                ));
            } else if (line.contains("%limit%")) {
                newLore.add(line.replace("%limit%", String.valueOf(limit - ExtractShopData.get().getPurchases(observer, id))));
            } else {
                newLore.add(line);
            }
        }
        itemBuilder.putLore(newLore);
        GrindToolBuilder.hideAllAttributes(itemBuilder);
        return itemBuilder;
    }


}
