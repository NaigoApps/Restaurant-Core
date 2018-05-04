/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author naigo
 */
@ApplicationScoped
public class Locks {
    private final Object ORDINATION_PROGRESSIVE;
    private final Object RECEIPT_PROGRESSIVE;
    private final Object INVOICE_PROGRESSIVE;

    public Locks() {
        ORDINATION_PROGRESSIVE = new Object();
        RECEIPT_PROGRESSIVE = new Object();
        INVOICE_PROGRESSIVE = new Object();
    }

    public Object ORDINATION_PROGRESSIVE() {
        return ORDINATION_PROGRESSIVE;
    }

    public Object INVOICE_PROGRESSIVE() {
        return INVOICE_PROGRESSIVE;
    }

    public Object RECEIPT_PROGRESSIVE() {
        return RECEIPT_PROGRESSIVE;
    }
    
    
    
    
}
