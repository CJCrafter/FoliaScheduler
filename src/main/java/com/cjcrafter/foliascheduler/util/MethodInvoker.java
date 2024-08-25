package com.cjcrafter.foliascheduler.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.CallerSensitive;

import java.lang.reflect.Method;

/**
 * Wraps a {@link Method} object from the Java Reflection API and provides
 * methods to invoke the method without having to handle checked exceptions.
 *
 * <p>This class delegates methods to the underlying {@code Method} object,
 * but wraps checked exceptions in {@code RuntimeException} to avoid having to
 * handle them. All {@link ReflectiveOperationException} instances are caught
 * and rethrown as {@code RuntimeException}. Other runtime exceptions are not
 * caught and will be thrown as normal.
 */
public class MethodInvoker {

    private final @NotNull Method method;

    public MethodInvoker(@NotNull Method method) {
        this.method = method;
    }

    /**
     * Returns the underlying {@code Method} object that this {@code MethodInvoker} wraps.
     *
     * @return the underlying {@code Method} object
     */
    public @NotNull Method getMethod() {
        return method;
    }

    /**
     * Invokes the underlying method represented by this {@code Method} object, on the specified object with the specified parameters. Individual parameters are automatically unwrapped to match primitive
     * formal parameters, and both primitive and reference parameters are subject to method invocation conversions as necessary.
     *
     * <p>If the underlying method is static, then the specified {@code obj}
     * argument is ignored. It may be null.
     *
     * <p>If the number of formal parameters required by the underlying method is
     * 0, the supplied {@code args} array may be of length 0 or null.
     *
     * <p>If the underlying method is an instance method, it is invoked
     * using dynamic method lookup as documented in The Java Language Specification, Second Edition, section 15.12.4.4; in particular, overriding based on the runtime type of the target object will
     * occur.
     *
     * <p>If the underlying method is static, the class that declared
     * the method is initialized if it has not already been initialized.
     *
     * <p>If the method completes normally, the value it returns is
     * returned to the caller of invoke; if the value has a primitive type, it is first appropriately wrapped in an object. However, if the value has the type of an array of a primitive type, the elements
     * of the array are <i>not</i> wrapped in objects; in other words, an array of primitive type is returned.  If the underlying method return type is void, the invocation returns null.
     *
     * @param obj the object the underlying method is invoked from
     * @param args the arguments used for the method call
     * @return the result of dispatching the method represented by this object on {@code obj} with parameters {@code args}
     * @throws RuntimeException if this {@code Method} object is enforcing Java language access control and the underlying method is inaccessible.
     * @throws IllegalArgumentException if the method is an instance method and the specified object argument is not an instance of the class or interface declaring the underlying method (or of a subclass
     * or implementor thereof); if the number of actual and formal parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value cannot
     * be converted to the corresponding formal parameter type by a method invocation conversion.
     * @throws RuntimeException if the underlying method throws an exception.
     * @throws NullPointerException if the specified object is null and the method is an instance method.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     */
    @CallerSensitive
    public @Nullable Object invoke(@Nullable Object obj, Object... args) throws IllegalArgumentException {
        try {
            return method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
