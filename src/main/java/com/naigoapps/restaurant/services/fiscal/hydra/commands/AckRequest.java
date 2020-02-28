package com.naigoapps.restaurant.services.fiscal.hydra.commands;

public class AckRequest extends AbstractRequest {

	public static final byte CODE = 0x06;

	@Override
	public byte[] getBytes() {
		return new byte[] { CODE };
	}

}
