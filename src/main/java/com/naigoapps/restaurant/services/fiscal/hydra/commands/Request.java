package com.naigoapps.restaurant.services.fiscal.hydra.commands;

public interface Request {

    byte[] getBytes();

    int getExpectedResponses();
}
