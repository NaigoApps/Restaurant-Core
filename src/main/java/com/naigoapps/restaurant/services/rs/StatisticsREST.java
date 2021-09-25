/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.dao.StatisticsDao;
import com.naigoapps.restaurant.services.dto.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author naigo
 */
@Transactional
@RestController
@RequestMapping("/rest/statistics")
public class StatisticsREST {

    @Autowired
    private StatisticsDao dao;

    @GetMapping("dishes-amount/{from}/{to}")
    public StatisticsDTO getMostSoldDishes(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) {
        return dao.getMostSoldDishes(toDate(from), toDate(to));
    }

    @GetMapping("dishes-profit/{from}/{to}")
    public StatisticsDTO getMostProfitableDishes(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) {
        return dao.getMostProfitableDishes(toDate(from), toDate(to));
    }

    @GetMapping("categories-amount/{from}/{to}")
    public StatisticsDTO getMostSoldCategories(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) {
        return dao.getMostSoldCategories(toDate(from), toDate(to));
    }

    @GetMapping("categories-profit/{from}/{to}")
    public StatisticsDTO getMostProfitableCategories(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) {
        return dao.getMostProfitableCategories(toDate(from), toDate(to));
    }

    private LocalDate toDate(@PathVariable("from") String from) {
        return LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
