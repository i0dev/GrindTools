package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.entity.MPlayer;
import com.i0dev.grindtools.entity.MPlayerColl;
import com.i0dev.grindtools.util.ItemBuilder;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CmdGrindToolsDebug extends GrindToolsCommand {

    public CmdGrindToolsDebug() {
        this.addParameter(TypeString.get(), "arg1", "arg1");
        this.addRequirements(RequirementIsPlayer.get());
    }

    @SneakyThrows
    @Override
    public void perform() {
        String arg1 = this.readArg("arg1");

        switch (arg1) {
            case "arg1" -> {
                ItemMeta meta = me.getItemInHand().getItemMeta();
                PersistentDataContainer PDC = meta.getPersistentDataContainer();
                PDC.getKeys().forEach(namespacedKey -> {
                    me.sendMessage(namespacedKey.getKey() + " = " + PDC.get(namespacedKey, PersistentDataType.STRING));
                });
                break;
            }
            case "1" -> {
                MPlayer mPlayer = MPlayerColl.get().get(me);
                mPlayer.setCurrency(mPlayer.getCurrency() + 100);
                mPlayer.msg("You now have " + mPlayer.getCurrency() + " currency");
                break;
            }
            case "2" -> {
                me.getInventory().addItem(new ItemBuilder(Material.REDSTONE).name("Creeper Extract").addPDCValue("extract-id", "creeper").amount(5));
                me.getInventory().addItem(new ItemBuilder(Material.GLOWSTONE).name("Zombie Extract").addPDCValue("extract-id", "zombie").amount(5));
            }
        }


    }
}
