package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LootTable {

    String id;
    List<AdvancedItemConfig> weightedItems;

}
