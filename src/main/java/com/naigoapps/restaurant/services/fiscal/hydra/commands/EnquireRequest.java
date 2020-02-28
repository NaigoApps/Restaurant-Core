package com.naigoapps.restaurant.services.fiscal.hydra.commands;

public class EnquireRequest extends AbstractRequest {

    public static final byte CODE = 0x05;

    @Override
    public byte[] getBytes() {
        return new byte[]{CODE};
    }

    @Override
    public int getExpectedResponses() {
        return 1;
    }

    @Override
    public String toString() {
        return "ENQUIRE";
    }
}
