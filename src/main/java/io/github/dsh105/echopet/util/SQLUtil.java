package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.entity.PetData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLUtil {

    public static String serialise(PetData[] data, boolean isRider) {
        String s = "";
        for (PetData pd : data) {
            if (!s.equalsIgnoreCase("")) {
                s += " varchar(255), ";
            }
            if (isRider) {
                s = s + "Rider" + pd.toString();
            } else {
                s += pd.toString();
            }
        }
        s += " varchar(255)";
        return s;
    }

    public static String serialiseUpdate(List<PetData> data, Object value, boolean isRider) {
        String s = ", ";
        String rider = isRider ? "Rider" : "";
        if (!data.isEmpty()) {
            for (PetData pd : data) {
                if (!s.equalsIgnoreCase(", ")) {
                    s += ", ";
                }
                s = s + rider + "`" + pd.toString() + "` = '" + value.toString() + "'";
            }
        }
        if (s.equals(", ")) {
            return "";
        }
        return s;
    }

    public static Map<String, String> constructUpdateMap(List<PetData> data, Object value, boolean isRider) {
        Map<String, String> updateMap = new HashMap<String, String>();
        for (PetData pd : data) {
            updateMap.put((isRider ? "Rider" : "") + pd.toString(), value.toString());
        }
        return updateMap;
    }
}