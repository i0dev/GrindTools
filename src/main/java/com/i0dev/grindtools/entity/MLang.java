package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
@EditorName("config")
public class MLang extends Entity<MLang> {

    protected static transient MLang i;

    public static MLang get() {
        return i;
    }

    String prefix = "&8[&6GrindTools&8]&7";

    // Flux
    String amountMustBePositive = "%prefix% &cAmount must be positive.";
    String addedFlux = "%prefix% &aAdded &f%amount% &aflux to &f%player%.";
    String yourFluxBalance = "%prefix% &aYour flux balance is &f%amount%.";
    String playersFluxBalance = "%prefix% &a%player%'s flux balance is &f%amount%.";
    String dontHaveEnoughFlux = "%prefix% &cYou don't have enough flux.";
    String youPaidFlux = "%prefix% &aYou paid &f%amount% &aflux to &f%player%.";
    String youReceivedFlux = "%prefix% &aYou received &f%amount% &aflux from &f%player%.";
    String amountMustBe = "%prefix% &cAmount must be less than or equal to &f%amount%&c.";
    String removedFlux = "%prefix% &aRemoved &f%amount% &aflux from &f%player%.";
    String setFlux = "%prefix% &aSet &f%player%'s &aflux to &f%amount%.";
    String notEnoughFluxBalance = "%prefix% you don't have enough flux.";
    String boughtFluxItem = "%prefix% &aYou bought %item% for &f%price% &aflux.";

    // GrindTools
    String makeSelectionWithWorldEdit = "%prefix% &cMake a selection with WorldEdit.";

    // Ores
    String removedOres = "%prefix% &aRemoved &f%amount% &aores from from storage.";
    String replacedOres = "%prefix% &aReplaced &f%amount% blocks with &f%ore%&a.";

    // Fishing Region
    String fishingRegionAlreadyExists = "%prefix% &cYou already have a fishing region with that name.";
    String fishingRegionCreated = "%prefix% &aCreated fishing region &f%name%&a.";
    String fishingRegionDoesntExist = "%prefix% &cYou don't have a fishing region with that name.";
    String fishingRegionRemoved = "%prefix% &aDeleted fishing region &f%name%&a.";
    String fishingRegionTeleported = "%prefix% &aYou were teleported to fishing region &f%name%&a.";

    // TechChip
    String techChipDoesntExist = "%prefix% &cTech chip with that name doesn't exist.";
    String techChipMaxLevel = "%prefix% &cTech chip is over max level.";
    String gaveTechChip = "%prefix% &aGave &f%player% &a%amount% &f%techChip% &atech chip(s) of level &f%level%&a.";

    // Tool
    String tierDoesntExist = "%prefix% &cTier with that name doesn't exist.";
    String gaveTool = "%prefix% &aGave &f%player% a &f%tier% &afor %tool%.";
    String cantCraftGrindTool = "&cYou can't craft with Grind Tools in the crafting matrix.";

    // Upgrade
    String upgradeDoesntExist = "%prefix% &cUpgrade with that name doesn't exist.";
    String gaveUpgrade = "%prefix% &aGave &f%player% &a%amount% &f%upgrade% &aupgrade(s).";
    String upgradeToolNotHolding = "%prefix% &cYou must be holding a grind tool.";
    String toolNotUpgradable = "%prefix% &cThis tool is not upgradable.";
    String notEnoughMoneyToUpgrade = "%prefix% &cYou don't have enough money to upgrade this tech chip.";
    String maxTechChipLevel = "%prefix% &cThis tech chip is already at max level.";
    String upgradedTechChip = "%prefix% &aUpgraded tech chip &f%techChip% &ato level &f%level%&a.";

    // Engines
    String canOnlyBreakCaneWithHoe = "%prefix% &cYou can only break sugar cane with a grind hoe.";
    String youFoundATreasure = "%prefix% &aYou found a treasure!";
    String youExtractedSpecialItem = "%prefix% &aYou extracted a special item!";
    String inventoryFull = "%prefix% &cYour inventory is full. Dropping items on the ground.";
    String needGrindPickToMine = "%prefix% &cYou need a grind pickaxe to mine this block.";
    String canOnlyUseGrindPickOnGrindOres = "%prefix% &cYou can only use a grind pickaxe on grind ores.";
    String blockRegenerating = "%prefix% &aBlock is regenerating... please wait.";
    String blockNotFullyGrown = "%prefix% &cBlock is not fully grown.";
    String canOnlyFishInSpecificRegions = "%prefix% &cYou can only fish in specific regions.";
    String grindSwordNoEffect = "%prefix% &cThis grind sword has no effect on this mob.";
    String notGrindTool = "%prefix% &cThis is not a grind tool.";
    String canOnlyApplyTechChipTo = "%prefix% &cYou can only apply this tech chip to %tools%.";
    String techChipAlreadyApplied = "%prefix% &cThis tech chip is already applied to this tool. Upgrade with &c/upgrade&f.";
    String appliedTechChip = "%prefix% &aApplied tech chip &f%techChip% &ato your tool.";
    String canOnlyApplyTierUpgradeTo = "%prefix% &cYou can only apply this tier upgrade to %tools%.";
    String toolAlreadyMaxTier = "%prefix% &cThis tool is already at max tier.";
    String appliedToolUpgrade = "%prefix% &aApplied tier upgrade &f%tier% &ato your tool.";

    // Extract shop
    String extractShopRefreshedAnnouncement = "%prefix% &aExtract shop has been refreshed.";
    String notEnoughExtractBalance = "%prefix% &cYou don't have enough extract points in your inventory to buy this.";
    String reachedExtractBalanceLimit = "%prefix% &cYou have reached the extract balance limit.";
    String boughtExtractItem = "%prefix% &aBought &a%item% with your extract points.";


    @Override
    public MLang load(MLang that) {
        super.load(that);
        return this;
    }
}
