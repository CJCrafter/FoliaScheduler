package com.cjcrafter.foliascheduler.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.jpenilla.reflectionremapper.ReflectionRemapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * A utility class that wraps the Java Reflection API and provides methods to
 * access classes and constructors without having to handle checked exceptions.
 *
 * <p>This class delegates methods to the underlying Java Reflection API,
 * but wraps checked exceptions in {@link WrappedReflectiveOperationException}.
 * You can get the original exception by calling {@link WrappedReflectiveOperationException#getCause()}.
 *
 * <p>This class handles the remapping of class names, field names, and method names
 * when running on Paper 1.20.5+. Because of the remapping, these methods will be
 * even slower than the normal Java Reflection API. This means that you should be
 * saving the results of these methods, typically in a <code>static final</code>
 * field, to avoid the performance penalty of reflection.
 */
public final class ReflectionUtil {

    // These predicates can be used in #getField and #getMethod to filter fields more precisely
    public static final @NotNull Predicate<Member> IS_PUBLIC = (member) -> Modifier.isPublic(member.getModifiers());
    public static final @NotNull Predicate<Member> IS_NOT_PUBLIC = IS_PUBLIC.negate();
    public static final @NotNull Predicate<Member> IS_PRIVATE = (member) -> Modifier.isPrivate(member.getModifiers());
    public static final @NotNull Predicate<Member> IS_NOT_PRIVATE = IS_PRIVATE.negate();
    public static final @NotNull Predicate<Member> IS_STATIC = (member) -> Modifier.isStatic(member.getModifiers());
    public static final @NotNull Predicate<Member> IS_NOT_STATIC = IS_STATIC.negate();
    public static final @NotNull Predicate<Member> IS_FINAL = (member) -> Modifier.isFinal(member.getModifiers());
    public static final @NotNull Predicate<Member> IS_NOT_FINAL = IS_FINAL.negate();

    private ReflectionUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static @NotNull Field makeFieldAccessible(@NotNull Field field) {
        if (!field.isAccessible())
            field.setAccessible(true);
        return field;
    }

    private static @NotNull Method makeMethodAccessible(@NotNull Method method) {
        if (!method.isAccessible())
            method.setAccessible(true);
        return method;
    }

    /**
     * Returns the {@link Class} object with the specified name.
     *
     * <p>This method is a wrapper around {@link Class#forName(String)} that
     * catches the checked exception and rethrows it as a {@link WrappedReflectiveOperationException}.
     *
     * <p>When running on Paper 1.20.5+, this method will remap the class name
     * using the mappings built into the Paper jar.
     *
     * @param className the fully qualified name of the desired class
     * @return the {@code Class} object for the class with the specified name
     * @param <T> the type of the class
     * @throws WrappedReflectiveOperationException if the class cannot be found
     */
    public static <T> @NotNull Class<T> getClass(@NotNull String className) {
        // In Paper 1.20.5+, Paper remaps the server to Mojang mappings
        if (ServerVersions.isPaper() && MinecraftVersions.TRAILS_AND_TAILS.get(5).isAtLeast()) {
            ReflectionRemapper remapper = ReflectionRemapper.forReobfMappingsInPaperJar();
            className = remapper.remapClassOrArrayName(className);
        }

        try {
            // noinspection unchecked
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new WrappedReflectiveOperationException(e);
        }
    }

    /**
     * Returns the net.minecraft.server {@link Class} object with the specified name.
     * This method calls {@link #getClass(String)} with the full class name constructed
     * from the package name and class name.
     *
     * <p>We recommend using a <a href="https://mappings.dev/">mappings website</a> to find the
     * correct package and class name. Always use the <b>Spigot mapped</b> package and class name.
     *
     * @param packageName the name of the <b>Spigot mapped</b> mojang package, without the
     * "net.minecraft." prefix (e.g. "world.entity.player")
     * @param className the mojang mapped class name (e.g. "EntityPlayer")
     * @return the {@code Class} object for the class with the specified name
     * @param <T> the type of the class
     * @throws WrappedReflectiveOperationException if the class cannot be found
     */
    public static <T> @NotNull Class<T> getMinecraftClass(@NotNull String packageName, @NotNull String className) {

        // In 1.17+, Spigot stopped remapping Mojang into 1 big package
        if (MinecraftVersions.CAVES_AND_CLIFFS_1.isAtLeast()) {
            return getClass("net.minecraft." + packageName + "." + className);
        }

        // In older versions, Mojang classes are in a single package: net.minecraft.server.<version>
        return getClass("net.minecraft.server." + MinecraftVersions.getCurrent() + "." + className);
    }

    /**
     * Returns the {@link Class} object from the org.bukkit.craftbukkit package with the specified
     * relative path. This method calls {@link #getClass(String)}.
     *
     * <p>For example, to get the class <code>org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer</code>,
     * you would call <code>getCraftBukkitClass("entity.CraftPlayer")</code>.
     *
     * @param classPath the relative path of the class, without the "org.bukkit.craftbukkit." prefix.
     * @return the {@code Class} object for the class with the specified name
     * @param <T> the type of the class
     */
    public static <T> @NotNull Class<T> getCraftBukkitClass(@NotNull String classPath) {
        // The version data was stripped from the package name in Paper servers 1.20.5+
        if (ServerVersions.isPaper() && MinecraftVersions.TRAILS_AND_TAILS.get(5).isAtLeast()) {
            return getClass("org.bukkit.craftbukkit." + classPath);
        }

        return getClass("org.bukkit.craftbukkit." + MinecraftVersions.getCurrent() + "." + classPath);
    }

    /**
     * Returns the {@link FieldAccessor} for the specified field.
     *
     * @param clazz The class that owns the field.
     * @param fieldName The name of the field.
     * @return The {@link FieldAccessor} for the field.
     */
    public static @NotNull FieldAccessor getField(@NotNull Class<?> clazz, @NotNull String fieldName) {
        try {
            // In Paper 1.20.5+, Paper remaps the server to Mojang mappings
            if (ServerVersions.isPaper() && MinecraftVersions.TRAILS_AND_TAILS.get(5).isAtLeast()) {
                ReflectionRemapper remapper = ReflectionRemapper.forReobfMappingsInPaperJar();
                fieldName = remapper.remapFieldName(clazz, fieldName);
            }
            return new FieldAccessor(makeFieldAccessible(clazz.getDeclaredField(fieldName)));
        } catch (ReflectiveOperationException e) {
            throw new WrappedReflectiveOperationException(e);
        }
    }

    /**
     * Returns the {@link FieldAccessor} for the specified field. If no such field exists, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param clazz The class that owns the field.
     * @param fieldType The type of the field.
     * @return The {@link FieldAccessor} for the field.
     * @see #getField(Class, Class, int, Predicate)
     */
    public static @NotNull FieldAccessor getField(@NotNull Class<?> clazz, @NotNull Class<?> fieldType) {
        return getField(clazz, fieldType, 0, null);
    }

    /**
     * Returns the {@link FieldAccessor} for the specified field. If no such field exists, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param clazz The class that owns the field.
     * @param fieldType The type of the field.
     * @param index The index of the field to get (in case there are multiple fields of the same type).
     * @return The {@link FieldAccessor} for the field.
     * @see #getField(Class, Class, int, Predicate)
     */
    public static @NotNull FieldAccessor getField(@NotNull Class<?> clazz, @NotNull Class<?> fieldType, int index) {
        return getField(clazz, fieldType, index, null);
    }

    /**
     * Returns the {@link FieldAccessor} for the specified field. If no such field exists, an
     * {@link IllegalArgumentException} is thrown.
     *
     * <p>This method can be used to get the nth field of a certain type in a class. For example,
     * to get the 2nd non-static field of type int in a class, you would call
     * <code>getField(clazz, int.class, 2, IS_FIELD_NON_STATIC)</code>.
     *
     * @param clazz The class that owns the field.
     * @param fieldType The type of the field.
     * @param index The index of the field to get (in case there are multiple fields of the same type).
     * @param predicate A predicate to filter the fields. If the predicate returns false, the field is skipped.
     * @return The {@link FieldAccessor} for the field.
     */
    public static @NotNull FieldAccessor getField(@NotNull Class<?> clazz, @NotNull Class<?> fieldType, int index, @Nullable Predicate<? super Field> predicate) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!fieldType.isAssignableFrom(field.getType()))
                continue;
            if (predicate != null && !predicate.test(field))
                continue;
            if (index-- > 0)
                continue;

            return new FieldAccessor(makeFieldAccessible(field));
        }

        // If no field was found, recursively check super class
        if (clazz.getSuperclass() != null)
            return getField(clazz.getSuperclass(), fieldType, index, predicate);

        throw new IllegalArgumentException("No field of type " + fieldType.getName() + " found in class " + clazz.getName());
    }

    /**
     * Returns the {@link MethodInvoker} for the specified method.
     *
     * @param clazz The class that owns the method.
     * @param methodName The name of the method.
     * @param parameterTypes The parameter types of the method.
     * @return The {@link MethodInvoker} for the method.
     */
    public static @NotNull MethodInvoker getMethod(@NotNull Class<?> clazz, @NotNull String methodName, Class<?>... parameterTypes) {
        try {
            // In Paper 1.20.5+, Paper remaps the server to Mojang mappings
            if (ServerVersions.isPaper() && MinecraftVersions.TRAILS_AND_TAILS.get(5).isAtLeast()) {
                ReflectionRemapper remapper = ReflectionRemapper.forReobfMappingsInPaperJar();
                methodName = remapper.remapMethodName(clazz, methodName);
            }
            return new MethodInvoker(makeMethodAccessible(clazz.getDeclaredMethod(methodName, parameterTypes)));
        } catch (ReflectiveOperationException e) {
            throw new WrappedReflectiveOperationException(e);
        }
    }

    /**
     * Returns the {@link MethodInvoker} for the specified method. If no such method exists, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param clazz The class that owns the method.
     * @param returnType The return type of the method.
     * @param parameterTypes The parameter types of the method.
     * @return The {@link MethodInvoker} for the method.
     * @see #getMethod(Class, Class, int, Predicate, Class...)
     */
    public static @NotNull MethodInvoker getMethod(@NotNull Class<?> clazz, @NotNull Class<?> returnType, Class<?>... parameterTypes) {
        return getMethod(clazz, returnType, 0, null, parameterTypes);
    }

    /**
     * Returns the {@link MethodInvoker} for the specified method. If no such method exists, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param clazz The class that owns the method.
     * @param returnType The return type of the method.
     * @param index The index of the method to get (in case there are multiple methods with the same return type).
     * @param parameterTypes The parameter types of the method.
     * @return The {@link MethodInvoker} for the method.
     * @see #getMethod(Class, Class, int, Predicate, Class...)
     */
    public static @NotNull MethodInvoker getMethod(@NotNull Class<?> clazz, @NotNull Class<?> returnType, int index, Class<?>... parameterTypes) {
        return getMethod(clazz, returnType, index, null, parameterTypes);
    }

    /**
     * Returns the {@link MethodInvoker} for the specified method. If no such method exists, an
     * {@link IllegalArgumentException} is thrown.
     *
     * <p>This method can be used to get the <code>n</code>th method of a certain return type in a
     * class. For example, to get the 2nd method with return type void in a class, you would call
     * <code>getMethod(clazz, void.class, 2, null, parameterTypes)</code>.
     *
     * @param clazz The class that owns the method.
     * @param returnType The return type of the method.
     * @param index The index of the method to get (in case there are multiple methods with the same return type).
     * @param predicate A predicate to filter the methods. If the predicate returns false, the method is skipped.
     * @param parameterTypes The parameter types of the method.
     * @return The {@link MethodInvoker} for the method.
     */
    public static @NotNull MethodInvoker getMethod(@NotNull Class<?> clazz, @NotNull Class<?> returnType, int index, @Nullable Predicate<? super Method> predicate, Class<?>... parameterTypes) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!returnType.isAssignableFrom(method.getReturnType()))
                continue;
            if (predicate != null && !predicate.test(method))
                continue;
            if (parameterTypes.length != method.getParameterCount())
                continue;

            // Each argument must be assignable from the parameter type
            boolean match = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!parameterTypes[i].isAssignableFrom(method.getParameterTypes()[i])) {
                    match = false;
                    break;
                }
            }
            if (!match)
                continue;
            if (index-- > 0)
                continue;

            return new MethodInvoker(makeMethodAccessible(method));
        }

        // If no method was found, recursively check super class
        if (clazz.getSuperclass() != null)
            return getMethod(clazz.getSuperclass(), returnType, index, predicate, parameterTypes);

        throw new IllegalArgumentException("No method with return type " + returnType.getName() + " found in class " + clazz.getName());
    }

    /**
     * Returns the {@link ConstructorInvoker} for the specified constructor.
     *
     * @param clazz The class that owns the constructor.
     * @param parameterTypes The parameter types of the constructor.
     * @return The {@link ConstructorInvoker} for the constructor.
     * @param <T> The type of the class.
     */
    public static <T> @NotNull ConstructorInvoker<T> getConstructor(@NotNull Class<T> clazz, Class<?>... parameterTypes) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            return new ConstructorInvoker<>(constructor);
        } catch (ReflectiveOperationException e) {
            throw new WrappedReflectiveOperationException(e);
        }
    }
}
