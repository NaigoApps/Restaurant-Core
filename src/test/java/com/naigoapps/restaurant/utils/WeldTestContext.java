/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.utils;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author naigo
 */
public class WeldTestContext {

    public static final WeldTestContext INSTANCE = new WeldTestContext();

    private final Weld weld;
    private final WeldContainer container;

    private WeldTestContext() {
        this.weld = new Weld();
        this.container = weld.initialize();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (container.isRunning()) {
                    weld.shutdown();
                }
            }
        });
    }

    public <T> T getBean(Class<T> type) {
        return container.select(type).get();
    }
}
