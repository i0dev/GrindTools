package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdGlobalCurrency extends GlobalCurrencyCommand {

    private static CmdGlobalCurrency i = new CmdGlobalCurrency();

    public CmdGlobalCurrencyAdd cmdGlobalCurrencyAdd = new CmdGlobalCurrencyAdd();
    public CmdGlobalCurrencyBalance cmdGlobalCurrencyBalance = new CmdGlobalCurrencyBalance();
    public CmdGlobalCurrencyRemove cmdGlobalCurrencyRemove = new CmdGlobalCurrencyRemove();
    public CmdGlobalCurrencySet cmdGlobalCurrencySet = new CmdGlobalCurrencySet();
    public CmdGlobalCurrencyShop cmdGlobalCurrencyShop = new CmdGlobalCurrencyShop();
    public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(GlobalCurrencyPlugin.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));

    public static CmdGlobalCurrency get() {
        return i;
    }

    @Override
    public List<String> getAliases() {
        return MConf.get().aliasesGlobalCurrency;
    }

}
