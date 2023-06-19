package com.i0dev.grindtools.entity.object;

import lombok.Getter;

@Getter
public enum TechChips {

    AUTO_SELL("Auto Sell", 14),
    SOULBOUND("Soulbound", 15),
    TREASURE_HUNTER("Treasure Hunter", 16),
    DROP_BOOST("Drop Boost", 23),
    TOKEN_BOOST("Token Boost", 24),
    EXP_BOOST("Exp Boost", 25),
    EXTRACT("Extract", 32),
    LURE("Lure", 33),
    DAMAGE("Damage", 34);


    private final String displayName;
    private final int upgradeSlot;

    TechChips(String displayName, int upgradeSlot) {
        this.displayName = displayName;
        this.upgradeSlot = upgradeSlot;
    }

    public String getId() {
        return this.name().toLowerCase();
    }

}
