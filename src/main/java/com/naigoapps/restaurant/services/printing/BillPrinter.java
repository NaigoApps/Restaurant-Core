/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Receipt;
import com.naigoapps.restaurant.services.PrinterService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author naigo
 */
public class BillPrinter implements ObjectPrinter<Receipt>{

    @Override
    public PrinterService apply(PrinterService ps, Receipt obj) throws IOException {
        
        ps.size(PrinterService.Size.STANDARD)
                .lf(3)
//                .printCenter("Tavolo " + obj.getTable().getName())
                .printCenter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .lf()
                .printLine(obj.getCoverCharges() + " COPERTI",
                        PrinterService.formatPrice(obj.getTable().getEvening().getCoverCharge() * obj.getCoverCharges()))
                .accept(new CategoriesReviewPrinter(), obj.getOrders())
                .separator('-')
                .printLine("TOT: ", PrinterService.formatPrice(obj.getTotal()));
                
        return ps;
    }
}
