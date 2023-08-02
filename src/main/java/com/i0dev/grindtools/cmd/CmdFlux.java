package com.i0dev.grindtools.cmd;

import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdFlux extends GrindToolsCommand {

    private static CmdFlux i = new CmdFlux();

    public static CmdFlux get() {
        return i;
    }

    public CmdFluxAdd cmdFluxAdd = new CmdFluxAdd();
    public CmdFluxRemove cmdFluxRemove = new CmdFluxRemove();
    public CmdFluxSet cmdFluxSet = new CmdFluxSet();
    public CmdFluxPay cmdFluxPay = new CmdFluxPay();
    public CmdFluxBalance cmdFluxBalance = new CmdFluxBalance();


    @Override
    public List<String> getAliases() {
        return MUtil.list("flux");
    }

    @Override
    public String getDesc() {
        return "Manage flux.";
    }
}
