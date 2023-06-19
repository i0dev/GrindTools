package com.i0dev.globalcurrency.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class MLang extends Entity<MLang> {

    protected static transient MLang i;

    public static MLang get() {
        return i;
    }

    public String prefix = "&8[&bGlobalCurrency&8]&7";
    public String addedToPlayer = "%prefix% &aAdded &f%amount% &ato &f%player%&a's balance";
    public String removedFromPlayer = "%prefix% &cRemoved &f%amount% &cfrom &f%player%&c's balance";
    public String setPlayerBalance = "%prefix% &aSet &f%player%&a's balance to &f%amount%";
    public String yourBalance = "%prefix% &aYour balance is &f%amount%";
    public String playerBalance = "%prefix% &a%player%'s balance is &f%amount%";
    public String notEnoughMoney = "%prefix% &cYou do not have enough money to purchase this, your balance is &f%amount%";
    public String youBought = "%prefix% &aYou have purchased %item% for &f%amount% medals";
    public String canOnlyUsePositiveNumbers = "%prefix% &cYou can only use positive numbers";
    public String cantRemoveMoreThanPlayerHas = "%prefix% &cYou can't remove more than the player has, they only have &f%amount%";

    @Override
    public MLang load(MLang that) {
        super.load(that);
        return this;
    }
}
