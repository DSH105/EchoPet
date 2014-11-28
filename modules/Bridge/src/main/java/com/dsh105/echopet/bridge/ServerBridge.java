package com.dsh105.echopet.bridge;

import com.dsh105.commodus.StringUtil;

public enum ServerBridge {

    BUKKIT;

    private String classPrefix;

    ServerBridge() {
        this.classPrefix = StringUtil.capitalise(name());
    }

    ServerBridge(String classPrefix) {
        this.classPrefix = classPrefix;
    }

    public String getClassPrefix() {
        return classPrefix;
    }
}