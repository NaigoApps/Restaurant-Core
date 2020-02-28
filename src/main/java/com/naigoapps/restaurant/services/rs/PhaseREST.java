/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.PhaseDTO;
import com.naigoapps.restaurant.services.dto.mappers.PhaseMapper;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/phases")
@RestController
public class PhaseREST {
    
    @Autowired
    private PhaseDao pDao;
    
    @Autowired
    private PhaseMapper mapper;
    
    @GetMapping
    public List<PhaseDTO> findAll() {
        return pDao.findAll().stream()
        	.map(mapper::map)
        	.collect(Collectors.toList());
    }
    
}
