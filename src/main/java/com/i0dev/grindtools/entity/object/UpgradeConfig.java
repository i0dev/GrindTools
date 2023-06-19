package com.i0dev.grindtools.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class UpgradeConfig {

    public String hoeTitle = "&c&lUpgrade your Hoe";
    public String swordTitle = "&c&lUpgrade your Sword";
    public String pickaxeTitle = "&c&lUpgrade your Pickaxe";
    public String rodTitle = "&c&lUpgrade your Fishing Rod";

    public Material borderMaterial = Material.BLACK_STAINED_GLASS_PANE;
    public String borderName = "&f";
    public List<String> borderLore = MUtil.list();
    public boolean borderGlow = true;


}
