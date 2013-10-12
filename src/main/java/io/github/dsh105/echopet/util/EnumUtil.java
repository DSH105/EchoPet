package com.github.dsh105.echopet.util;


public class EnumUtil {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isEnumType(Class<? extends Enum> clazz, String s) {
		try {
			Enum.valueOf(clazz, s.toUpperCase());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
