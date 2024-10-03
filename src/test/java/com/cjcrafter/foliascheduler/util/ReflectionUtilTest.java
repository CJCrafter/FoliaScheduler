package com.cjcrafter.foliascheduler.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionUtilTest {

    @Test
    public void testGetNamedField() {
        FieldAccessor field = ReflectionUtil.getField(Boolean.class, "TRUE");
        assertEquals(Boolean.TRUE, field.get(null));
    }

    @Test
    public void testSearchForStaticFinalFields() {
        // This test is bad since the behavior will be the same if this predicate is null...
        // Filters are properly tested in other test cases.
        Predicate<Member> isStaticFinal = ReflectionUtil.IS_STATIC.and(ReflectionUtil.IS_FINAL);
        FieldAccessor actualTrue = ReflectionUtil.getField(Boolean.class, Boolean.class, 0, isStaticFinal);
        assertEquals(Boolean.TRUE, actualTrue.get(null));

        FieldAccessor actualFalse = ReflectionUtil.getField(Boolean.class, Boolean.class, 1, isStaticFinal);
        assertEquals(Boolean.FALSE, actualFalse.get(null));
    }

    @Test
    public void testSearchForInstanceFields() {
        FieldAccessor wrappedValueField = ReflectionUtil.getField(Boolean.class, boolean.class);
        assertEquals(true, wrappedValueField.get(Boolean.TRUE));
        assertEquals(false, wrappedValueField.get(Boolean.FALSE));
    }

    @Test
    public void testGetNamedMethod() {
        MethodInvoker method = ReflectionUtil.getMethod(Boolean.class, "valueOf", String.class);
        assertEquals(Boolean.TRUE, method.invoke(null, "true"));
    }

    @Test
    public void testSearchForStaticMethods() {
        MethodInvoker valueOf = ReflectionUtil.getMethod(Boolean.class, Boolean.class, 0, ReflectionUtil.IS_STATIC, String.class);
        assertEquals(Boolean.TRUE, valueOf.invoke(null, "true"));
        assertEquals(Boolean.FALSE, valueOf.invoke(null, "false"));
    }

    @Test
    public void testFilters() {
        // This test tries many different ways of getting the same field
        FieldAccessor w = ReflectionUtil.getField(TestFilterObject.class, int.class, 0, ReflectionUtil.IS_NOT_STATIC);
        assertEquals("w", w.getField().getName());
        w = ReflectionUtil.getField(TestFilterObject.class, int.class, 0, ReflectionUtil.IS_NOT_FINAL);
        assertEquals("w", w.getField().getName());
        w = ReflectionUtil.getField(TestFilterObject.class, int.class, 1);
        assertEquals("w", w.getField().getName());

        FieldAccessor x = ReflectionUtil.getField(TestFilterObject.class, int.class, 0, ReflectionUtil.IS_NOT_STATIC.and(ReflectionUtil.IS_PRIVATE));
        assertEquals("x", x.getField().getName());
        x = ReflectionUtil.getField(TestFilterObject.class, int.class, 0, ReflectionUtil.IS_NOT_FINAL.and(ReflectionUtil.IS_PRIVATE));
        assertEquals("x", x.getField().getName());
        x = ReflectionUtil.getField(TestFilterObject.class, int.class, 0, ReflectionUtil.IS_PRIVATE);
        assertEquals("x", x.getField().getName());
        x = ReflectionUtil.getField(TestFilterObject.class, int.class, 2);
        assertEquals("x", x.getField().getName());

        FieldAccessor y = ReflectionUtil.getField(TestFilterObject.class, int.class, 1, ReflectionUtil.IS_NOT_STATIC.and(ReflectionUtil.IS_PRIVATE));
        assertEquals("y", y.getField().getName());
        y = ReflectionUtil.getField(TestFilterObject.class, int.class, 1, ReflectionUtil.IS_NOT_FINAL.and(ReflectionUtil.IS_PRIVATE));
        assertEquals("y", y.getField().getName());
        y = ReflectionUtil.getField(TestFilterObject.class, int.class, 1, ReflectionUtil.IS_PRIVATE);
        assertEquals("y", y.getField().getName());
        y = ReflectionUtil.getField(TestFilterObject.class, int.class, 3);
        assertEquals("y", y.getField().getName());

        FieldAccessor z = ReflectionUtil.getField(TestFilterObject.class, int.class, 2, ReflectionUtil.IS_NOT_STATIC.and(ReflectionUtil.IS_PRIVATE));
        assertEquals("z", z.getField().getName());
        z = ReflectionUtil.getField(TestFilterObject.class, int.class, 2, ReflectionUtil.IS_NOT_FINAL.and(ReflectionUtil.IS_PRIVATE));
        assertEquals("z", z.getField().getName());
        z = ReflectionUtil.getField(TestFilterObject.class, int.class, 2, ReflectionUtil.IS_PRIVATE);
        assertEquals("z", z.getField().getName());
        z = ReflectionUtil.getField(TestFilterObject.class, int.class, 4);
        assertEquals("z", z.getField().getName());
    }
}
