/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.dao.StatisticsDao;
import com.naigoapps.restaurant.services.StatisticsService;
import com.naigoapps.restaurant.services.dto.StatisticsDTO;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.print.PrintException;
import java.io.IOException;
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
    private StatisticsService service;

    @GetMapping("dishes-profit/{from}/{to}")
    public StatisticsDTO getMostProfitableDishes(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) {
        return service.getMostSoldDishes(toDate(from), toDate(to));
    }

    @GetMapping("categories-profit/{from}/{to}")
    public StatisticsDTO getMostSoldCategories(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) {
        return service.getMostSoldCategories(toDate(from), toDate(to));
    }

    @GetMapping("print/{from}/{to}")
    public void printStatistics(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    ) throws PrintException, IOException {
        service.printStatistics(toDate(from), toDate(to));
    }

    private LocalDate toDate(@PathVariable("from") String from) {
        return LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE);
    }

}