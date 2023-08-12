package com.i0dev.grindtools.cmd;

import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdExtract extends GrindToolsCommand {

    public static CmdExtract i = new CmdExtract();

    public static CmdExtract get() {
        return i;
    }


    public CmdExtractShop cmdExtractShop = new CmdExtractShop();
    public CmdExtractRefreshShop cmdExtractRefreshShop = new CmdExtractRefreshShop();


    @Override
    public List<String> getAliases() {
        return MUtil.list("extract");
    }

    @Override
    public String getDesc() {
        return "extract commands.";
    }

}
