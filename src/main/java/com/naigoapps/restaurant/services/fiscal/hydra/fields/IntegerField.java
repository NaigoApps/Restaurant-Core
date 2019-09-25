package com.naigoapps.restaurant.services.fiscal.hydra.fields;

import java.nio.charset.StandardCharsets;

public class IntegerField implements Field {

	private int value;
	private int digits;

	public IntegerField(int value) {
		this(value, -1);
	}

	public IntegerField(int value, int digits) {
		this.value = value;
		this.digits = digits;
	}

	public static IntegerField fromBytes(byte[] bytes) {
		return fromBytes(bytes, false);
	}

	public static IntegerField fromBytes(byte[] bytes, boolean hex) {
		String s = new String(bytes, StandardCharsets.US_ASCII);
		return new IntegerField(Integer.parseInt(s, hex ? 16 : 10));
	}
	
	@Override
	public byte[] value() {
		StringBuilder format = new StringBuilder();
		format.append('%');
		if(digits > 0) {
			format.append('0').append(digits);
		}
		format.append('d');
		return String.format(format.toString(), value).getBytes(StandardCharsets.US_ASCII);
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public int size() {
		if(digits != -1) {
			return digits;
		}
		return String.valueOf(value).length();
	}
}
