package com.i0dev.grindtools.action;

import com.i0dev.grindtools.cmd.CmdFluxShop;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.MPlayer;
import com.i0dev.grindtools.entity.object.FluxShopItem;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.chestgui.ChestAction;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@AllArgsConstructor
public class ActionBuyFluxShop implements ChestAction {

    FluxShopItem item;

    @Override
    public boolean onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MPlayer mPlayer = MPlayer.get(player);

        if (item.getPrice() > mPlayer.getCurrency()) {
            Utils.msg(player, MLang.get().notEnoughFluxBalance);
            return false;
        }

        MPlayer.get(player).setCurrency(mPlayer.getCurrency() - item.getPrice());

        Utils.runCommands(item.getCommands(), player);

        Utils.msg(player, MLang.get().boughtFluxItem,
                new Pair<>("%price%", String.valueOf(item.getPrice())),
                new Pair<>("%item%", item.getDisplayName())
        );

        CmdFluxShop.reopenInventory(player);
        return true;
    }


}
