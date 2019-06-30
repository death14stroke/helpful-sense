package com.andruid.magic.helpfulsense.eventbus;

import com.andruid.magic.helpfulsense.model.Action;

public class ActionEvent {
    private final Action action;
    private final String command;

    public ActionEvent(Action action, String command) {
        this.action = action;
        this.command = command;
    }

    public Action getAction() {
        return action;
    }

    public String getCommand() {
        return command;
    }
}