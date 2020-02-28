package com.naigoapps.restaurant.services.fiscal.hydra.commands;

public class CancelReceiptRequest implements Request {

    public static final byte CODE = '+';

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
        return "CANCEL_RECEIPT";
    }

}
