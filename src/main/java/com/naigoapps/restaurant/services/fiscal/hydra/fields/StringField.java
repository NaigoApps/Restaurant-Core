package com.naigoapps.restaurant.services.fiscal.hydra.fields;

import java.nio.charset.StandardCharsets;

public class StringField implements Field {

	private String value;

	public StringField(String value) {
		this.value = value;
	}
	
	public static StringField fromBytes(byte[] bytes) {
		String s = new String(bytes, StandardCharsets.US_ASCII);
		return new StringField(s);
	}
	
	@Override
	public byte[] value() {
		return value.getBytes(StandardCharsets.US_ASCII);
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public int size() {
		return value.length();
	}
}
