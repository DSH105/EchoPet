package io.github.dsh105.echopet.reflection;

public interface MethodAccessor<T> {

    T invoke(Object instance, Object... args);

}
