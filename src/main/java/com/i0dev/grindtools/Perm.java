package com.i0dev.grindtools;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified {

    BASECOMMAND("basecommand"),

    DEBUG("debug"),

    EXTRACT("extract"),
    EXTRACT_SHOP("extract.shop"),
    EXTRACT_REFRESHSHOP("extract.refreshshop"),

    FLUX("flux"),
    FLUX_SET("flux.set"),
    FLUX_ADD("flux.add"),
    FLUX_PAY("flux.pay"),
    FLUX_SHOP("flux.shop"),
    FLUX_REMOVE("flux.remove"),
    FLUX_BALANCE("flux.balance"),
    FLUX_BALANCE_OTHERS("flux.balance.others"),

    REPLACE_ORE("replaceore"),
    CLEAR_ORE("clearore"),

    FISHING_REGION("fishingregion"),
    FISHING_REGION_CREATE("fishingregion.create"),
    FISHING_REGION_REMOVE("fishingregion.remove"),
    FISHING_REGION_LIST("fishingregion.list"),
    FISHING_REGION_TELEPORT("fishingregion.teleport"),
    GIVE("give"),
    GIVE_TOOL("give.tool"),
    GIVE_TECHCHIP("give.techchip"),
    GIVE_UPGRADE("give.upgrade"),
    GIVE_UPGRADE_ANY("give.upgrade.any"),
    GIVE_UPGRADE_NEXT("give.upgrade.next"),
    UPGRADE("upgrade"),

    VERSION("version");

    private final String id;

    Perm(String id) {
        this.id = "grindtools." + id;
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean has(Permissible permissible, boolean verboose) {
        return PermissionUtil.hasPermission(permissible, this, verboose);
    }

    public boolean has(Permissible permissible) {
        return PermissionUtil.hasPermission(permissible, this);
    }

}
