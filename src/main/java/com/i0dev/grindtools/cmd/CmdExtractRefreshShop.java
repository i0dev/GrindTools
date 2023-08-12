package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.Perm;
import com.i0dev.grindtools.task.TaskRefreshExtractShop;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Bukkit;

public class CmdExtractRefreshShop extends GrindToolsCommand {

    public CmdExtractRefreshShop() {
        this.addAliases("refreshShop");
    }

    @Override
    protected <T extends Enum<T>> T calcPerm() {
        return (T) Perm.EXTRACT_REFRESHSHOP;
    }

    @Override
    public void perform() throws MassiveException {
        TaskRefreshExtractShop.get().refreshShop();
    }

}
