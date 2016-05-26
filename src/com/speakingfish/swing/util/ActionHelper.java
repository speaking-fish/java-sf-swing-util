package com.speakingfish.swing.util;

import javax.swing.Action;

import com.speakingfish.common.type.Named;

import static com.speakingfish.common.type.Names.*;

public class ActionHelper {
    
    public static final Named<String> ACTION_NAME = named(Action.NAME);

    @SuppressWarnings("unchecked")
    public static <T> T getActionValue(Action action, Named<T> key) {
        return (T) action.getValue(key.id());
    }

    public static <T> void putActionValue(Action action, Named<T> key, T value) {
        action.putValue(key.id(), value);
    }
    
    
}
