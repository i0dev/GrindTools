package com.i0dev.grindtools.cmd.type;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.object.LootTable;
import com.i0dev.grindtools.entity.object.TechChips;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class TypeLootTable extends TypeAbstractChoice<LootTable> {
    private static final TypeLootTable i = new TypeLootTable();

    public static TypeLootTable get() {
        return i;
    }

    public TypeLootTable() {
        super(LootTable.class);
    }

    public String getName() {
        return "text";
    }

    public LootTable read(String arg, CommandSender sender) throws MassiveException {
        for (LootTable lootTable : MConf.get().lootTables) {
            if (lootTable.getId().equalsIgnoreCase(arg)) {
                return lootTable;
            }
        }
        throw new MassiveException().addMsg("<b>Unknown loot table <h>%s<b>.", arg);
    }

    public Collection<String> getTabList(CommandSender sender, String arg) {
        return MConf.get().lootTables.stream().map(LootTable::getId).collect(Collectors.toList());
    }
}
