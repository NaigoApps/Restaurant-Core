/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;
import com.naigoapps.restaurant.model.builders.WaiterBuilder;
import com.naigoapps.restaurant.utils.WeldTestRunner;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author naigo
 */
@RunWith(WeldTestRunner.class)
public class WaiterDaoTest extends AbstractPersistenceTest {

    @Test
    public void testGetOrderByClause() {
        assertEquals("name", waiterDao.getOrderByClause());
    }

    @Test
    public void testFindActive() {
        List<Waiter> actives = waiterDao.findActive();
        assertEquals(1, actives.size());
        assertEquals("A", actives.get(0).getName());
    }

    @Test
    public void testGetEntityClass() {
        assertEquals(Waiter.class, waiterDao.getEntityClass());
    }

    @Override
    public void insertData() {
        Waiter a = new WaiterBuilder().name("A").status(WaiterStatus.ACTIVE).getContent();
        Waiter b = new WaiterBuilder().name("B").status(WaiterStatus.SUSPENDED).getContent();
        Waiter c = new WaiterBuilder().name("C").status(WaiterStatus.REMOVED).getContent();
        waiterDao.persist(a, b, c);
    }

}
