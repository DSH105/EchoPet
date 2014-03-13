package io.github.dsh105.echopet.reflection;

public abstract class SafeDirectField<T> implements FieldAccessor<T> {

    public boolean isValid() {
        return true;
    }

    @Override
    public T transfer(Object from, Object to) {
        T old = get(to);
        set(to, get(from));
        return old;
    }
}
