/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing.services;

import com.naigoapps.restaurant.model.Printer;

/**
 *
 * @author naigo
 */
public class PrintingServiceProvider {
	public static PrintingService get(Printer p) {
//		return new ThermalPrintingService(p);
		return new StringsPrintingService(p);
	}
}
