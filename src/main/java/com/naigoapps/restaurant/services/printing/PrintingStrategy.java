/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import java.io.IOException;

/**
 *
 * @author naigo
 */
public interface PrintingStrategy {
    public void print(PrintingService service, DiningTable table) throws IOException;
}
