package com.naigoapps.restaurant.services.fiscal.hydra;

import com.naigoapps.restaurant.services.fiscal.hydra.commands.ItemSaleRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.OpenReceiptRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.PayRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.Request;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HydraRequestsBuilder {

    private StringBuilder builder;

    private List<Request> requests;

    public HydraRequestsBuilder() {
        builder = new StringBuilder();
        requests = new ArrayList<>();
    }

    public HydraRequestsBuilder openReceipt() {
        requests.add(new OpenReceiptRequest());
        return this;
    }

    public HydraRequestsBuilder itemSale(String item, String details, int qty, double price,
        int dep) {
        requests.add(new ItemSaleRequest(item, details, qty, price, dep));
        return this;
    }

	public HydraRequestsBuilder pay() {
		requests.add(new PayRequest());
		return this;
	}

    public HydraRequestsBuilder itemBack(String item, String details, int qty, double price,
        int dep, int vat) {
        return field("3")
            .field("N")
            .field(trim(item, 30))
            .field(trim(details, 30))
            .field(integer(qty))
            .field(amount(price))
            .field(integer(dep))
            .field(/*VAT*/);
    }

    public HydraRequestsBuilder comment(String comment) {
        return field("7")
            .field("1")
            .field("1")
            .field(trim(comment, 48));
    }

    public HydraRequestsBuilder cashback(LocalDate date, int zNumber, int docNumber) {
        return field("-")
            .field(date.format(DateTimeFormatter.ofPattern("ddMMuuuu")))
            .field(integer(zNumber))
            .field(integer(docNumber))
            .field(/*DEVICE*/);
    }

    public HydraRequestsBuilder cancelReceipt(LocalDate date, int zNumber, int docNumber) {
        return field("+")
            .field(integer(1))
            .field(date.format(DateTimeFormatter.ofPattern("ddMMuuuu")))
            .field(integer(zNumber))
            .field(integer(docNumber));
    }


    public List<Request> build() {
        return requests;
    }

    private HydraRequestsBuilder field() {
        return field("");
    }

    private HydraRequestsBuilder field(String field) {
        builder.append(field).append('/');
        return this;
    }

    private String trim(String item, int size) {
        if (item.length() > size) {
            return item.substring(0, size - 1) + ".";
        }
        return item;
    }

    private String integer(int val) {
        return String.valueOf(val);
    }

    private String amount(double val) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        return format.format(val).substring(1);
    }

}
