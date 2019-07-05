package com.andruid.magic.helpfulsense.util;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.model.ActionHolder;

import java.util.ArrayList;
import java.util.List;

public class HolderUtil {
    public static List<ActionHolder> getActionHoldersFromActions(List<Action> actions){
        List<ActionHolder> actionHolderList = new ArrayList<>();
        for(Action action : actions)
            actionHolderList.add(new ActionHolder(action));
        return actionHolderList;
    }

    public static List<Action> getActionsFromActionHolders(List<ActionHolder> actionHolders){
        List<Action> actions = new ArrayList<>();
        for(ActionHolder actionHolder : actionHolders)
            actions.add(actionHolder.getAction());
        return actions;
    }
}