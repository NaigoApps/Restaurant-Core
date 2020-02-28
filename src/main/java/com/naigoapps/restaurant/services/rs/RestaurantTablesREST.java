/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.services.dto.WrapperDTO;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.builders.RestaurantTableBuilder;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;
import com.naigoapps.restaurant.services.dto.mappers.RestaurantTableMapper;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/restaurant-tables")
@RestController
@Transactional
public class RestaurantTablesREST {
    
    @Autowired
    private RestaurantTableDao tablesDao;

    @Autowired
    private EveningManager manager;
    
    @Autowired
    private RestaurantTableMapper mapper;
    
    @GetMapping
    public List<RestaurantTableDTO> getAllTables() {
        return tablesDao.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GetMapping("{uuid}")
    public RestaurantTableDTO find(@PathVariable("uuid") String uuid) {
    	return mapper.map(tablesDao.findByUuid(uuid));
    }
    
    @GetMapping("available")
    public List<RestaurantTableDTO> getAvailableTables() {
        
        List<RestaurantTable> open = manager.getSelectedEvening().getDiningTables().stream()
                .filter(table -> table.getStatus().equals(DiningTableStatus.OPEN))
                .map(DiningTable::getTable)
                .collect(Collectors.toList());
        
        return tablesDao.findAll().stream()
        		.filter(table -> !open.contains(table))
                .map(mapper::map)
                .collect(Collectors.toList());
        
    }
    
    @PostMapping
    public String createTable(){
        RestaurantTable table = new RestaurantTableBuilder()
                .getContent();
        
        tablesDao.persist(table);
        
        return table.getUuid();
    }
    
    @PutMapping("{uuid}/name")
    public RestaurantTableDTO updateTableName(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> name){
        RestaurantTable rt = tablesDao.findByUuid(uuid);
        rt.setName(name.getValue());
        return mapper.map(rt);
    }
    
    @DeleteMapping("{uuid}")
    public void deleteTable(@PathVariable("uuid") String uuid){
        tablesDao.deleteByUuid(uuid);   
    }
}
