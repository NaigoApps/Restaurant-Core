/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.builders;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author naigo
 */
public class EveningBuilderTest {
    
    private EveningBuilder builder;
    
    @Before
    public void setUp() {
        builder = new EveningBuilder();
    }

    @Test
    public void testDay() {
        LocalDate day = LocalDate.now();
        assertEquals(day, builder.day(day).getContent().getDay());
    }

    @Test
    public void testCoverCharge() {
        assertEquals(1.0f, builder.coverCharge(1.0f).getContent().getCoverCharge(), 0.0f);
    }

    @Test
    public void testDiningTable() {
        DiningTable table = new DiningTableBuilder().getContent();
        Evening result = builder.diningTable(table).getContent();
        assertEquals(result, table.getEvening());
        assertTrue(result.getDiningTables().contains(table));
    }

}
