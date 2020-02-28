package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import com.naigoapps.restaurant.services.fiscal.hydra.Codes;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraGateway;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.EnquireRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.Request;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.ResetRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.LoggerFactory;

public abstract class BasicHydraFSM extends FSM {

    private HydraGateway gateway;

    protected BasicHydraFSM(HydraGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    protected StateNames initialState() {
        return BasicStates.CANCELING;
    }

    @Override
    protected Set<StateNames> endingStates() {
        return new HashSet<>(Arrays.asList(BasicStates.SUCCESS, BasicStates.FAILURE));
    }

    @Override
    protected Set<State> define() {
        //@formatter:off
        StatesBuilder builder = statuses()
            .onTimeout(3000, BasicStates.FAILURE)
            .state(BasicStates.CANCELING)
                .onEnter(sendCancelRunnable())
                .timeout(1000, firstState())
                .transition(BasicEvents.ACK_ARRIVED, firstState())
                .transition(BasicEvents.NAK_ARRIVED, firstState())
            .end();

        defineStates(builder);

        return builder.state(BasicStates.FAILURE)
            .end()
            .state(BasicStates.SUCCESS)
            .end()
            .build();
        //@formatter:on
    }

    protected Runnable sendCancelRunnable() {
        return sendCommandRunnable(new ResetRequest());
    }

    protected Runnable sendEnquireRunnable() {
        return sendCommandRunnable(new EnquireRequest());
    }

    protected Runnable sendCommandRunnable(Request request) {
        return sendCommandRunnable(request, false);
    }

    protected Runnable sendCommandRunnable(Request request, boolean saveResponse) {
        return () -> {
            new Thread(() -> {
                LoggerFactory.getLogger(getClass()).info("Sending {}", request);
                gateway.send(request.getBytes(), data -> {
                    if(saveResponse){
                        this.data = data;
                    }
                    if (isAck(data)) {
                        fire(BasicEvents.ACK_ARRIVED);
                    } else if (isNak(data)) {
                        fire(BasicEvents.NAK_ARRIVED);
                    } else {
                        fire(BasicEvents.STATUS_ARRIVED);
                    }
                });
            }).start();
        };
    }

    protected boolean isAck(byte[] data) {
        return data != null && data.length == 1 && data[0] == Codes.ACK;
    }

    protected boolean isNak(byte[] data) {
        return data != null && data.length == 1 && data[0] == Codes.NACK;
    }

    protected abstract void defineStates(StatesBuilder builder);

    protected abstract StateNames firstState();

    public enum BasicStates implements StateNames {
        CANCELING, ENQUIRING, SUCCESS, FAILURE
    }

    public enum BasicEvents implements Events {
        ACK_ARRIVED, NAK_ARRIVED, STATUS_ARRIVED
    }
}
