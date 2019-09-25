package com.naigoapps.restaurant.services.fiscal.hydra.fields;

public class ByteField implements Field {

	private byte value;

	public ByteField(byte value) {
		this.value = value;
	}

	@Override
	public byte[] value() {
		return new byte[] { value };
	}

	@Override
	public int size() {
		return 1;
	}
}
