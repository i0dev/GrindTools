package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.Perm;
import com.i0dev.grindtools.action.ActionBuyFluxShop;
import com.i0dev.grindtools.engine.EngineUpgradeTechChip;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.FluxShopItem;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;

public class CmdFluxShop extends GrindToolsCommand {

    public CmdFluxShop() {
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    protected <T extends Enum<T>> T calcPerm() {
        return (T) Perm.FLUX_SHOP;
    }

    @Override
    public void perform() throws MassiveException {
        reopenInventory(me);
    }


    public static Inventory getInventory(Player player) {
        ChestGui chestGui = EngineUpgradeTechChip.get().getBasicChestGui(FluxConf.get().getFluxShopInventoryTitle(), FluxConf.get().getFluxShopInventorySize());

        ItemStack balanceInfoItem = FluxConf.get().getFluxShopBalanceItem().getItemStack();
        ItemMeta meta = balanceInfoItem.getItemMeta();
        meta.setDisplayName(Utils.color(meta.getDisplayName().replace("%balance%", NumberFormat.getInstance().format(MPlayer.get(player).getCurrency()))));
        balanceInfoItem.setItemMeta(meta);
        chestGui.getInventory().setItem(FluxConf.get().getFluxShopBalanceItemSlot(), balanceInfoItem);

        for (FluxShopItem item : FluxConf.get().getShopItems()) {
            chestGui.getInventory().setItem(item.getSlot(), item.getItemStack());
            chestGui.setAction(item.getSlot(), new ActionBuyFluxShop(item));
        }

        return chestGui.getInventory();
    }

    public static void reopenInventory(Player player) {
        player.openInventory(getInventory(player));
    }

}
