/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.builders.CustomerBuilder;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.model.builders.EveningBuilder;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.builders.PrinterBuilder;
import com.naigoapps.restaurant.model.builders.RestaurantTableBuilder;
import com.naigoapps.restaurant.services.PrinterService;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class BillPrinterTest {

    private BillPrinter bPrinter;

    private Printer printer;
    private PrinterService service;

    @Before
    public void setUp() {
        printer = new PrinterBuilder()
                .line(20)
                .main(true)
                .name("printer")
                .getContent();
        service = new PrinterService(printer);
        service.setDebugMode(true);
    }

    @Test
    public void testConstruct1() {
        bPrinter = new BillPrinter(true);
        assertTrue(bPrinter.isGeneric());
        assertNull(bPrinter.getCustomer());
    }

    @Test
    public void testConstruct2() {
        Customer c = new CustomerBuilder().getContent();
        bPrinter = new BillPrinter(false, c);
        assertFalse(bPrinter.isGeneric());
        assertEquals(c, bPrinter.getCustomer());
    }

    @Test
    public void testApplyGenericReceipt() throws Exception {
        bPrinter = new BillPrinter(true);
        LocalDateTime time = LocalDateTime.of(2018, Month.MARCH, 3, 19, 19, 19);
        Bill b = bill(dTable(evening(2), rTable("T1")), 20, order("A", 1), order("B", 3), order("C", 6), order("D", 2), order("E", 7));
        bPrinter.apply(service, b, time);
        assertEquals("\n\n\nTavolo T1\n"
                + "03-03-2018 19:19\n\n"
                + "0 COPERTI       0,00\n"
                + "GENERICO       19,00\n"
                + "MAGGIORAZIONE   1,00\n"
                + "--------------------\n"
                + "TOT:           20,00\n",
                service.getText());
    }

    private Evening evening(float cc) {
        return new EveningBuilder().coverCharge(cc).getContent();
    }

    private RestaurantTable rTable(String name) {
        return new RestaurantTableBuilder().name(name).getContent();
    }

    private DiningTable dTable(Evening e, RestaurantTable rt) {
        return new DiningTableBuilder().evening(e).table(rt).getContent();
    }

    private Bill bill(DiningTable dt, float total, Order... o) {
        List<Order> orders = Arrays.stream(o).collect(Collectors.toList());
        Bill b = new BillBuilder()
                .table(dt)
                .total(total)
                .orders(orders)
                .getContent();
        return b;
    }

    private Dish dish(String name, float price) {
        return new DishBuilder().name(name).price(price).getContent();
    }

    private Order order(String name, float price) {
        return new OrderBuilder()
                .dish(dish(name, 0))
                .price(price)
                .getContent();

    }

}
