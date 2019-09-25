/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Settings;
import javax.inject.Inject;

/**
 *
 * @author naigo
 */
public class PrinterDao extends AbstractDao<Printer> {

    @Inject
    private SettingsDao sDao;

    public Printer findMainPrinter(){
        Settings s = sDao.find();
        if(s != null){
            return s.getMainPrinter();
        }
        return null;
    }

    @Override
    public Class<Printer> getEntityClass() {
        return Printer.class;
    }

}
