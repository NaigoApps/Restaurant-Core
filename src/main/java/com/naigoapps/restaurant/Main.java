/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.builders.CategoryBuilder;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.model.builders.RestaurantTableBuilder;
import com.naigoapps.restaurant.model.builders.WaiterBuilder;
import com.naigoapps.restaurant.model.dao.AbstractDao;
import com.naigoapps.restaurant.model.dao.Generic;
import com.naigoapps.restaurant.model.dao.GenericDao;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author naigo
 */
@Startup
@Singleton
public class Main {

    @Inject @Generic
    private GenericDao dao;

    @PostConstruct
    public void init() {
        RestaurantTableBuilder tableBuilder = new RestaurantTableBuilder();
        WaiterBuilder waiterBuilder = new WaiterBuilder();
        CategoryBuilder categoryBuilder = new CategoryBuilder();
        DishBuilder dishBuilder = new DishBuilder();
//        dao.persist(
//                tableBuilder.name("T1").getContent(),
//                tableBuilder.name("T2").getContent(),
//                tableBuilder.name("T3").getContent()
//        );
//        dao.persist(
//                waiterBuilder.name("Lorenzo").surname("Niccolai").getContent(),
//                waiterBuilder.name("Eloisa").surname("Vannacci").getContent()
//        );
//        Category cat1 = categoryBuilder.name("Pizze").getContent();
//        Category cat2 = categoryBuilder.name("Primi").getContent();
//        dao.persist(cat1, cat2);
//        
//        dao.persist(
//                dishBuilder.name("Spaghetti alle vongole").price(9).category(cat2).getContent(),
//                dishBuilder.name("Ravioli maremmani").price(9).category(cat2).getContent(),
//                dishBuilder.name("Penne agli scampi").price(11).category(cat2).getContent(),
//                dishBuilder.name("Tagliolini ai funghi porcini").price(10).category(cat2).getContent(),
//                dishBuilder.name("Margherita").price(5).category(cat1).getContent(),
//                dishBuilder.name("Quattro formaggi").price(6).category(cat1).getContent(),
//                dishBuilder.name("Marinara").price(7).category(cat1).getContent(),
//                dishBuilder.name("Diavola").price(7.5f).category(cat1).getContent()
//        );
//        for(int i = 0;i < 20;i++){
//            dao.persist(dishBuilder
//                .name("DISH_" + i)
//                .price((int)(Math.random()*10 + 3))
//                .category(cat1)
//                .description("DESC_" + i)
//                .getContent());
//        }
        
    }
}
