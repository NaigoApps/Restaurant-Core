/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author naigo
 */
public class BillPrinter implements ObjectPrinter<Bill> {

    private boolean generic;
    private String headers;

    public BillPrinter(boolean generic, String headers) {
        this.generic = generic;
        this.headers = headers;
    }

    @Override
    public PrintingService apply(PrintingService ps, Bill obj, LocalDateTime time) throws IOException {

        ps.size(PrintingService.Size.STANDARD)
                .lf(3);

        if(StringUtils.isNotEmpty(headers)){
            String[] split = headers.split("\n");
            for(String s : split){
                ps.printCenter(StringUtils.abbreviate(s, ps.getPrinter().getLineCharacters()));
            }
        }

        ps.lf(1);

        if (obj.getPrintTime() != null) {
            ps.printCenter("RICEVUTA " + obj.getProgressive());
        }
        ps.printCenter("Tavolo " + obj.getTable().getTable().getName())
                .printCenter(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .lf();

        if (generic) {
            ps.accept(new GenericReviewPrinter(), obj, time);
        } else {
            ps.accept(new CategoriesReviewPrinter(), obj, time);
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

    public boolean isGeneric() {
        return generic;
    }


}
