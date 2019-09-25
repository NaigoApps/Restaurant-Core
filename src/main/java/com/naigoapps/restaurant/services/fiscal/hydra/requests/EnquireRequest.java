package com.naigoapps.restaurant.services.fiscal.hydra.requests;

import java.util.Arrays;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;

public class EnquireRequest implements Request {

	public static final byte CODE = 0x05;

	@Override
	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE));
	}

	public byte[] getBytes() {
		return new byte[] { CODE };
	}
}
