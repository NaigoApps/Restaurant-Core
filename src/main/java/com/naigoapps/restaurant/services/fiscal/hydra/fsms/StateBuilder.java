package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.State.StandardEvents.TIMEOUT;

import java.util.HashMap;
import java.util.Map;

public class StateBuilder {

    private StateNames name;
    private StatesBuilder returnTo;
    private Runnable onEnter;
    private Map<Events, StateNames> transitions;
    private Map<Events, Runnable> reactions;
    private long timeout;

    public StateBuilder(StateNames name, StatesBuilder returnTo) {
        this.name = name;
        this.returnTo = returnTo;
        this.transitions = new HashMap<>();
        this.reactions = new HashMap<>();
    }

    public StateBuilder onEnter(Runnable runnable) {
        this.onEnter = runnable;
        return this;
    }

    public StateBuilder transition(Events evt, StateNames destination) {
        transitions.put(evt, destination);
        return this;
    }

    public StateBuilder reaction(Events evt, Runnable runnable) {
        reactions.put(evt, runnable);
        return this;
    }

    public StatesBuilder end(){
        State result = new State(name, onEnter, transitions, reactions);
        result.setTimeout(timeout);
        return returnTo.state(result);
    }

    public StateBuilder timeout(long timeout, StateNames timeoutState) {
        this.timeout = timeout;
        transitions.put(TIMEOUT, timeoutState);
        return this;
    }
}
