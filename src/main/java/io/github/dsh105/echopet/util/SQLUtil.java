package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.entity.living.data.PetData;

import java.util.ArrayList;

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

    public static String serialiseDataList(ArrayList<PetData> data, boolean mount) {
        String s = "";
        if (!data.isEmpty()) {
            for (PetData pd : data) {
                if (!s.equalsIgnoreCase("")) {
                    s = s + ", ";
                }
                if (mount) {
                    s = s + "Mount" + pd.toString();
                } else {
                    s = s + pd.toString();
                }
            }
        }
        return s;
    }

    public static String serialiseDataListBooleans(ArrayList<PetData> data, Boolean result) {
        String s = "'";
        if (!data.isEmpty()) {
            for (PetData pd : data) {
                if (!s.equalsIgnoreCase("'")) {
                    s = s + "', '";
                }
                s = s + result.toString();
            }
        }
        s = s + "'";
        if (s.equalsIgnoreCase("''")) {
            s = "";
        }
        return s;
    }

    public static String serialiseUpdate(ArrayList<PetData> data, Object value, boolean isMount) {
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
}