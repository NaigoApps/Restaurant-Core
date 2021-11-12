package com.naigoapps.restaurant.services.fiscal.hydra;

import java.util.function.Consumer;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HydraGateway {

    private final CamelContext camelContext;

    private final ReplyHandler handler;

    @Autowired
    public HydraGateway(CamelContext camelContext,
        ReplyHandler handler) {
        this.camelContext = camelContext;
        this.handler = handler;
    }

    public void send(byte[] content, Consumer<byte[]> callback) {
        handler.setCallback(callback);
        camelContext.createProducerTemplate()
            .sendBody("direct:toHydra", content);
    }

}
