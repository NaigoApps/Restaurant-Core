/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.naigoapps.restaurant.utils.WeldTestRunner;

/**
 *
 * @author naigo
 */
@RunWith(WeldTestRunner.class)
public abstract class AbstractPersistenceTest {

    private EntityManagerFactory emf;
    
    private EntityManager entityManager;
    
    @Autowired
    @Qualifier("generic")
    protected GenericDao genericDao;
    
    @Autowired
    protected AdditionDao additionDao;
    
    @Autowired
    protected CategoryDao categoryDao;
    
    @Autowired
    protected DishDao dishDao;
    
    @Autowired
    protected WaiterDao waiterDao;
    
    @Autowired
    protected BillDao billDao;
    
    @Before
    public void setUp(){
        emf = Persistence.createEntityManagerFactory("restaurant-test-pu");
        entityManager = emf.createEntityManager();
        initDaos();
        entityManager.getTransaction().begin();
        insertData();
        entityManager.getTransaction().commit();
    }

    private void initDaos(){
        genericDao.setEntityManager(entityManager);
        categoryDao.setEntityManager(entityManager);
        dishDao.setEntityManager(entityManager);
        additionDao.setEntityManager(entityManager);
        waiterDao.setEntityManager(entityManager);
        billDao.setEntityManager(entityManager);
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    @After
    public void tearDown(){
        entityManager.close();
        emf.close();
    }
    
    public void insertData(){
        
    }
    
}
