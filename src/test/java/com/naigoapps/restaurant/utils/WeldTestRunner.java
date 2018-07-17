/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.utils;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 *
 * @author naigo
 */
public class WeldTestRunner extends BlockJUnit4ClassRunner{
    
    public WeldTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    @Override
    protected Object createTest() {
        final Class<?> test = getTestClass().getJavaClass();
        return WeldTestContext.INSTANCE.getBean(test);
    }
    
}
