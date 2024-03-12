package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class AdvancedItemConfig extends ItemConfig {

    int weight;
    int minAmount;
    int maxAmount;
    List<String> commands;
    boolean dropItemStack;
    String extractId = null;
    List<String> extraMetadata = null;

    public AdvancedItemConfig(Material material, String displayName, List<String> lore, boolean glow, int weight, int minAmount, int maxAmount, boolean dropItemStack, List<String> commands) {
        super(material, displayName, lore, glow);
        this.weight = weight;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.dropItemStack = dropItemStack;
        this.commands = commands;
    }

    public AdvancedItemConfig(Material material, String displayName, List<String> lore, boolean glow, int weight, int minAmount, int maxAmount, boolean dropItemStack, List<String> commands, List<String> extraMetadata) {
        super(material, displayName, lore, glow);
        this.weight = weight;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.dropItemStack = dropItemStack;
        this.commands = commands;
        this.extraMetadata = extraMetadata;
    }


    public ItemStack getItemStack() {
        int amount = ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);

        ItemBuilder builder = new ItemBuilder(material)
                .amount(amount)
                .name(displayName)
                .putLore(lore)
                .addGlow(glow);

        if (extractId != null) builder.addPDCValue("extract-id", extractId);

        if (extraMetadata != null) {
            for (String metadata : extraMetadata) {
                String[] split = metadata.split(":");
                builder.addPDCValue(split[0], split[1]);
            }
        }
        return builder;
    }


}
