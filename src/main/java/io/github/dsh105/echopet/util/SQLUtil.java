package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.entity.living.PetData;

import java.util.*;

public class SQLUtil {

    public static String serialise(PetData[] data, boolean mount) {
        String s = "";
        for (PetData pd : data) {
            if (!s.equalsIgnoreCase("")) {
                s = s + " varchar(255), ";
            }
            if (mount) {
                s = s + "Mount" + pd.toString();
            } else {
                s = s + pd.toString();
            }
        }
        s = s + " varchar(255)";
        return s;
    }

    public static String serialiseUpdate(List<PetData> data, Object value, boolean isMount) {
        String s = ", ";
        String mount = isMount ? "Mount" : "";
        if (!data.isEmpty()) {
            for (PetData pd : data) {
                if (!s.equalsIgnoreCase(", ")) {
                    s = s + ", ";
                }
                s = s + mount + "`" + pd.toString() + "` = '" + value.toString() + "'";
            }
        }
        if (s.equals(", ")) {
            return "";
        }
        return s;
    }

    public static Map<String, String> constructUpdateMap(List<PetData> data, Object value, boolean isMount) {
        Map<String, String> updateMap = new HashMap<String, String>();
        for (PetData pd : data) {
            updateMap.put((isMount ? "Mount" : "") + pd.toString(), value.toString());
        }
        return updateMap;
    }
}