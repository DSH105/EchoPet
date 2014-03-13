package io.github.dsh105.echopet.reflection;

import java.lang.reflect.Field;

public interface FieldAccessor<T> {

    public Field getField();

    boolean set(Object instance, T value);

    T get(Object instance);

    T transfer(Object from, Object to);

    boolean isPublic();

    boolean isReadOnly();

    void setReadOnly(Object target, boolean value);
}
