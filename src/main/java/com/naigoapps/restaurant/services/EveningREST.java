/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.builders.EveningBuilder;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.EveningDTO;
import com.naigoapps.restaurant.services.dto.mappers.EveningMapper;
import com.naigoapps.restaurant.services.filters.Accessible;

/**
 *
 * @author naigo
 */
@Path("/evenings")
@Produces(MediaType.APPLICATION_JSON)
public class EveningREST {

    @Inject
    private EveningManager eveningManager;

    @Inject
    private EveningDao eveningDao;

    @Inject
    private SettingsDao sDao;
    
    @Inject
    private EveningMapper mapper;

    @GET
    @Transactional
    @Accessible
    public EveningDTO selectEvening(@QueryParam("date") String date) {
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
        
        throw new BadRequestException("Data non valida");
    }

    @GET
    @Path("selected")
    @Transactional
    public EveningDTO getSelectedEvening() {
    	return mapper.map(eveningManager.getSelectedEvening());
    }

    @PUT
    @Path("{uuid}/coverCharge")
    @Transactional
    public EveningDTO updateCoverCharge(@PathParam("uuid") String uuid, float coverCharge) {
        Evening current = eveningManager.getSelectedEvening();
        Settings settings = sDao.find();
        if (current.getUuid().equals(uuid)) {
            settings.setDefaultCoverCharge(coverCharge);
            current.setCoverCharge(coverCharge);
            return mapper.map(current);
        }
        throw new BadRequestException("Serata non valida");
    }
}
