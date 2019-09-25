package com.naigoapps.restaurant.services.utils;

public class Utils {

	private Utils() {
	}

	public static boolean equals(String a, String b) {
		if (a == null) {
			return b == null;
		} else {
			return a.equals(b);
		}
	}
}
