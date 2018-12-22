/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.builders.EveningBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    private WaiterDao wDao;

    @Inject
    private RestaurantTableDao rtDao;

    @Inject
    private DiningTableDao dtDao;

    @Inject
    private SettingsDao sDao;

    @GET
    @Transactional
    public Response selectEvening(@QueryParam("date") String date) {
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
            return Response
                    .status(Response.Status.OK)
                    .entity(DTOAssembler.fromEvening(chosen))
                    .build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("selected")
    @Transactional
    public Response getSelectedEvening() {
        Evening e = eveningManager.getSelectedEvening();
        if (e != null) {
            return ResponseBuilder.ok(DTOAssembler.fromEvening(e));
        } else {
            return ResponseBuilder.notFound("Nessuna serata selezionata");
        }
    }

    @PUT
    @Path("{uuid}/coverCharge")
    @Transactional
    public Response updateCoverCharge(@PathParam("uuid") String uuid, float coverCharge) {
        Evening e = eveningManager.getSelectedEvening();
        Settings settings = sDao.find();
        if (e.getUuid().equals(uuid)) {
            settings.setDefaultCoverCharge(coverCharge);
            e.setCoverCharge(coverCharge);
            return Response
                    .status(200)
                    .entity(DTOAssembler.fromEvening(e))
                    .build();
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(DTOAssembler.fromEvening(e))
                    .build();
        }
    }
}
