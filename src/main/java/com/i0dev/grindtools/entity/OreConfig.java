package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@EditorName("config")
public class OreConfig extends Entity<OreConfig> {

    protected static transient OreConfig i;

    public static OreConfig get() {
        return i;
    }

    public Ore getOreById(String id) {
        return ores.stream().filter(ore -> ore.getId().equals(id)).findFirst().orElse(null);
    }


    public List<Ore> ores = MUtil.list(
            new Ore(
                    "iron_ore",
                    3,
                    Material.IRON_ORE,
                    false,
                    60000,
                    120000,
                    MUtil.list(
                            new AdvancedItemConfig(Material.IRON_INGOT, "Iron Ingot", MUtil.list(), false, -1, 1, 3, true, MUtil.list())
                    ),
                    1,
                    "fishing1",
                    Material.DEEPSLATE,
                    true,
                    false
            )
    );

    @Override
    public OreConfig load(OreConfig that) {
        super.load(that);
        return this;
    }

}
