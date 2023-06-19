package com.i0dev.grindtools.action;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.cmd.CmdGrindToolsUpgrade;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.MPlayer;
import com.i0dev.grindtools.entity.object.TechChipConfig;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.TechChips;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.massivecraft.massivecore.chestgui.ChestAction;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@AllArgsConstructor
public class ActionChooseUpgrade implements ChestAction {

    ItemStack tool;
    TechChips techChip;
    int currentLevel;
    int price;

    private TechChipConfigEntry getCnf() {
        return MConf.get().techChipConfig.getTechChipConfigById(techChip.getId());
    }

    @Override
    public boolean onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return false;
        TechChipConfigEntry cnf = getCnf();

        if (cnf == null) {
            player.sendMessage("Tech chip config entry for " + techChip.name() + " not found.");
            return false;
        }

        MPlayer mPlayer = MPlayer.get(player);

        if (mPlayer.getCurrency() < price) {
            player.sendMessage("You don't have enough money to upgrade this tech chip.");
            return false;
        }

        if (currentLevel + 1 > cnf.getMaxLevel()) {
            player.sendMessage("You have reached the maximum level for this tech chip.");
            return false;
        }

        ItemMeta meta = tool.getItemMeta();
        PersistentDataContainer PDC = meta.getPersistentDataContainer();

        PDC.set(GrindToolBuilder.getKey("techchip-" + techChip.getId()), PersistentDataType.STRING, String.valueOf(currentLevel + 1));
        tool.setItemMeta(meta);

        meta = tool.getItemMeta();

        Tools type = Tools.valueOf(PDC.get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING));

        switch (type) {
            case HOE -> {
                meta.setLore(GrindToolBuilder.formatLore(MConf.get().hoeConfig.getLoreFormat(), tool));
            }
            case PICKAXE -> {
                meta.setLore(GrindToolBuilder.formatLore(MConf.get().pickaxeConfig.getLoreFormat(), tool));
            }
            case SWORD -> {
                meta.setLore(GrindToolBuilder.formatLore(MConf.get().swordConfig.getLoreFormat(), tool));
            }
            case ROD -> {
                meta.setLore(GrindToolBuilder.formatLore(MConf.get().rodConfig.getLoreFormat(), tool));
            }
        }

        tool.setItemMeta(meta);

        mPlayer.setCurrency(mPlayer.getCurrency() - price);
        player.sendMessage("You have successfully upgraded your " + techChip.getId() + " chip to level " + (currentLevel + 1) + ".");

        CmdGrindToolsUpgrade.get().execute(player, List.of());

        return true;
    }
}
