package com.cjcrafter.foliascheduler.util;

import java.util.function.Predicate;

/**
 * Incomplete class used in {@link ReflectionUtilTest} to test the predicates in
 * {@link ReflectionUtil#getField(Class, Class, int, Predicate)} and
 * {@link ReflectionUtil#getMethod(Class, Class, int, Predicate, Class...)}.
 */
public class TestFilterObject {
    public static final int STATIC_FINAL_INT = 0;
    public int w;
    private int x;
    private int y;
    private int z;
}
