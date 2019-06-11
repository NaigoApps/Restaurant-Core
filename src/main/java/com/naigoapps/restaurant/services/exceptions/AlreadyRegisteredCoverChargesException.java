/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.exceptions;

/**
 *
 * @author naigo
 */
public class AlreadyRegisteredCoverChargesException extends RuntimeException {
    public AlreadyRegisteredCoverChargesException(String msg) {
    	super(msg);
    }
}
