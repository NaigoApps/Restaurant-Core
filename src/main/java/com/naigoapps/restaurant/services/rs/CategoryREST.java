/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.CategoryDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.CategoryMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
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

/**
 * @author naigo
 */
@RequestMapping("/rest/categories")
@RestController
@Transactional
public class CategoryREST {

    @Autowired
    private CategoryDao dao;

    @Autowired
    private DishDao dDao;

    @Autowired
    private LocationDao lDao;

    @Autowired
    private AdditionDao aDao;

    @Autowired
    private CategoryMapper mapper;

    @GetMapping
    public List<CategoryDTO> findAll() {
        return dao.findAll()
            .stream()
            .map(mapper::map)
            .collect(Collectors.toList());
    }

    @GetMapping("{uuid}")
    public CategoryDTO find(@PathVariable("uuid") String uuid) {
        return mapper.map(dao.findByUuid(uuid));
    }

    @PostMapping
    public String createCategory() {
        Category category = new Category();
        dao.persist(category);
        return category.getUuid();
    }

    @PutMapping("{uuid}/name")
    public CategoryDTO updateCategoryName(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> name) {
        return setCategoryProperty(cat -> cat.setName(name.getValue()), uuid);
    }

    @PutMapping("{uuid}/location")
    public CategoryDTO updateCategoryLocation(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> locUuid) {
        Location loc = lDao.findByUuid(locUuid.getValue());
        if (loc != null) {
            return setCategoryProperty(cat -> cat.setLocation(loc), uuid);
        } else {
            throw new RuntimeException("Postazione non trovata");
        }
    }

    @PutMapping("{uuid}/additions")
    public CategoryDTO updateCategoryAdditions(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String[]> additionUuids) {
        String[] uuids = additionUuids.getValue();
        uuids = uuids == null ? new String[0] : uuids;
        List<Addition> additions = Arrays.stream(uuids)
            .map(a -> aDao.findByUuid(a))
            .collect(Collectors.toList());
        if (additions.stream().allMatch(Objects::nonNull)) {
            return setCategoryProperty(cat -> cat.setAdditions(additions), uuid);
        } else {
            throw new RuntimeException("Impossibile trovare alcune varianti");
        }
    }

    @PutMapping("{uuid}/color")
    public CategoryDTO updateCategoryColor(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> color) {
        return setCategoryProperty(cat -> cat.setColor(color.getValue()), uuid);
    }

    private CategoryDTO setCategoryProperty(Consumer<Category> setter, String uuid) {
        Category cat = dao.findByUuid(uuid);
        if (cat != null) {
            setter.accept(cat);
            return mapper.map(cat);
        } else {
            throw new RuntimeException("Categoria non trovata");
        }
    }

    @DeleteMapping("{uuid}")
    public void deleteCategory(@PathVariable("uuid") String uuid) {
        dao.deleteByUuid(uuid);
    }

}
