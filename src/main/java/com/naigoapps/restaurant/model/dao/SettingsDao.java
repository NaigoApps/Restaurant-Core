/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.Settings;

/**
 *
 * @author naigo
 */
@Repository
public interface SettingsDao extends JpaRepository<Settings, Long> {

//    public SettingsDao(EntityManager em) {
//		super(Settings.class, em);
//	}
//
	default Settings find() {
		return findAll().get(0);
    }
//
//    @Override
//    public Class<Settings> getEntityClass() {
//        return Settings.class;
//    }

}
