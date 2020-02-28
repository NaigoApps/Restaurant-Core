/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.builders.AdditionBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.services.dto.AdditionDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.AdditionMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author naigo
 */
@Transactional
@RestController
@RequestMapping("/rest/additions")
public class AdditionREST {

    @Autowired
    private AdditionDao dao;

    @Autowired
    private DishDao dDao;

    @Autowired
    private AdditionMapper mapper;

    @GetMapping
    public List<AdditionDTO> find(@RequestParam(value = "dish", required = false) String dishUuid,
        @RequestParam(value = "generic", required = false) Boolean generic) {
        Set<Addition> result = new HashSet<>();
        if (dishUuid != null) {
            Dish dish = dDao.findByUuid(dishUuid);
            result.addAll(dish.getCategory().getAdditions());
            result.addAll(dao.findGeneric());
        } else if (generic != null) {
            result.addAll(dao.findWhere("generic=" + generic));
        } else {
            result.addAll(dao.findAll());
        }
        return result.stream()
            .sorted((a1, a2) -> StringUtils.compareIgnoreCase(a1.getName(), a2.getName()))
            .map(mapper::map)
            .collect(Collectors.toList());
    }

    @GetMapping("{uuid}")
    public AdditionDTO find(@PathVariable("uuid") String uuid) {
        return mapper.map(dao.findByUuid(uuid));
    }

    @PostMapping
    public String createAddition() {
        Addition addition = new AdditionBuilder().name("").getContent();
        dao.persist(addition);
        return addition.getUuid();
    }

    @PutMapping("{uuid}/name")
    public AdditionDTO updateAdditionName(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> name) {
        Addition a = dao.findByUuid(uuid);
        a.setName(name.getValue());
        return mapper.map(a);
    }

    @PutMapping("{uuid}/price")
    public AdditionDTO updateWaiterSurname(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<Float> price) {
        Addition a = dao.findByUuid(uuid);
        a.setPrice(price.getValue());
        return mapper.map(a);
    }

    @PutMapping("{uuid}/generic")
    public AdditionDTO updateAdditionGeneric(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<Boolean> generic) {
        Addition a = dao.findByUuid(uuid);
        a.setGeneric(generic.getValue());
        return mapper.map(a);
    }

    @DeleteMapping("{uuid}")
    public void deleteAddition(@PathVariable("uuid") String uuid) {
        dao.deleteByUuid(uuid);
    }
}
