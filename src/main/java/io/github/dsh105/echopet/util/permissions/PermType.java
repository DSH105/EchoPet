package io.github.dsh105.echopet.util.permissions;


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