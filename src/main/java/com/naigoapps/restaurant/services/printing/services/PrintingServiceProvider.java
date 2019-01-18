/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing.services;

import com.naigoapps.restaurant.model.Printer;
import java.io.IOException;

/**
 *
 * @author naigo
 */
public class PrintingServiceProvider {
    //FIXME Implementare
    public static PrintingService get(Printer p) throws IOException{
        return new ThermalPrintingService(p);
    }
}
