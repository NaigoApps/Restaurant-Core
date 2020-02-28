/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.services.dto.WrapperDTO;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.DishStatus;
import com.naigoapps.restaurant.model.builders.DishBuilder;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.services.dto.DishDTO;
import com.naigoapps.restaurant.services.dto.mappers.DishMapper;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/dishes")
@RestController
@Transactional
public class DishREST {

    @Autowired
    private DishDao dao;

    @Autowired
    private OrderDao oDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private DishMapper mapper;

    @GetMapping("{uuid}")
    public DishDTO findByUuid(@PathVariable("uuid") String uuid) {
    	return mapper.map(dao.findByUuid(uuid));
    }

    @GetMapping
    public List<DishDTO> findByCategory(@RequestParam("category") String catUuid) {
        return dao.findByCategory(catUuid).stream()
                .filter(d -> DishStatus.SUSPENDED != d.getStatus())
                .filter(d -> DishStatus.REMOVED != d.getStatus())
                .sorted(Comparator.comparing(Dish::getName))
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("all")
    public List<DishDTO> findAll() {
        return dao.findAll().stream()
                .filter(d -> DishStatus.REMOVED != d.getStatus())
                .sorted((d1, d2) -> d1.getName().compareTo(d2.getName()))
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PutMapping("{uuid}/name")
    public DishDTO updateName(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> name) {
    	return updateDishProperty(uuid, d -> d.setName(name.getValue()));
    }

    @PutMapping("{uuid}/description")
    public DishDTO updateDescription(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> description) throws Exception {
    	return updateDishProperty(uuid, d -> d.setDescription(description.getValue()));
    }

    @PutMapping("{uuid}/price")
    public DishDTO updatePrice(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<Float> price) {
    	return updateDishProperty(uuid, d -> d.setPrice(price.getValue()));
    }

    @PutMapping("{uuid}/status")
    public DishDTO updateStatus(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<DishStatus> statusText) {
    	DishStatus status = statusText.getValue();
    	return updateDishProperty(uuid, d -> {
    		if (!DishStatus.REMOVED.equals(status) || !hasOrders(d)) {
    			d.setStatus(status);
    		}else {
    			throw new RuntimeException("Il piatto è usato in alcuni ordini");
    		}
    	});
    }

    private DishDTO updateDishProperty(String dishUuid, Consumer<Dish> consumer) {
    	Dish dish = dao.findByUuid(dishUuid);
    	if(dish == null) {
    		throw new RuntimeException("Piatto non trovato");
    	}
    	consumer.accept(dish);
    	return mapper.map(dish);
    }

    private boolean hasOrders(Dish d) {
        return oDao.countByDish(d) > 0;
    }

    @PutMapping("{uuid}/category")
    public DishDTO updateCategory(@PathVariable("uuid") String dishUuid, @RequestBody WrapperDTO<String> catUuid) {
    	Category cat = categoryDao.findByUuid(catUuid.getValue());
        return updateDishProperty(dishUuid, d -> d.setCategory(cat));
    }

    @PostMapping
    public String createDish(@RequestParam("category") String category) {
        Category cat = categoryDao.findByUuid(category);

        if (cat != null) {
            Dish dish = new DishBuilder()
                    .category(cat)
                    .getContent();
            dao.persist(dish);

            return dish.getUuid();
        }else {
        	throw new RuntimeException("Categoria non valida");
        }
    }

    @DeleteMapping("{uuid}")
    public void deleteDish(@PathVariable("uuid") String uuid) {
        dao.deleteByUuid(uuid);
    }
}
