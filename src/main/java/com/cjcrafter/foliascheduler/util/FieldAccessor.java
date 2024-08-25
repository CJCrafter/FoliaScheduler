package com.cjcrafter.foliascheduler.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.CallerSensitive;

import java.lang.reflect.Field;

/**
 * A class that provides access to the {@code Field} class in the Java Reflection API.
 *
 * <p>This class provides methods to access the value of a field, set the value of a field without
 * requiring a try-catch block. Any {@link IllegalAccessException} is caught and rethrown as a
 * {@link RuntimeException}.
 */
public class FieldAccessor {

    private final @NotNull Field field;

    public FieldAccessor(@NotNull Field field) {
        this.field = field;
    }

    /**
     * Returns the {@code Field} object that this {@code FieldAccessor} object represents.
     *
     * @return the underlying {@code Field} object
     */
    public @NotNull Field getField() {
        return field;
    }

    /**
     * Gets the value of a static or instance {@code boolean} field.
     *
     * @param obj the object to extract the {@code boolean} value from
     * @return the value of the {@code boolean} field
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code boolean} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public boolean getBoolean(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getBoolean(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on the specified object. The value is automatically wrapped in an object if it has a primitive type.
     *
     * <p>The underlying field's value is obtained as follows:
     *
     * <p>If the underlying field is a static field, the {@code obj} argument
     * is ignored; it may be null.
     *
     * <p>Otherwise, the underlying field is an instance field.  If the
     * specified {@code obj} argument is null, the method throws a {@code NullPointerException}. If the specified object is not an instance of the class or interface declaring the underlying field, the
     * method throws an {@code IllegalArgumentException}.
     *
     * <p>If this {@code Field} object is enforcing Java language access control, and
     * the underlying field is inaccessible, the method throws an {@code IllegalAccessException}. If the underlying field is static, the class that declared the field is initialized if it has not already
     * been initialized.
     *
     * <p>Otherwise, the value is retrieved from the underlying instance
     * or static field.  If the field has a primitive type, the value is wrapped in an object before being returned, otherwise it is returned as is.
     *
     * <p>If the field is hidden in the type of {@code obj},
     * the field's value is obtained according to the preceding rules.
     *
     * @param obj object from which the represented field's value is to be extracted
     * @return the value of the represented field in object {@code obj}; primitive values are wrapped in an appropriate object before being returned
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof).
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     */
    @CallerSensitive
    public @Nullable Object get(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the field represented by this {@code Field} object on the specified object argument to the specified new value. The new value is automatically unwrapped if the underlying field has a primitive
     * type.
     *
     * <p>The operation proceeds as follows:
     *
     * <p>If the underlying field is static, the {@code obj} argument is
     * ignored; it may be null.
     *
     * <p>Otherwise the underlying field is an instance field.  If the
     * specified object argument is null, the method throws a {@code NullPointerException}.  If the specified object argument is not an instance of the class or interface declaring the underlying field,
     * the method throws an {@code IllegalArgumentException}.
     *
     * <p>If this {@code Field} object is enforcing Java language access control, and
     * the underlying field is inaccessible, the method throws an {@code IllegalAccessException}.
     *
     * <p>If the underlying field is final, the method throws an
     * {@code IllegalAccessException} unless {@code setAccessible(true)} has succeeded for this {@code Field} object and the field is non-static. Setting a final field in this way is meaningful only
     * during deserialization or reconstruction of instances of classes with blank final fields, before they are made available for access by other parts of a program. Use in any other context may have
     * unpredictable effects, including cases in which other parts of a program continue to use the original value of this field.
     *
     * <p>If the underlying field is of a primitive type, an unwrapping
     * conversion is attempted to convert the new value to a value of a primitive type.  If this attempt fails, the method throws an {@code IllegalArgumentException}.
     *
     * <p>If, after possible unwrapping, the new value cannot be
     * converted to the type of the underlying field by an identity or widening conversion, the method throws an {@code IllegalArgumentException}.
     *
     * <p>If the underlying field is static, the class that declared the
     * field is initialized if it has not already been initialized.
     *
     * <p>The field is set to the possibly unwrapped and widened new value.
     *
     * <p>If the field is hidden in the type of {@code obj},
     * the field's value is set according to the preceding rules.
     *
     * @param obj the object whose field should be modified
     * @param value the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     */
    @CallerSensitive
    public void set(@Nullable Object obj, @Nullable Object value) throws IllegalArgumentException {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code float} on the specified object. This method is equivalent to {@code set(obj, fObj)}, where {@code fObj} is a {@code Float} object and
     * {@code fObj.floatValue() == f}.
     *
     * @param obj the object whose field should be modified
     * @param f the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setFloat(@Nullable Object obj, float f) throws IllegalArgumentException {
        try {
            field.setFloat(obj, f);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance {@code byte} field.
     *
     * @param obj the object to extract the {@code byte} value from
     * @return the value of the {@code byte} field
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code byte} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public byte getByte(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getByte(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code boolean} on the specified object. This method is equivalent to {@code set(obj, zObj)}, where {@code zObj} is a {@code Boolean} object and
     * {@code zObj.booleanValue() == z}.
     *
     * @param obj the object whose field should be modified
     * @param z the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setBoolean(@Nullable Object obj, boolean z) throws IllegalArgumentException {
        try {
            field.setBoolean(obj, z);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance field of type {@code char} or of another primitive type convertible to type {@code char} via a widening conversion.
     *
     * @param obj the object to extract the {@code char} value from
     * @return the value of the field converted to type {@code char}
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code char} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public char getChar(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getChar(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code double} on the specified object. This method is equivalent to {@code set(obj, dObj)}, where {@code dObj} is a {@code Double} object and
     * {@code dObj.doubleValue() == d}.
     *
     * @param obj the object whose field should be modified
     * @param d the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setDouble(@Nullable Object obj, double d) throws IllegalArgumentException {
        try {
            field.setDouble(obj, d);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code byte} on the specified object. This method is equivalent to {@code set(obj, bObj)}, where {@code bObj} is a {@code Byte} object and
     * {@code bObj.byteValue() == b}.
     *
     * @param obj the object whose field should be modified
     * @param b the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setByte(@Nullable Object obj, byte b) throws IllegalArgumentException {
        try {
            field.setByte(obj, b);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance field of type {@code short} or of another primitive type convertible to type {@code short} via a widening conversion.
     *
     * @param obj the object to extract the {@code short} value from
     * @return the value of the field converted to type {@code short}
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code short} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public short getShort(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getShort(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code char} on the specified object. This method is equivalent to {@code set(obj, cObj)}, where {@code cObj} is a {@code Character} object and
     * {@code cObj.charValue() == c}.
     *
     * @param obj the object whose field should be modified
     * @param c the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setChar(@Nullable Object obj, char c) throws IllegalArgumentException {
        try {
            field.setChar(obj, c);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance field of type {@code int} or of another primitive type convertible to type {@code int} via a widening conversion.
     *
     * @param obj the object to extract the {@code int} value from
     * @return the value of the field converted to type {@code int}
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code int} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public int getInt(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getInt(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance field of type {@code long} or of another primitive type convertible to type {@code long} via a widening conversion.
     *
     * @param obj the object to extract the {@code long} value from
     * @return the value of the field converted to type {@code long}
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code long} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public long getLong(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getLong(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code short} on the specified object. This method is equivalent to {@code set(obj, sObj)}, where {@code sObj} is a {@code Short} object and
     * {@code sObj.shortValue() == s}.
     *
     * @param obj the object whose field should be modified
     * @param s the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setShort(@Nullable Object obj, short s) throws IllegalArgumentException {
        try {
            field.setShort(obj, s);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as an {@code int} on the specified object. This method is equivalent to {@code set(obj, iObj)}, where {@code iObj} is a {@code Integer} object and
     * {@code iObj.intValue() == i}.
     *
     * @param obj the object whose field should be modified
     * @param i the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setInt(@Nullable Object obj, int i) throws IllegalArgumentException {
        try {
            field.setInt(obj, i);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance field of type {@code float} or of another primitive type convertible to type {@code float} via a widening conversion.
     *
     * @param obj the object to extract the {@code float} value from
     * @return the value of the field converted to type {@code float}
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code float} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public float getFloat(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getFloat(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of a static or instance field of type {@code double} or of another primitive type convertible to type {@code double} via a widening conversion.
     *
     * @param obj the object to extract the {@code double} value from
     * @return the value of the field converted to type {@code double}
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if the field value
     * cannot be converted to the type {@code double} by a widening conversion.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#get
     */
    @CallerSensitive
    public double getDouble(@Nullable Object obj) throws IllegalArgumentException {
        try {
            return field.getDouble(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of a field as a {@code long} on the specified object. This method is equivalent to {@code set(obj, lObj)}, where {@code lObj} is a {@code Long} object and
     * {@code lObj.longValue() == l}.
     *
     * @param obj the object whose field should be modified
     * @param l the new value for the field of {@code obj} being modified
     * @throws RuntimeException if this {@code Field} object is enforcing Java language access control and the underlying field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping
     * conversion fails.
     * @throws NullPointerException if the specified object is null and the field is an instance field.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @see Field#set
     */
    @CallerSensitive
    public void setLong(@Nullable Object obj, long l) throws IllegalArgumentException {
        try {
            field.setLong(obj, l);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
