package com.dsh105.echopet.api.entity.attribute;

import java.lang.reflect.Array;

public abstract class AttributeEnumBridge<B, S> implements EntityAttribute {

    private B bukkitEquivalent;
    private S spongeEquivalent;

    protected AttributeEnumBridge(B bukkitEquivalent, S spongeEquivalent) {
        this.bukkitEquivalent = bukkitEquivalent;
        this.spongeEquivalent = spongeEquivalent;
    }

    public static AttributeEnumBridge valueOf(String name) {
        return valueOf(enclosingClass(), name);
    }

    public static AttributeEnumBridge valueOf(String name, boolean allowTransformation) {
        return valueOf(enclosingClass(), name, allowTransformation);
    }

    public static AttributeEnumBridge valueOf(int ordinal) {
        return valueOf(enclosingClass(), ordinal);
    }

    public static <E extends AttributeEnumBridge> E valueOf(Class<E> enumType, String name) {
        return Attributes.valueOf(enumType, name);
    }

    public static <E extends AttributeEnumBridge> E valueOf(Class<E> enumType, String name, boolean allowTransformation) {
        return Attributes.valueOf(enumType, name, allowTransformation);
    }

    public static <E extends AttributeEnumBridge> E valueOf(Class<E> enumType, int ordinal) {
        return Attributes.valueOf(enumType, ordinal);
    }

    public static <E extends AttributeEnumBridge> E[] values(Class<E> enumType) {
        return Attributes.values(enumType).toArray((E[]) Array.newInstance(enumType));
    }

    public static <E extends AttributeEnumBridge> E[] values() {
        return (E[]) values(enclosingClass());
    }

    private static Class<? extends AttributeEnumBridge> enclosingClass() {
        return (Class<? extends AttributeEnumBridge>) new Object[]{}.getClass().getEnclosingClass();
    }

    public B getBukkitEquivalent() {
        return bukkitEquivalent;
    }

    public S getSpongeEquivalent() {
        return spongeEquivalent;
    }

    public String name() {
        return Attributes.nameOf(enclosingClass(), this);
    }

    public int ordinal() {
        return Attributes.ordinalOf(enclosingClass(), this);
    }

}