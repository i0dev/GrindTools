package com.i0dev.grindtools.task;

import com.massivecraft.massivecore.ModuloRepeatTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        for (ActionBarMessage actionBarMessage : actionBarLastMessageList) {
            // If it has been more than X seconds,

        }

    }

    public


    int seconds = 10;
    public List<ActionBarMessage> actionBarLastMessageList = new ArrayList<>();


    public ActionBarMessage getActionBarMessage(UUID player, ActionBarType type) {
        return actionBarLastMessageList.stream()
                .filter(actionBarMessage -> actionBarMessage.getPlayer().equals(player))
                .filter(actionBarMessage -> actionBarMessage.getType().equals(type))
                .findFirst()
                .orElse(null);
    }

    public enum ActionBarType {
        HOE,
        PICKAXE,
        SWORD,
        ROD,
        FLUX
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class ActionBarMessage {
        private UUID player;
        private long time;
        private int amount;
        private ActionBarType type;

        public void addAmount(int amount) {
            this.amount += amount;
        }
    }
}
