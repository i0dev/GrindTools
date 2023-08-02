package com.i0dev.grindtools.action;

import com.i0dev.grindtools.cmd.CmdGrindToolsUpgrade;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.TechChips;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
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
        return TechChipConfig.get().getTechChipConfigById(techChip.getId());
    }

    @Override
    public boolean onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return false;
        TechChipConfigEntry cnf = getCnf();

        if (cnf == null) {
            Utils.msg(player, MLang.get().techChipDoesntExist);
            return false;
        }

        MPlayer mPlayer = MPlayer.get(player);

        if (mPlayer.getCurrency() < price) {
            Utils.msg(player, MLang.get().notEnoughMoneyToUpgrade);
            return false;
        }

        if (currentLevel + 1 > cnf.getMaxLevel()) {
            Utils.msg(player, MLang.get().maxTechChipLevel);
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
                meta.setLore(GrindToolBuilder.formatLore(HoeConfig.get().getLoreFormat(), tool));
            }
            case PICKAXE -> {
                meta.setLore(GrindToolBuilder.formatLore(PickaxeConfig.get().getLoreFormat(), tool));
            }
            case SWORD -> {
                meta.setLore(GrindToolBuilder.formatLore(SwordConfig.get().getLoreFormat(), tool));
            }
            case ROD -> {
                meta.setLore(GrindToolBuilder.formatLore(RodConfig.get().getLoreFormat(), tool));
            }
        }

        tool.setItemMeta(meta);

        mPlayer.setCurrency(mPlayer.getCurrency() - price);
        Utils.msg(player, MLang.get().upgradedTechChip,
                new Pair<>("%techChip%", techChip.getId()),
                new Pair<>("%level%", String.valueOf(currentLevel + 1))
        );

        CmdGrindToolsUpgrade.get().execute(player, List.of());

        return true;
    }
}
