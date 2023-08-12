package com.i0dev.grindtools.util;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.*;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GrindToolBuilder {

    public static ItemStack buildNewItem(Tier tier, Tools type) {
        return applyTier(new ItemBuilder(tier.getMaterial()).name(tier.getDisplayName()), tier, type);
    }

    public static void hideAllAttributes(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    public static ItemStack applyTier(ItemStack item, Tier tier, Tools type) {
        hideAllAttributes(item);
        ItemMeta meta1 = item.getItemMeta();
        meta1.setDisplayName(Utils.color(tier.getDisplayName()));
        meta1.setUnbreakable(true);
        item.setItemMeta(meta1);

        item.setType(tier.getMaterial());
        if (tier.isGlow()) item.addUnsafeEnchantment(Glow.getGlow(), 1);


        applyTag(item, "tool-type", type.name());

        applyTag(item, "tier", tier.getId());
        applyTag(item, "upgradable", String.valueOf(tier.isUpgradeable()));

        applyTag(item, "modifier-dropRatesMultiplier", String.valueOf(tier.getDropRatesMultiplier()));
        applyTag(item, "modifier-everythingMultiplier", String.valueOf(tier.getEverythingMultiplier()));

        switch (type) {
            case HOE -> {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(HoeConfig.get().getLoreFormat(), item));
                item.setItemMeta(meta);
            }
            case PICKAXE -> {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(PickaxeConfig.get().getLoreFormat(), item));
                int efficiencyLevel = getEfficiencyLevel(item);
                if (efficiencyLevel != 0) {
                    meta.addEnchant(Enchantment.DIG_SPEED, efficiencyLevel, true);
                }
                item.setItemMeta(meta);
            }
            case ROD -> {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(RodConfig.get().getLoreFormat(), item));
                item.setItemMeta(meta);
            }
            case SWORD -> {
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(SwordConfig.get().getLoreFormat(), item));
                item.setItemMeta(meta);
            }
        }
        return item;
    }


    // replaces the lore placeholders with values from the persistent data container
    public static List<String> formatLore(List<String> lore, ItemStack item) {
        List<String> newLore = new ArrayList<>(lore);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        Tools type = Tools.valueOf(pdc.get(getKey("tool-type"), PersistentDataType.STRING));

        List<String> descriptionReplacedLore = new ArrayList<>();
        String tier = pdc.get(getKey("tier"), PersistentDataType.STRING);
        for (String s : newLore) {
            if (s.contains("%description%")) {
                List<String> description = new ArrayList<>();
                switch (type) {
                    case HOE -> description = HoeConfig.get().getFromId(tier).getDescription();
                    case PICKAXE -> description = PickaxeConfig.get().getFromId(tier).getDescription();
                    case ROD -> description = RodConfig.get().getFromId(tier).getDescription();
                    case SWORD -> description = SwordConfig.get().getTierFromId(tier).getDescription();
                }
                description.forEach(line -> descriptionReplacedLore.add(Utils.color(line)));
            } else descriptionReplacedLore.add(s);
        }
        newLore = descriptionReplacedLore;

        TechChipConfig cnf = TechChipConfig.get();

        double tokenBoost = Math.round((getCurrencyModifier(item)) * 1000.0) / 1000.0;
        double expBoost = Math.round(getExpModifier(item) * 100.0) / 100.0;
        double dropBoost = Math.round(getDropModifierDefault(item) * 100.0) / 100.0;
        double treasureHunter = 100 * Math.round(getTreasureHunterPercent(item) * 10000.0) / 10000.0;
        double extract = 100 * Math.round(getExtractPercent(item) * 1000.0) / 1000.0;
        double averageLure = Math.round(((double) (getLureMin(item) + getLureMax(item)) / 2 / 20) * 10.0) / 10.0;
        double damage = Math.round(getDamageModifier(item) * 10.0) / 10.0;
        int efficiency = getEfficiencyLevel(item);

        List<String> techChipsToAdd = new ArrayList<>();
        List<String> modifiersToAdd = new ArrayList<>();

        getAllTechChips(item).forEach((techChip, multiplierLevel) -> techChipsToAdd.add(Utils.color(MConf.get().getTechChipLoreFormat()
                .replace("%chip%", techChip.getDisplayName())
                .replace("%level%", multiplierLevel == null ? "1" : String.valueOf(multiplierLevel.getLevel()))
        )));

        if (tokenBoost != 0 && isAppliedToTool(type, cnf.token_boost)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatTokenBoost()
                    .replace("%amount%", String.valueOf(tokenBoost))
            ));
        }
        if (expBoost != 0 && isAppliedToTool(type, cnf.exp_boost)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatExpBoost()
                    .replace("%amount%", String.valueOf(expBoost))
            ));
        }
        if (dropBoost != 0 && isAppliedToTool(type, cnf.drop_boost)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatDropBoost()
                    .replace("%amount%", String.valueOf(dropBoost))
            ));
        }
        if (treasureHunter != 0 && isAppliedToTool(type, cnf.treasure_hunter)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatTreasureHunter()
                    .replace("%amount%", String.valueOf(treasureHunter))
            ));
        }
        if (extract != 0 && isAppliedToTool(type, cnf.extract)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatExtract()
                    .replace("%amount%", String.valueOf(extract))
            ));
        }
        if (isAppliedToTool(type, cnf.lure)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatLure()
                    .replace("%amount%", String.valueOf(averageLure))
            ));
        }
        if (damage != 0 && isAppliedToTool(type, cnf.damage)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatDamage()
                    .replace("%amount%", String.valueOf(damage))
            ));
        }
        if (efficiency != 0 && isAppliedToTool(type, cnf.efficiency)) {
            modifiersToAdd.add(Utils.color(MConf.get().getModifierFormatEfficiency()
                    .replace("%amount%", String.valueOf(efficiency))
            ));
        }

        List<String> toRemove = new ArrayList<>();
        List<String> toAdd = new ArrayList<>();

        for (String s : newLore) {
            if (s.equals("%tech-chips%")) {
                toRemove.add(s);
                if (!techChipsToAdd.isEmpty()) {
                    toAdd.add("");
                    toAdd.add(Utils.color("&b&lTech Chips"));
                    toAdd.addAll(techChipsToAdd);
                }
            }
            if (s.equals("%modifiers%")) {
                toRemove.add(s);
                if (!modifiersToAdd.isEmpty()) {
                    toAdd.add("");
                    toAdd.add(Utils.color("&9&lModifiers"));
                    toAdd.addAll(modifiersToAdd);
                }
            }
        }

        toRemove.forEach(newLore::remove);
        newLore.addAll(toAdd);

        return newLore;
    }


    public static boolean isAppliedToTool(Tools tool, TechChipConfigEntry configEntry) {
        return configEntry.getApplicableTools().contains(tool);
    }


    public static Map<TechChips, MultiplierLevel> getAllTechChips(ItemStack item) {
        Map<TechChips, MultiplierLevel> ret = new HashMap<>();

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.getKeys().stream().filter(namespacedKey -> namespacedKey.getKey().startsWith("techchip-")).forEach(namespacedKey -> {
            String name = namespacedKey.getKey().replace("techchip-", "");
            TechChips techChip = TechChips.valueOf(name.toUpperCase());
            int levelInt = Integer.parseInt(pdc.get(namespacedKey, PersistentDataType.STRING));
            MultiplierLevel level = TechChipConfig.get().getTechChipConfigById(techChip.getId()).getLevels().stream().filter(lvl -> lvl.getLevel() == levelInt).findFirst().orElseGet(null);
            ret.put(techChip, level);
        });
        return ret;
    }

    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static void applyTag(ItemStack item, String key, String value) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(getKey(key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(GrindToolsPlugin.get(), key);
    }

    // modifiers //

    private static PersistentDataContainer getPDC(ItemStack tool) {
        ItemMeta meta = tool.getItemMeta();
        return meta.getPersistentDataContainer();
    }

    private static double getEverythingMultiplier(ItemStack tool) {
        return Double.parseDouble(getPDC(tool).get(getKey("modifier-everythingMultiplier"), PersistentDataType.STRING));
    }


    public static double getDropModifierDefault(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        double dropRatesMultiplier = Double.parseDouble(getPDC(tool).get(getKey("modifier-dropRatesMultiplier"), PersistentDataType.STRING));
        int dropBoostLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-drop_boost"), PersistentDataType.STRING, "0"));

        return getEverythingMultiplier(tool) * (dropRatesMultiplier * dropBoostLevel == 0 ? 1 : cnf.drop_boost.getLevels().stream().filter(lvl -> lvl.getLevel() == dropBoostLevel).findFirst().orElseThrow().getMultiplier());
    }


    public static double getDropModifier(ItemStack tool) {

        double totalMultiplier = getDropModifierDefault(tool);

        // get the 0.XX of the dropRatesMultiplier
        double dropRatesMultiplierDecimal = totalMultiplier - Math.floor(totalMultiplier);
        double percentChanceToRoundUp = dropRatesMultiplierDecimal * 100;
        totalMultiplier = Math.floor(totalMultiplier) + (ThreadLocalRandom.current().nextInt(100) < percentChanceToRoundUp ? 1 : 0);

        return totalMultiplier;
    }

    public static double getCurrencyModifier(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int tokenBoostLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-token_boost"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (tokenBoostLevel == 0 ? 1 : cnf.token_boost.getLevels().stream().filter(lvl -> lvl.getLevel() == tokenBoostLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getExpModifier(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int expBoostLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-exp_boost"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (expBoostLevel == 0 ? 1 : cnf.exp_boost.getLevels().stream().filter(lvl -> lvl.getLevel() == expBoostLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getTreasureHunterPercent(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();
        Tools toolType = Tools.valueOf(getPDC(tool).get(getKey("tool-type"), PersistentDataType.STRING));
        int treasureHunterLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-treasure_hunter"), PersistentDataType.STRING, "0"));

        double perToolMultiplier = cnf.treasure_hunter_multiplier_per_tool_override.get(toolType);

        return perToolMultiplier * getEverythingMultiplier(tool) * (treasureHunterLevel == 0 ? 0 : cnf.treasure_hunter.getLevels().stream().filter(lvl -> lvl.getLevel() == treasureHunterLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getExtractPercent(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int extractLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-extract"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (extractLevel == 0 ? 0 : cnf.extract.getLevels().stream().filter(lvl -> lvl.getLevel() == extractLevel).findFirst().orElseThrow().getMultiplier());
    }


    public static boolean isSoulbound(ItemStack tool) {
        return getPDC(tool).has(getKey("techchip-soulbound"), PersistentDataType.STRING);
    }

    public static int getLureMin(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int lureLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-lure"), PersistentDataType.STRING, "0"));
        return (lureLevel == 0 ? 100 : (cnf.lure.getLevels().stream().filter(lvl -> lvl.getLevel() == lureLevel).findFirst().orElseThrow()).getMin());
    }

    public static int getLureMax(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int lureLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-lure"), PersistentDataType.STRING, "0"));
        return (lureLevel == 0 ? 600 : (cnf.lure.getLevels().stream().filter(lvl -> lvl.getLevel() == lureLevel).findFirst().orElseThrow()).getMax());
    }


    public static double getDamageModifier(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int damageLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-damage"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (damageLevel == 0 ? 1 : cnf.damage.getLevels().stream().filter(lvl -> lvl.getLevel() == damageLevel).findFirst().orElseThrow().getMultiplier());
    }


    public static int getEfficiencyLevel(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int efficiencyLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-efficiency"), PersistentDataType.STRING, "0"));
        return efficiencyLevel == 0 ? 0 : cnf.efficiency.getLevels().stream().filter(lvl -> lvl.getLevel() == efficiencyLevel).findFirst().orElseThrow().getLevel();
    }

    public static boolean isAutoSell(ItemStack tool) {
        return getPDC(tool).has(getKey("techchip-auto_sell"), PersistentDataType.STRING);
    }

    public static double getPrice(ItemStack itemStack) {
        return ShopGuiPlusApi.getItemStackPriceSell(itemStack);
    }

    public static void givePlayerMoney(Player player, double amount) {
        ShopGuiPlusApi.getPlugin().getEconomyManager().getDefaultEconomyProvider().deposit(player, amount);
    }

}
