package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import java.util.Map;
import java.util.Objects;

public class State {

    private StateNames name;
    private Runnable onEnter;
    private Map<Events, StateNames> transitions;
    private Map<Events, Runnable> reactions;

    private long timeout;

    public State(StateNames name, Runnable onEnter, Map<Events, StateNames> transitions, Map<Events, Runnable> reactions) {
        this.name = name;
        this.onEnter = onEnter;
        this.transitions = transitions;
        this.reactions = reactions;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void onEnter() {
        if (onEnter != null) {
            onEnter.run();
        }
    }

    public StateNames on(Events evt) {
        return transitions.get(evt);
    }

    public StateNames getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        State state = (State) o;
        return name.equals(state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public StateNames getTransition(Events evt) {
        return transitions.get(evt);
    }

    public void setTransition(Events evt, StateNames state) {
        this.transitions.put(evt, state);
    }

    public void react(Events evt){
        if(this.reactions.containsKey(evt)){
            this.reactions.get(evt).run();
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public Map<Events, StateNames> getTransitions() {
        return transitions;
    }

    public enum StandardEvents implements Events {
        TIMEOUT
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}
