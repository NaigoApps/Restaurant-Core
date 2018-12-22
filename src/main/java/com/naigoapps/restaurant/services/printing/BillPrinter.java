/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
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
    public PrintingService apply(PrintingService ps, Bill obj, LocalDateTime time) throws IOException {

        ps.size(PrintingService.Size.STANDARD)
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
                .printCenter(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .lf()
                .printLine(obj.getCoverCharges() + " COPERTI",
                        PrintingService.formatPrice(obj.getTable().getEvening().getCoverCharge() * obj.getCoverCharges()));

        if(generic){
            ps.accept(new GenericReviewPrinter(), obj.getOrders(), time);
        }else{
            ps.accept(new CategoriesReviewPrinter(), obj.getOrders(), time);
        }

        float estimatedTotal = obj.getEstimatedTotal();
        float finalTotal = obj.getTotal();
        if (finalTotal > estimatedTotal) {
            ps.printLine("MAGGIORAZIONE", PrintingService.formatPrice(finalTotal - estimatedTotal));
        } else if (finalTotal < estimatedTotal) {
            ps.printLine("SCONTO", PrintingService.formatPrice(estimatedTotal - finalTotal));
        }

        ps.separator('-')
                .printLine("TOT: ", PrintingService.formatPrice(obj.getTotal()));

        return ps;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isGeneric() {
        return generic;
    }
    
    
}
