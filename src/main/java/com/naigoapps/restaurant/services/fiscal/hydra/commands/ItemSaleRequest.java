package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.StringField;
import java.util.Arrays;
import java.util.List;

public class ItemSaleRequest extends AbstractRequest {

    private final byte CODE = '3';

    private String item;
    private String details;
    private int quantity;
    private double price;
    private int department;

    public ItemSaleRequest(String item, String details, int quantity, double price, int department){
        this.item = item;
        this.details = details;
        this.quantity = quantity;
        this.price = price;
        this.department = department;
    }

    @Override
    public byte[] getBytes() {
        return wrap(getContent());
    }

    private List<Field> getContent(){
        return Arrays.asList(
            new ByteField(CODE),
            new StringField("S"),
            new StringField(trim(item, 30)),
            new StringField(trim(details, 30)),
            new IntegerField(quantity),
            new StringField(amount(price)),
            new IntegerField(department),
            new StringField("")
        );
    }

    @Override
    public String toString() {
        return "ITEM SALE";
    }
}
