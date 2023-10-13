package com.noscompany.snake.game.online.gui.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Controllers {
    private static final Map<Class<?>, AbstractController> controllers = new HashMap<>();

    static <T extends AbstractController> void put(T controller) {
        controllers.put(controller.getClass(), controller);
    }

    public static <T extends AbstractController> T get(Class<T> tClass) {
        return (T) controllers.get(tClass);
    }

    public static Collection<AbstractController> getAll() {
        return new LinkedList<>(controllers.values());
    }
}