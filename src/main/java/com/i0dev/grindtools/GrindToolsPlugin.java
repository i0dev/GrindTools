package com.i0dev.grindtools;

import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.integration.PlaceholderAPI;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class GrindToolsPlugin extends MassivePlugin {

    private static GrindToolsPlugin i;

    public GrindToolsPlugin() {
        GrindToolsPlugin.i = this;
    }

    public static GrindToolsPlugin get() {
        return i;
    }

    @Override
    public void onEnableInner() {
        this.activateAuto();
    }


    @Override
    public void onEnable() {
        super.onEnable();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
        }
    }

    @Override
    public List<Class<?>> getClassesActiveColls() {
        return new MassiveList<>(
                MConfColl.class,
                MLangColl.class,
                MPlayerColl.class,

                HoeConfigColl.class,
                LootTableConfColl.class,
                PickaxeConfigColl.class,
                RodConfigColl.class,
                SwordConfigColl.class,
                TechChipConfigColl.class,
                UpgradeConfigColl.class,
                OreConfigColl.class,
                OreDataColl.class
        );
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        return p instanceof WorldEditPlugin ? (WorldEditPlugin) p : null;
    }

}