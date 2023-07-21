package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.EntityType;

@AllArgsConstructor
@Data
public class MobTypeLootTable {

    EntityType entity;
    String lootTable;


}
