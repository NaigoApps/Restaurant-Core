/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.services.PrinterService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author naigo
 */
public class BillPrinter implements ObjectPrinter<Bill> {

    private boolean generic;
    private Customer customer;

    public BillPrinter(boolean generic, Customer c) {
        this.generic = generic;
        this.customer = c;
    }

    public BillPrinter(boolean generic) {
        this(generic, null);
    }

    @Override
    public PrinterService apply(PrinterService ps, Bill obj) throws IOException {

        ps.size(PrinterService.Size.STANDARD)
                .lf(3);
        if (obj.getPrintTime() != null) {
            if (customer != null) {
                ps.printCenter("FATTURA " + obj.getProgressive());
                ps.printCenter(customer.getName() + " " + customer.getSurname());
                ps.printCenter("CF: " + customer.getCf());
                ps.printCenter("P.IVA: " + customer.getPiva());
            } else {
                ps.printCenter("RICEVUTA " + obj.getProgressive());
            }
        }
        ps.printCenter("Tavolo " + obj.getTable().getTable().getName())
                .printCenter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .lf()
                .printLine(obj.getCoverCharges() + " COPERTI",
                        PrinterService.formatPrice(obj.getTable().getEvening().getCoverCharge() * obj.getCoverCharges()));

        if(generic){
            ps.accept(new GenericReviewPrinter(), obj.getOrders());
        }else{
            ps.accept(new CategoriesReviewPrinter(), obj.getOrders());
        }

        float estimatedTotal = obj.getEstimatedTotal();
        float finalTotal = obj.getTotal();
        if (finalTotal > estimatedTotal) {
            ps.printLine("MAGGIORAZIONE", PrinterService.formatPrice(finalTotal - estimatedTotal));
        } else if (finalTotal < estimatedTotal) {
            ps.printLine("SCONTO", PrinterService.formatPrice(estimatedTotal - finalTotal));
        }

        ps.separator('-')
                .printLine("TOT: ", PrinterService.formatPrice(obj.getTotal()));

        return ps;
    }
}
