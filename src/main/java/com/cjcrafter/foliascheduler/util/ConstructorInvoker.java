package com.cjcrafter.foliascheduler.util;

import org.jetbrains.annotations.NotNull;
import sun.reflect.CallerSensitive;

import java.lang.reflect.Constructor;

/**
 * Wraps a {@link Constructor} object from the Java Reflection API and provides
 * methods to invoke the constructor without having to handle checked exceptions.
 *
 * <p>This class delegates methods to the underlying {@code Constructor} object,
 * but wraps checked exceptions in {@code RuntimeException} to avoid having to
 * handle them. All {@link ReflectiveOperationException} instances are caught
 * and rethrown as {@code RuntimeException}. Other runtime exceptions are not
 * caught and will be thrown as normal.
 *
 * @param <T> the type of object that the constructor creates
 */
public class ConstructorInvoker<T> {

    private final @NotNull Constructor<T> constructor;

    public ConstructorInvoker(@NotNull Constructor<T> constructor) {
        this.constructor = constructor;
    }

    /**
     * Returns the underlying {@code Constructor} object that this {@code ConstructorInvoker} wraps.
     *
     * @return the underlying {@code Constructor} object.
     */
    public @NotNull Constructor<T> getConstructor() {
        return constructor;
    }

    /**
     * Uses the constructor represented by this {@code Constructor} object to create and initialize a new instance of the constructor's declaring class, with the specified initialization parameters.
     * Individual parameters are automatically unwrapped to match primitive formal parameters, and both primitive and reference parameters are subject to method invocation conversions as necessary.
     *
     * <p>If the number of formal parameters required by the underlying constructor
     * is 0, the supplied {@code initargs} array may be of length 0 or null.
     *
     * <p>If the constructor's declaring class is an inner class in a
     * non-static context, the first argument to the constructor needs to be the enclosing instance; see section 15.9.3 of
     * <cite>The Java&trade; Language Specification</cite>.
     *
     * <p>If the required access and argument checks succeed and the
     * instantiation will proceed, the constructor's declaring class is initialized if it has not already been initialized.
     *
     * <p>If the constructor completes normally, returns the newly
     * created and initialized instance.
     *
     * @param initargs array of objects to be passed as arguments to the constructor call; values of primitive types are wrapped in a wrapper object of the appropriate type (e.g. a {@code float} in a
     * {@link Float Float})
     * @return a new object created by calling the constructor this object represents
     * @throws WrappedReflectiveOperationException if this {@code Constructor} object is enforcing Java language access control and the underlying constructor is inaccessible.
     * @throws IllegalArgumentException if the number of actual and formal parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value
     * cannot be converted to the corresponding formal parameter type by a method invocation conversion; if this constructor pertains to an enum type.
     * @throws WrappedReflectiveOperationException if the class that declares the underlying constructor represents an abstract class.
     * @throws WrappedReflectiveOperationException if the underlying constructor throws an exception.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     */
    @CallerSensitive
    public @NotNull T newInstance(Object... initargs) throws IllegalArgumentException {
        try {
            return constructor.newInstance(initargs);
        } catch (ReflectiveOperationException e) {
            throw new WrappedReflectiveOperationException(e);
        }
    }
}
