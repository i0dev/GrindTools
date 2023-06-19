package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemConfig {

    public Material material;
    public String displayName;
    public List<String> lore;
    public boolean glow;

}
