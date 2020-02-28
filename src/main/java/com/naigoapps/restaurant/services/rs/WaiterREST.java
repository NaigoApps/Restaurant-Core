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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import com.naigoapps.restaurant.services.dto.mappers.WaiterMapper;

/**
 *
 * @author naigo
 */
@Transactional
@RequestMapping("/rest/waiters")
@RestController
public class WaiterREST {

    @Autowired
    private WaiterDao waiterDao;

    @Autowired
    private DiningTableDao dtDao;

    @Autowired
    private WaiterMapper mapper;
    
    @GetMapping("active")
    public List<WaiterDTO> getAvailableWaiters() {
        return waiterDao.findActive().stream()
        		.filter(w -> WaiterStatus.REMOVED != w.getStatus())
        		.filter(w -> WaiterStatus.SUSPENDED != w.getStatus())
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<WaiterDTO> getAllWaiters() {
        return waiterDao.findAll().stream()
                .filter(w -> WaiterStatus.REMOVED != w.getStatus())
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GetMapping("{uuid}")
    public WaiterDTO findByUuid(@PathVariable("uuid") String uuid) {
    	return mapper.map(waiterDao.findByUuid(uuid));
    }

    @PostMapping
    public String createWaiter() {
        Waiter waiter = new Waiter();
        waiterDao.persist(waiter);
        return waiter.getUuid();
    }

    @PutMapping("{uuid}/name")
    public WaiterDTO updateWaiterName(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> name) {
        Waiter w = waiterDao.findByUuid(uuid);
        w.setName(name.getValue());
        return mapper.map(w);
    }

    @PutMapping("{uuid}/surname")
    public WaiterDTO updateWaiterSurname(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> surname) {
        Waiter w = waiterDao.findByUuid(uuid);
        w.setSurname(surname.getValue());
        return mapper.map(w);
    }

    @PutMapping("{uuid}/cf")
    public WaiterDTO updateWaiterCf(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> cf) {
        Waiter w = waiterDao.findByUuid(uuid);
        w.setCf(cf.getValue());
        return mapper.map(w);
    }

    @PutMapping("{uuid}/status")
    public WaiterDTO updateWaiterStatus(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> statusText) {
        Waiter w = waiterDao.findByUuid(uuid);
        WaiterStatus status = WaiterStatus.valueOf(statusText.getValue());
        if (status != WaiterStatus.REMOVED || !hasTables(w)) {
            w.setStatus(status);
            return mapper.map(w);
        } else {
            throw new RuntimeException("Il cameriere ha tavoli assegnati.");
        }
    }

    @DeleteMapping("{uuid}")
    public void deleteWaiter(@PathVariable("uuid") String uuid) {
        waiterDao.deleteByUuid(uuid);
    }

    private boolean hasTables(Waiter w) {
        return !dtDao.findByWaiter(w.getUuid()).isEmpty();
    }

}
