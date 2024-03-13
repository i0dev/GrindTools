package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;

@Data
@AllArgsConstructor
public class WorldBreakingConfig {

    public String worldName;

    // If is true, then the block list will be a list of blocks allowed to mine, all others will be canceled
    // If is false, then the block list will be a list of blocks not allowed to mine, all others will be allowed
    public boolean allowedMiningMode;

    public List<Material> blockList;
    public boolean preventBreakingBottomCaneBlock;
    public String permissionBypass;
    public boolean preventPlacingAllBlocks;
}
