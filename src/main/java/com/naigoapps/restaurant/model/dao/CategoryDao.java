/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.Category;

/**
 *
 * @author naigo
 */
@Repository
public class CategoryDao extends AbstractDao<Category> {

    @Override
    protected String getOrderByClause() {
        return "name";
    }

    @Override
    public Class<Category> getEntityClass() {
        return Category.class;
    }

}
