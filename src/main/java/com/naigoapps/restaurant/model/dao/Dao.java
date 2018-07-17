/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.BaseEntity;
import java.util.List;

/**
 *
 * @author naigo
 * @param <E>
 */
public interface Dao<E extends BaseEntity> {

    public E findByUuid(String uuid);

    public List<E> findAll();

    public void deleteByUuid(String uuid);

    public void delete(BaseEntity entity);
    
    public Class<E> getEntityClass();

}
