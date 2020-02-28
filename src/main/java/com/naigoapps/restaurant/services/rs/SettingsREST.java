/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.SettingsDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.SettingsMapper;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/settings")
@RestController
@Transactional
public class SettingsREST {

    @Autowired
    private SettingsDao dao;

    @Autowired
    private PrinterDao pDao;
    
    @Autowired
    private SettingsMapper mapper;
    
    @GetMapping
    public SettingsDTO getSettings() {
        Settings settings = dao.find();
        return mapper.map(settings);
    }
    
    @PutMapping("client")
    public void setSettings(@RequestBody String value) {
        dao.find().setClientSettings(value);
    }

    @PutMapping("fiscal-printer-address")
    public void setFiscalPrinterAddress(@RequestBody String value) {
    	dao.find().setFiscalPrinterAddress(value);
    }

    @PutMapping("fiscal-printer-path")
    public void setFiscalPrinterPath(@RequestBody String value) {
    	dao.find().setFiscalPrinterPath(value);
    }

    @PutMapping("fiscal-printer-port")
    public void setFiscalPrinterAddress(@RequestBody int value) {
    	dao.find().setFiscalPrinterPort(value);
    }
    
    @PutMapping("mainPrinter")
    public void setMainPrinter(@RequestBody PrinterDTO dto) {
    	dao.find().setMainPrinter(pDao.findByUuid(dto.getUuid()));
    }
    
    @PutMapping("shrink-ordination")
    public void setShrinkOrdinations(@RequestBody WrapperDTO<Boolean> value){
        dao.find().setShrinkOrdinations(value.getValue());
    }
    
    @PutMapping("cashPassword")
    public void setCashPassword(@RequestBody WrapperDTO<String> value){
        dao.find().setCashPassword(value.getValue());
    }
    
}
