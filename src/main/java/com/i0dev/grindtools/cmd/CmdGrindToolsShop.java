package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.cmd.type.TypeSellShop;
import com.i0dev.grindtools.engine.EngineSell;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.OreData;
import com.i0dev.grindtools.entity.SellShop;
import com.i0dev.grindtools.entity.SellShopColl;
import com.i0dev.grindtools.util.Cuboid;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CmdGrindToolsShop extends GrindToolsCommand {

    public CmdGrindToolsShop() {
        this.setVisibility(Visibility.SECRET);
        this.addRequirements(RequirementIsPlayer.get());
        this.addParameter(TypeSellShop.get(), "shop");
    }

    @Override
    public void perform() throws MassiveException {
        SellShop shop = this.readArg();

        me.openInventory(EngineSell.get().getNewSellInventory(shop));
    }
}
