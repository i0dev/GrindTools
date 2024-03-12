package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.OreConfig;
import com.i0dev.grindtools.entity.SellShop;
import com.i0dev.grindtools.entity.SellShopColl;
import com.i0dev.grindtools.entity.object.Ore;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.stream.Collectors;

public class TypeSellShop extends TypeAbstractChoice<SellShop> {
    private static final TypeSellShop i = new TypeSellShop();

    public static TypeSellShop get() {
        return i;
    }

    public TypeSellShop() {
        super(SellShop.class);
    }

    public String getName() {
        return "text";
    }

    public SellShop read(String arg, CommandSender sender) {
        return SellShop.get(arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return SellShopColl.get().getAll().stream().map(SellShop::getId).collect(Collectors.toList());
    }
}
