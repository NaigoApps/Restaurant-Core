/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.utils.WeldTestRunner;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 *
 * @author naigo
 */
@RunWith(WeldTestRunner.class)
public abstract class AbstractPersistenceTest {

    private EntityManagerFactory emf;
    
    private EntityManager entityManager;
    
    @Inject
    @Generic
    protected GenericDao genericDao;
    
    @Inject
    protected AdditionDao additionDao;
    
    @Inject
    protected CategoryDao categoryDao;
    
    @Inject
    protected DishDao dishDao;
    
    @Inject
    protected WaiterDao waiterDao;
    
    @Inject
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
