package com.naigoapps.restaurant.services.fiscal.hydra;

import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
public class ReplyHandler {

    private Consumer<byte[]> callback;

    public synchronized void handle(@Body byte[] reply) {
        if (callback != null) {
			System.out.println("Accepting " + print(reply));
            callback.accept(reply);
			System.out.println("Accepted " + print(reply));
		}
    }

    public void setCallback(Consumer<byte[]> callback) {
        this.callback = callback;
    }

    private String print(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(b);
        }
        return result.toString();
    }
}
