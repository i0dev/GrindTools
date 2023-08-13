package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class AdvancedItemConfig extends ItemConfig {

    int weight;
    int minAmount;
    int maxAmount;
    List<String> commands;
    boolean dropItemStack;
    String extractId = null;

    public AdvancedItemConfig(Material material, String displayName, List<String> lore, boolean glow, int weight, int minAmount, int maxAmount, boolean dropItemStack, List<String> commands) {
        super(material, displayName, lore, glow);
        this.weight = weight;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.dropItemStack = dropItemStack;
        this.commands = commands;
    }


    public ItemStack getItemStack() {
        int amount = (int) (Math.random() * (maxAmount - minAmount) + minAmount);

        ItemBuilder builder = new ItemBuilder(material)
                .amount(amount)
                .name(displayName)
                .putLore(lore)
                .addGlow(glow);

        if (extractId != null) builder.addPDCValue("extract-id", extractId);
        return builder;
    }


}
