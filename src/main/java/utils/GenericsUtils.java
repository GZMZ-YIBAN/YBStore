package utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * <p>
 * -----------------------------
 * @description
 * @date 21:39 2018/4/25
 * @modified By EchoLZY
 */
public class GenericsUtils {
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    private static Class getSuperClassGenricType(Class clazz, int index) {
        Type getType = clazz.getGenericSuperclass();
        if (!(getType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) getType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}