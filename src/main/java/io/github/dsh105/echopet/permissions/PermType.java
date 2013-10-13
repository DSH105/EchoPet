package io.github.dsh105.echopet.permissions;

/**
 * Project by DSH105
 */

public enum PermType {

    BASE("echopet.pet.*"),
    ADMIN("echopet.petadmin.*");

    String perm;
    PermType(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return this.perm;
    }
}