package io.github.dsh105.echopet.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringSimplifier {

    // From http://stackoverflow.com/a/1453284

    private static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    public static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }
}