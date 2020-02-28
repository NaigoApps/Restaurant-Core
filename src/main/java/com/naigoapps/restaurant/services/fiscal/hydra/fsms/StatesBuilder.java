package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.State.StandardEvents.TIMEOUT;

import java.util.HashSet;
import java.util.Set;

public class StatesBuilder {

    private Set<State> states;
    private StateNames timeoutState;
    private long timeoutTime;

    public StatesBuilder() {
        states = new HashSet<>();
    }

    public StateBuilder state(StateNames name) {
        return new StateBuilder(name, this);
    }

    public StatesBuilder state(State state) {
        this.states.add(state);
        return this;
    }

    public Set<State> build(){
        if(timeoutState != null){
            states.forEach(state -> {
                if(state.getTransition(TIMEOUT) == null){
                    state.setTimeout(timeoutTime);
                    state.setTransition(TIMEOUT, timeoutState);
                }
            });
        }
        return states;
    }

    public StatesBuilder onTimeout(long timeout, StateNames timeoutState) {
        this.timeoutTime = timeout;
        this.timeoutState = timeoutState;
        return this;
    }
}
