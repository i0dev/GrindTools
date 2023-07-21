package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.entity.LootTableConf;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;

public class CmdGrindToolsFishingRegionList extends GrindToolsCommand {

    public CmdGrindToolsFishingRegionList() {
        this.setVisibility(Visibility.SECRET);
    }

    @SneakyThrows
    @Override
    public void perform() {

        msg("<green>Fishing regions list:");
        LootTableConf.get().fishingRegions.forEach(fishingRegion -> {

            TextComponent component = new TextComponent(Utils.prefixAndColor("&fname: %region% &f- world: &7%world% &f- loot table: %loottable% &f- coords: (&7%x1%,%y1%,%z1%&f) &f(&7%x2%,%y2%,%z2%7&f)")
                    .replace("%x1%", String.valueOf(fishingRegion.getCuboid().xMin))
                    .replace("%y1%", String.valueOf(fishingRegion.getCuboid().yMin))
                    .replace("%z1%", String.valueOf(fishingRegion.getCuboid().zMin))
                    .replace("%x2%", String.valueOf(fishingRegion.getCuboid().xMax))
                    .replace("%y2%", String.valueOf(fishingRegion.getCuboid().yMax))
                    .replace("%z2%", String.valueOf(fishingRegion.getCuboid().zMax))
                    .replace("%world%", fishingRegion.getCuboid().world.getName())
                    .replace("%loottable%", fishingRegion.getLootTable())
                    .replace("%region%", fishingRegion.getName()));

            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Utils.prefixAndColor("&7Click to teleport to this fishing region."))}));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/grindtools fishingregion tp " + fishingRegion.getName()));
            me.spigot().sendMessage(component);
        });
    }
}
