package com.naigoapps.restaurant.services.fiscal.hydra;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HydraCommandsBuilder {

	private StringBuilder builder;
	
	public HydraCommandsBuilder() {
		builder = new StringBuilder();
	}
	
	public HydraCommandsBuilder openReceipt() {
		return field(">")
				.field(/*Comment*/)
				.field(/*Comment*/)
				.field(/*Comment*/)
				.endl();
	}
	
	public HydraCommandsBuilder itemSale(String item, String details, int qty, double price, int dep) {
		return field("3")
				.field("S")
				.field(trim(item, 30))
				.field(trim(details, 30))
				.field(integer(qty))
				.field(amount(price))
				.field(integer(dep))
				.field(/*VAT*/)
				.endl();
	}
	
	public HydraCommandsBuilder itemBack(String item, String details, int qty, double price, int dep, int vat) {
		return field("3")
				.field("N")
				.field(trim(item, 30))
				.field(trim(details, 30))
				.field(integer(qty))
				.field(amount(price))
				.field(integer(dep))
				.field(/*VAT*/)
				.endl();
	}
	
	public HydraCommandsBuilder comment(String comment) {
		return field("7")
				.field("1")
				.field("1")
				.field(trim(comment, 48))
				.endl();
	}
	
	public HydraCommandsBuilder pay() {
		return field("5")
				.field("1")
				.field("0.00")
				.field(/*PAY DESC*/)
				.field(/*N.A.*/)
				.field(/*PAY EXT DESC*/)
				.field(/*PAY TYPE*/)
				.endl();
	}
	
	public HydraCommandsBuilder cashback(LocalDate date, int zNumber, int docNumber) {
		return field("-")
				.field(date.format(DateTimeFormatter.ofPattern("ddMMuuuu")))
				.field(integer(zNumber))
				.field(integer(docNumber))
				.field(/*DEVICE*/)
				.endl();
	}
	
	public HydraCommandsBuilder cancelReceipt(LocalDate date, int zNumber, int docNumber) {
		return field("+")
				.field(integer(1))
				.field(date.format(DateTimeFormatter.ofPattern("ddMMuuuu")))
				.field(integer(zNumber))
				.field(integer(docNumber))
				.endl();
	}
	
	
	
	public String build() {
		return builder.toString();
	}
	
	private HydraCommandsBuilder field() {
		return field("");
	}
	
	private HydraCommandsBuilder endl() {
		builder.append('\n');
		return this;
	}
	
	private HydraCommandsBuilder field(String field) {
		builder.append(field).append('/');
		return this;
	}

	private String trim(String item, int size) {
		if(item.length() > size) {
			return item.substring(0, size - 1) + ".";
		}
		return item;
	}
	
	private String integer(int val) {
		return String.valueOf(val);
	}
	
	private String amount(double val) {
		return String.format("%5.2f", val);
	}
	
}
