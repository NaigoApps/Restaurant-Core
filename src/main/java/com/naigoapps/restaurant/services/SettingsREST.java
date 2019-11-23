/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.SettingsDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.SettingsMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
@Accessible
@Transactional
public class SettingsREST {

    @Inject
    private SettingsDao dao;

    @Inject
    private PrinterDao pDao;
    
    @Inject
    private SettingsMapper mapper;
    
    @GET
    public SettingsDTO getSettings() {
        Settings settings = dao.find();
        return mapper.map(settings);
    }
    
    @PUT
    @Path("client")
    public void setSettings(String value) {
        dao.find().setClientSettings(value);
    }

    @PUT
    @Path("fiscal-printer-address")
    public void setFiscalPrinterAddress(String value) {
    	dao.find().setFiscalPrinterAddress(value);
    }

    @PUT
    @Path("fiscal-printer-port")
    public void setFiscalPrinterAddress(int value) {
    	dao.find().setFiscalPrinterPort(value);
    }
    
    @PUT
    @Path("mainPrinter")
    public void setMainPrinter(PrinterDTO dto) {
    	dao.find().setMainPrinter(pDao.findByUuid(dto.getUuid()));
    }
    
    @PUT
    @Path("shrink-ordination")
    public void setShrinkOrdinations(WrapperDTO<Boolean> value){
        dao.find().setShrinkOrdinations(value.getValue());
    }
    
    @PUT
    @Path("cashPassword")
    public void setCashPassword(WrapperDTO<String> value){
        dao.find().setCashPassword(value.getValue());
    }
    
}
