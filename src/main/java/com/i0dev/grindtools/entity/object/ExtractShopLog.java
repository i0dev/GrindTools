package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ExtractShopLog {

    UUID playerUUID;
    String id;

}
