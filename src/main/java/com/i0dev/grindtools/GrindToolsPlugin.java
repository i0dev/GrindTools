package com.i0dev.grindtools;

import com.i0dev.grindtools.entity.MConfColl;
import com.i0dev.grindtools.entity.MLangColl;
import com.i0dev.grindtools.entity.MPlayerColl;
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
    public List<Class<?>> getClassesActiveColls() {
        return new MassiveList<>(
                MConfColl.class,
                MLangColl.class,
                MPlayerColl.class
        );
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        return p instanceof WorldEditPlugin ? (WorldEditPlugin) p : null;
    }

}