package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemConfig {

    public Material material;
    public String displayName;
    public List<String> lore;
    public boolean glow;


    public ItemStack getItemStack() {
        return new ItemBuilder(material)
                .name(displayName)
                .addGlow(glow)
                .lore(lore);
    }

}
