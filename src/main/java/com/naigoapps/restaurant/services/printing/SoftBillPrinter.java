/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.services.printing.services.PrintingService;

/**
 *
 * @author naigo
 */
public class SoftBillPrinter implements ObjectPrinter<Bill> {

	private boolean generic;

	public SoftBillPrinter(boolean generic) {
		this.generic = generic;
	}

	@Override
	public PrintingService apply(PrintingService ps, Bill obj, LocalDateTime time) throws IOException {

		ps.printCenter("Tavolo " + obj.getTable().getTable().getName())
				.printCenter(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).lf();

		if (obj.getCoverCharges() > 0) {
			float ccs = obj.getTable().getEvening().getCoverCharge() * obj.getCoverCharges();
			ps.printLine(obj.getCoverCharges() + " COPERTI", PrintingService.formatPrice(ccs));
		}
		if (generic) {
			ps.accept(new GenericReviewPrinter(), obj.getOrders(), time);
		} else {
			ps.accept(new CategoriesReviewPrinter(), obj.getOrders(), time);
		}

		float estimatedTotal = obj.getEstimatedTotal();
		float finalTotal = obj.getTotal();
		if (finalTotal > estimatedTotal) {
			ps.printLine("MAGGIORAZIONE", PrintingService.formatPrice(finalTotal - estimatedTotal));
		} else if (finalTotal < estimatedTotal) {
			ps.printLine("SCONTO", PrintingService.formatPrice(estimatedTotal - finalTotal));
		}

		ps.separator('-').printLine("TOT: ", PrintingService.formatPrice(obj.getTotal()));

		return ps;
	}

	public boolean isGeneric() {
		return generic;
	}

}
