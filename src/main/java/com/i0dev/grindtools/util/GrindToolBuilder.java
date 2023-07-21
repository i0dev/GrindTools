package com.i0dev.grindtools.util;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.*;
import net.brcdev.shopgui.ShopGuiPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GrindToolBuilder {

    public static ItemStack buildNewItem(Tier tier, Tools type) {
        return applyTier(new ItemBuilder(tier.getMaterial()).name(tier.getDisplayName()), tier, type);
    }

    public static ItemStack applyTier(ItemStack item, Tier tier, Tools type) {

        ItemMeta meta1 = item.getItemMeta();
        meta1.setDisplayName(Utils.color(tier.getDisplayName()));
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
                applyTag(item, "baseCurrency", String.valueOf(HoeConfig.get().getBaseCurrency()));
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(HoeConfig.get().getLoreFormat(), item));
                item.setItemMeta(meta);
            }
            case PICKAXE -> {
                applyTag(item, "baseCurrency", String.valueOf(PickaxeConfig.get().getBaseCurrency()));
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(PickaxeConfig.get().getLoreFormat(), item));
                item.setItemMeta(meta);
            }
            case ROD -> {
                applyTag(item, "baseCurrency", String.valueOf(RodConfig.get().getBaseCurrency()));
                ItemMeta meta = item.getItemMeta();
                meta.setLore(formatLore(RodConfig.get().getLoreFormat(), item));
                item.setItemMeta(meta);
            }
            case SWORD -> {
                applyTag(item, "baseCurrency", String.valueOf(SwordConfig.get().getBaseCurrency()));
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

        switch (type) {
            case HOE ->
                    newLore.replaceAll(s -> Utils.color(s.replace("%description%", HoeConfig.get().getFromId(pdc.get(getKey("tier"), PersistentDataType.STRING)).getDescription())));
            case PICKAXE ->
                    newLore.replaceAll(s -> Utils.color(s.replace("%description%", PickaxeConfig.get().getFromId(pdc.get(getKey("tier"), PersistentDataType.STRING)).getDescription())));
            case ROD ->
                    newLore.replaceAll(s -> Utils.color(s.replace("%description%", RodConfig.get().getFromId(pdc.get(getKey("tier"), PersistentDataType.STRING)).getDescription())));
            case SWORD ->
                    newLore.replaceAll(s -> Utils.color(s.replace("%description%", SwordConfig.get().getFromId(pdc.get(getKey("tier"), PersistentDataType.STRING)).getDescription())));
        }


        String chipFormat = "&7- &a%chip% &7(Level %level%)";

        TechChipConfig cnf = TechChipConfig.get();

        int baseCurrency = Integer.parseInt(getPDC(item).get(getKey("baseCurrency"), PersistentDataType.STRING));

        double tokenBoost = Math.round((getCurrencyModifier(item) - baseCurrency) * 100.0) / 100.0;
        double expBoost = Math.round(getExpModifier(item) * 100.0) / 100.0;
        double dropBoost = Math.round(getDropModifier(item) * 100.0) / 100.0;
        double treasureHunter = Math.round(getTreasureHunterPercent(item) * 10000.0) / 10000.0;
        double extract = Math.round(getExtractPercent(item) * 1000.0) / 1000.0;
        double averageLure = Math.round(((double) (getLureMin(item) + getLureMax(item)) / 2 / 20) * 10.0) / 10.0;
        double damage = Math.round(getDamageModifier(item) * 10.0) / 10.0;

        List<String> techChipsToAdd = new ArrayList<>();
        List<String> modifiersToAdd = new ArrayList<>();

        getAllTechChips(item).forEach((techChip, multiplierLevel) -> techChipsToAdd.add(Utils.color(chipFormat
                .replace("%chip%", techChip.getDisplayName())
                .replace("%level%", multiplierLevel == null ? "1" : String.valueOf(multiplierLevel.getLevel()))
        )));

        if (tokenBoost > 1 && isAppliedToTool(type, cnf.token_boost)) {
            modifiersToAdd.add(Utils.color("&7- &6Token Boost &7(x" + tokenBoost + ")"));
        }
        if (expBoost > 1 && isAppliedToTool(type, cnf.exp_boost)) {
            modifiersToAdd.add(Utils.color("&7- &6Exp Boost &7(x" + expBoost + ")"));
        }
        if (dropBoost > 1 && isAppliedToTool(type, cnf.drop_boost)) {
            modifiersToAdd.add(Utils.color("&7- &6Drop Boost &7(x" + dropBoost + ")"));
        }
        if (treasureHunter > 1 && isAppliedToTool(type, cnf.treasure_hunter)) {
            modifiersToAdd.add(Utils.color("&7- &6Treasure Hunter &7(" + treasureHunter + "%)"));
        }
        if (extract > 1 && isAppliedToTool(type, cnf.extract)) {
            modifiersToAdd.add(Utils.color("&7- &6Extract &7(" + extract + "%)"));
        }
        if (isAppliedToTool(type, cnf.lure)) {
            modifiersToAdd.add(Utils.color("&7- &6Lure &7(" + averageLure + "s avg catch time)"));
        }
        if (damage > 1 && isAppliedToTool(type, cnf.damage)) {
            modifiersToAdd.add(Utils.color("&7- &6Damage &7(x" + damage + ")"));
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

    public static double getDropModifier(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        double dropRatesMultiplier = Double.parseDouble(getPDC(tool).get(getKey("modifier-dropRatesMultiplier"), PersistentDataType.STRING));
        int dropBoostLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-drop_boost"), PersistentDataType.STRING, "0"));

        // get the 0.XX of the dropRatesMultiplier
        double dropRatesMultiplierDecimal = dropRatesMultiplier - Math.floor(dropRatesMultiplier);
        double percentChanceToRoundUp = dropRatesMultiplierDecimal * 100;
        dropRatesMultiplier = Math.floor(dropRatesMultiplier) + (ThreadLocalRandom.current().nextInt(100) < percentChanceToRoundUp ? 1 : 0);

        return getEverythingMultiplier(tool) * (dropRatesMultiplier * dropBoostLevel == 0 ? 1 : cnf.drop_boost.getLevels().stream().filter(lvl -> lvl.getLevel() == dropBoostLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getCurrencyModifier(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();
        int baseCurrency = Integer.parseInt(getPDC(tool).get(getKey("baseCurrency"), PersistentDataType.STRING));

        int tokenBoostLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-token_boost"), PersistentDataType.STRING, "0"));
        return baseCurrency * getEverythingMultiplier(tool) * (tokenBoostLevel == 0 ? 1 : cnf.token_boost.getLevels().stream().filter(lvl -> lvl.getLevel() == tokenBoostLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getExpModifier(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int expBoostLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-exp_boost"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (expBoostLevel == 0 ? 1 : cnf.exp_boost.getLevels().stream().filter(lvl -> lvl.getLevel() == expBoostLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getTreasureHunterPercent(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int treasureHunterLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-treasure_hunter"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (treasureHunterLevel == 0 ? 0 : cnf.treasure_hunter.getLevels().stream().filter(lvl -> lvl.getLevel() == treasureHunterLevel).findFirst().orElseThrow().getMultiplier());
    }

    public static double getExtractPercent(ItemStack tool) {
        TechChipConfig cnf = TechChipConfig.get();

        int extractLevel = Integer.parseInt(getPDC(tool).getOrDefault(getKey("techchip-extract"), PersistentDataType.STRING, "0"));
        return getEverythingMultiplier(tool) * (extractLevel == 0 ? 0 : cnf.extract.getLevels().stream().filter(lvl -> lvl.getLevel() == extractLevel).findFirst().orElseThrow().getMultiplier());
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
