package com.i0dev.grindtools.task;

import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.ModuloRepeatTask;
import it.unimi.dsi.fastutil.Hash;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class TaskSendHotbarMessage extends ModuloRepeatTask {

    private static TaskSendHotbarMessage i = new TaskSendHotbarMessage();

    public static TaskSendHotbarMessage get() {
        return i;
    }

    @Override
    public long getDelayMillis() {
        return 1000L; // 1 second
    }

    @Override
    public void invoke(long l) {
        List<ActionBarMessage> toRemove = new ArrayList<>();
        for (ActionBarMessage actionBarMessage : actionBarLastMessageList) {
            // If it has been more than X seconds,
            if (actionBarMessage.getTime() + (seconds * 1000L) < System.currentTimeMillis()) {
                // Send the message
                int fluxAmount = actionBarMessage.getTypeAmountMap().getOrDefault(ActionBarType.FLUX, 0);
                int moneyAmount = actionBarMessage.getTypeAmountMap().getOrDefault(ActionBarType.MONEY, 0);
                int amount = actionBarMessage.amount;


                if (fluxAmount > 0 && moneyAmount > 0) { // Both have been gained
                    Utils.sendActionBarMessage(actionBarMessage.getPlayer(), MLang.get().autoSellActionBarMessageBoth
                            .replace("%flux%", String.valueOf(fluxAmount))
                            .replace("%money%", String.valueOf(moneyAmount))
                            .replace("%amount%", String.valueOf(amount))
                    );
                } else if (fluxAmount > 0) { // Only flux has been gained
                    Utils.sendActionBarMessage(actionBarMessage.getPlayer(), MLang.get().autoSellActionBarMessageFluxOnly
                            .replace("%flux%", String.valueOf(fluxAmount))
                            .replace("%amount%", String.valueOf(amount))
                    );
                }


                // Remove it
                toRemove.add(actionBarMessage);
            }
        }
        actionBarLastMessageList.removeAll(toRemove);
    }

    public int seconds = 10;
    public List<ActionBarMessage> actionBarLastMessageList = new ArrayList<>();

    public enum ActionBarType {
        MONEY,
        FLUX
    }

    @Getter
    @Setter
    public static class ActionBarMessage {
        private UUID player;
        private long time; //Time in milliseconds at which the message was last sent
        int amount; //Amount of the items
        private HashMap<ActionBarType, Integer> typeAmountMap;

        public void addAmount(ActionBarType type, int itemAmount, int typeAmount) { //Add amount to the map
            int previousAmount = typeAmountMap.getOrDefault(type, 0);
            typeAmountMap.put(type, previousAmount + typeAmount);
            this.amount += itemAmount;
        }

        public ActionBarMessage(UUID player, long time, int amount, Map<ActionBarType, Integer> typeAmountMap) {
            this.player = player;
            this.time = time;
            this.amount = amount;
            // copy the map
            this.typeAmountMap = new HashMap<>(typeAmountMap);
        }
    }


    public static void addAmountToActionBarMessage(UUID player, ActionBarType type, int itemsAmount, int typeMapAmount) {
        // Check if player has an action bar message
        ActionBarMessage actionBarMessage = TaskSendHotbarMessage.get().actionBarLastMessageList.stream()
                .filter(actionBarMessage1 -> actionBarMessage1.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
        if (actionBarMessage == null) {
            // If the player does not have an action bar message, create one
            actionBarMessage = new ActionBarMessage(player, System.currentTimeMillis(), itemsAmount, Map.of(type, typeMapAmount));
            TaskSendHotbarMessage.get().actionBarLastMessageList.add(actionBarMessage);
        } else {
            // If the player has an action bar message, add the amount to the map
            actionBarMessage.addAmount(type, itemsAmount, typeMapAmount);
        }
    }
}
