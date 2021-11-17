package com.noscompany.snakejavafxclient.commons;

import java.util.HashMap;
import java.util.Map;

public class Controllers {
    private static final Map<Class<?>, Object> controllers = new HashMap<>();

    static <T extends AbstractController> void put(T controller) {
        controllers.put(controller.getClass(), controller);
    }

    public static <T extends AbstractController> T get(Class<T> tClass) {
        return (T) controllers.get(tClass);
    }
}