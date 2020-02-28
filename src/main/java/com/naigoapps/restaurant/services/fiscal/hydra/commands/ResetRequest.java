package com.naigoapps.restaurant.services.fiscal.hydra.commands;

public class ResetRequest extends AbstractRequest {

    public static final byte CODE = 0x18;

    @Override
    public byte[] getBytes() {
        return new byte[]{CODE};
    }

    @Override
    public String toString() {
        return "RESET";
    }
}
