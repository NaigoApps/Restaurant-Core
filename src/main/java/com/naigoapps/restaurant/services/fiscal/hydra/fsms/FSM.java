package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.State.StandardEvents.TIMEOUT;

import com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FSM {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected byte[] data;

    private Map<StateNames, State> statuses;

    private State currentState;

    private Semaphore semaphore;

    public FSM() {
        semaphore = new Semaphore(0);
        statuses = new HashMap<>();
    }

    public void start() {
        Set<State> ss = define();
        ss.forEach(s -> statuses.put(s.getName(), s));
        LOGGER.info("Starting {}", getClass());
        this.statuses.forEach((k, v) -> {
            LOGGER.info("State {}:", k);
            v.getTransitions().forEach((evt, state) -> {
                LOGGER.info("Event {} -> {}", evt, state);
            });
        });

        this.currentState = statuses.get(initialState());
        this.currentState.onEnter();
        waitForEvent();
    }

    protected abstract Set<State> define();

    protected abstract StateNames initialState();

    protected abstract Set<StateNames> endingStates();

    public synchronized void fire(Events evt) {
        LoggerFactory.getLogger(getClass()).info("Firing {} on state {}", evt, currentState);
        LoggerFactory.getLogger(getClass()).info("May react...");
        currentState.react(evt);
        StateNames newStateName = currentState.on(evt);
        if (newStateName != null) {
            LoggerFactory.getLogger(getClass()).info("Going to {}", newStateName);
            State newState = statuses.get(newStateName);
            if (newState != null) {
                this.currentState = newState;
                LoggerFactory.getLogger(getClass()).info("Executing onEnter of {}", currentState);
                this.currentState.onEnter();
                semaphore.release();
                LoggerFactory.getLogger(getClass()).info("Executed onEnter of {}", currentState);
                if (endingStates().contains(this.currentState.getName())) {
                    LoggerFactory.getLogger(getClass())
                        .info("Reached ending state {}", currentState);
                }
            }else{
                semaphore.release();
            }
        }else{
            LoggerFactory.getLogger(getClass()).info("No transitions for event {} on {}", evt, currentState);
            semaphore.release();
        }

    }

    private void waitForEvent() {
        while(!endingStates().contains(currentState.getName())) {
            boolean ok;
            if (this.currentState.getTimeout() > 0) {
                try {
                    LoggerFactory.getLogger(getClass()).info("Waiting on {}", currentState);
                    ok = semaphore
                        .tryAcquire(this.currentState.getTimeout(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ok = false;
                }
            } else {
                try {
                    semaphore.acquire();
                    ok = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ok = false;
                }
            }
            if (!ok) {
                LoggerFactory.getLogger(getClass()).info("Wait failed. Now on {}", currentState);
                new Thread(() -> fire(TIMEOUT)).start();
            }else{
                LoggerFactory.getLogger(getClass()).info("Waited. Now on {}", currentState);
            }
        }
    }

    protected StatesBuilder statuses() {
        return new StatesBuilder();
    }

    public StateNames getCurrentStatus() {
        return currentState.getName();
    }

    public byte[] getData() {
        return data;
    }
}
