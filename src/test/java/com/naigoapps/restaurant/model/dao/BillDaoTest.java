/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.builders.CustomerBuilder;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.builders.EveningBuilder;
import com.naigoapps.restaurant.utils.WeldTestRunner;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
@RunWith(WeldTestRunner.class)
public class BillDaoTest extends AbstractPersistenceTest {
    
    private final LocalDateTime date = LocalDateTime.of(2018, Month.JUNE, 20, 19, 0);
    private final LocalDateTime yearBefore = date.minus(Period.ofYears(1));
    private final LocalDateTime dayBefore = date.minus(Period.ofDays(1));
    private final LocalDateTime sameDay = date.minus(Duration.ofHours(1));

    private final Customer sample = new CustomerBuilder().getContent();
    private final DiningTable table = new DiningTableBuilder().getContent();
    private final Evening evening1 = new EveningBuilder().day(date.toLocalDate()).getContent();
    private final Evening evening2 = new EveningBuilder().day(date.toLocalDate().plusDays(1)).getContent();

    @Test
    public void testNextBillProgressive() {
        assertEquals(11, billDao.nextBillProgressive(evening1));
        assertEquals(1, billDao.nextBillProgressive(evening2));
    }

    @Test
    public void testNextReceiptProgressive() {
        assertEquals(7, billDao.nextReceiptProgressive(date.toLocalDate().minusDays(1)));
        assertEquals(8, billDao.nextReceiptProgressive(date.toLocalDate()));
        assertEquals(1, billDao.nextReceiptProgressive(date.toLocalDate().plusDays(1)));
    }

    @Ignore
    @Test
    public void testNextInvoiceProgressive() {
        assertEquals(18, billDao.nextInvoiceProgressive(date.toLocalDate().minusDays(1)));
        assertEquals(18, billDao.nextInvoiceProgressive(date.toLocalDate()));
        assertEquals(18, billDao.nextInvoiceProgressive(date.toLocalDate().plusDays(1)));
    }

    @Test
    public void testGetEntityClass() {
        assertEquals(Bill.class, billDao.getEntityClass());
    }

    @Override
    public void insertData() {
        Bill b1 = new BillBuilder().progressive(10).getContent();
        b1.setTable(table);
        table.setEvening(evening1);
        Bill r1 = new BillBuilder().progressive(5).printTime(yearBefore).getContent();
        Bill r2 = new BillBuilder().progressive(6).printTime(dayBefore).getContent();
        Bill r3 = new BillBuilder().progressive(7).printTime(sameDay).getContent();
        Bill i1 = new BillBuilder().progressive(15).printTime(yearBefore).customer(sample).getContent();
        Bill i2 = new BillBuilder().progressive(16).printTime(dayBefore).customer(sample).getContent();
        Bill i3 = new BillBuilder().progressive(17).printTime(sameDay).customer(sample).getContent();
        billDao.persist(b1, r1, r2, r3, i1, i2, i3, sample, table, evening1, evening2);
    }

}
