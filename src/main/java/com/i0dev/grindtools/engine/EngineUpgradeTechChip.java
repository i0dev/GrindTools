package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.action.ActionChooseUpgrade;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.object.*;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.stream.IntStream;

public class EngineUpgrade extends Engine {

    private static final EngineUpgrade i = new EngineUpgrade();

    public static EngineUpgrade get() {
        return i;
    }

    private ChestGui getBasicChestGui(String title, int size) {
        Inventory inventory = Bukkit.createInventory(null, size, Txt.parse(title));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        UpgradeConfig cnf = MConf.get().upgradeConfig;

        IntStream.range(0, chestGui.getInventory().getSize()).forEach(i -> chestGui.getInventory().setItem(i, new ItemBuilder(cnf.borderMaterial)
                .amount(1)
                .name(cnf.borderName)
                .lore(cnf.borderLore)
                .addGlow(cnf.borderGlow)
        ));

        return chestGui;
    }

    public Inventory getUpgradeInventory(ItemStack itemInHand, Tools type) {
        UpgradeConfig cnf = MConf.get().upgradeConfig;

        String tite;
        switch (type) {
            case HOE -> tite = cnf.hoeTitle;
            case ROD -> tite = cnf.rodTitle;
            case SWORD -> tite = cnf.swordTitle;
            case PICKAXE -> tite = cnf.pickaxeTitle;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

        ChestGui chestGui = getBasicChestGui(tite, 45);

        chestGui.getInventory().setItem(20, itemInHand);

        TechChipConfig chipConfig = MConf.get().techChipConfig;

        PersistentDataContainer pdc = itemInHand.getItemMeta().getPersistentDataContainer();

        for (TechChips value : TechChips.values()) {
            TechChipConfigEntry techChipConfigEntry = chipConfig.getTechChipConfigById(value.name());

            if (!techChipConfigEntry.getApplicableTools().contains(type)) break;

            int level = Integer.parseInt(pdc.getOrDefault(GrindToolBuilder.getKey("techchip-" + value.name()), PersistentDataType.STRING, "0"));
            int price = level >= techChipConfigEntry.getMaxLevel() ? 0 : techChipConfigEntry.getLevels().stream().filter(lvl -> lvl.getLevel() == level + 1).findFirst().get().getPrice();
            ItemStack itemStack = techChipConfigEntry.getUpgradeItemStack(level, price);
            chestGui.getInventory().setItem(value.getUpgradeSlot(), itemStack);

            if (level != 0 && level <= techChipConfigEntry.getMaxLevel()) {
                chestGui.setAction(value.getUpgradeSlot(), new ActionChooseUpgrade(itemInHand, value, level, price));
            }
        }

        return chestGui.getInventory();
    }

}
