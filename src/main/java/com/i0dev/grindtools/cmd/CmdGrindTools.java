package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.Perm;
import com.i0dev.grindtools.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdGrindTools extends GrindToolsCommand {

    private static CmdGrindTools i = new CmdGrindTools();

    public static CmdGrindTools get() {
        return i;
    }

    public CmdGrindToolsGive cmdGrindToolsGive = new CmdGrindToolsGive();
    public CmdGrindToolsFishingRegion cmdGrindToolsFishingRegion = new CmdGrindToolsFishingRegion();
    public CmdGrindToolsUpgrade cmdGrindToolsUpgrade = new CmdGrindToolsUpgrade();
    public CmdGrindToolsDebug cmdGrindToolsDebug = new CmdGrindToolsDebug();
    public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(GrindToolsPlugin.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));

    @Override
    public List<String> getAliases() {
        return MConf.get().aliasesGrindTools;
    }

}
