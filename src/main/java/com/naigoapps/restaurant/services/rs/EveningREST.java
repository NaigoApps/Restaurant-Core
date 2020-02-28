/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.builders.EveningBuilder;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.EveningDTO;
import com.naigoapps.restaurant.services.dto.mappers.EveningMapper;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/evenings")
@RestController
@Transactional
public class EveningREST {

    @Autowired
    private EveningManager eveningManager;

    @Autowired
    private EveningDao eveningDao;

    @Autowired
    private SettingsDao sDao;
    
    @Autowired
    private EveningMapper mapper;

    @GetMapping
    public EveningDTO selectEvening(@RequestParam("date") String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate d = LocalDate.parse(date, formatter);

        if (d != null) {
            Evening chosen = eveningDao.findByDate(d);
            if (chosen == null) {
                Settings settings = sDao.find();
                chosen = new EveningBuilder()
                        .day(d)
                        .coverCharge(settings.getDefaultCoverCharge())
                        .getContent();
                eveningDao.persist(chosen);
            }
            eveningManager.setSelectedEvening(chosen.getUuid());

            return mapper.map(chosen);
        }
        
        throw new RuntimeException("Data non valida");
    }

    @GetMapping("selected")
    public EveningDTO getSelectedEvening() {
    	return mapper.map(eveningManager.getSelectedEvening());
    }

    @PutMapping("{uuid}/coverCharge")
    public EveningDTO updateCoverCharge(@PathVariable("uuid") String uuid, @RequestBody float coverCharge) {
        Evening current = eveningManager.getSelectedEvening();
        Settings settings = sDao.find();
        if (current.getUuid().equals(uuid)) {
            settings.setDefaultCoverCharge(coverCharge);
            current.setCoverCharge(coverCharge);
            return mapper.map(current);
        }
        throw new RuntimeException("Serata non valida");
    }
}
