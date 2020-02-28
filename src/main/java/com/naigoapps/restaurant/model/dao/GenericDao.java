/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.BaseEntity;

/**
 *
 * @author naigo
 */
@Repository
@Qualifier("generic")
public class GenericDao extends AbstractDao<BaseEntity> {

    @Override
    public List<BaseEntity> findAll() {
        throw new UnsupportedOperationException("Cannot find generic entities");
    }
    
    @Override
    public void deleteByUuid(String uuid) {
        throw new UnsupportedOperationException("Cannot delete generic entity by uuid");
    }

    @Override
    public BaseEntity findByUuid(String uuid) {
        throw new UnsupportedOperationException("Cannot find generic entity by uuid");
    }

    @Override
    protected String getOrderByClause() {
        return null;
    }

    @Override
    public Class<BaseEntity> getEntityClass() {
        return BaseEntity.class;
    }
    

}
