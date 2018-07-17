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
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.builders.EveningBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
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
            return Response
                    .status(Response.Status.OK)
                    .entity(DTOAssembler.fromEvening(e))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
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

    @POST
    @Path("tables")
    @Transactional
    public Response addDiningTable(DiningTableDTO newDiningTable) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            Waiter w = wDao.findByUuid(newDiningTable.getWaiter());
            RestaurantTable rt = rtDao.findByUuid(newDiningTable.getTable());
            if (w != null && rt != null) {
                DiningTable diningTable = new DiningTableBuilder()
                        .date(LocalDateTime.now())
                        .evening(currentEvening)
                        .waiter(w)
                        .table(rt)
                        .ccs(newDiningTable.getCoverCharges())
                        .getContent();

                dtDao.persist(diningTable);

                return Response
                        .status(Response.Status.CREATED)
                        .entity(DTOAssembler.fromDiningTable(diningTable))
                        .build();
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Dati del tavolo non validi")
                    .build();
        } else {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Serata non selezionata")
                    .build();
        }

    }

    @POST
    @Path("tables/merge/{uuid}")
    @Transactional
    public Response mergeDiningTables(@PathParam("uuid") String uuid1, String uuid2) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable srcTable = dtDao.findByUuid(uuid1);
        DiningTable dstTable = dtDao.findByUuid(uuid2);
        if (currentEvening != null && currentEvening.equals(srcTable.getEvening()) && currentEvening.equals(dstTable.getEvening())) {
            if (!DiningTableStatus.CLOSED.equals(srcTable.getStatus()) && !DiningTableStatus.CLOSED.equals(dstTable.getStatus())) {
                srcTable.getOrdinations().forEach(o -> o.setTable(dstTable));
                srcTable.getBills().forEach(b -> b.setTable(dstTable));
                dstTable.setCoverCharges(dstTable.getCoverCharges() + srcTable.getCoverCharges());
                srcTable.setEvening(null);
                dtDao.getEntityManager().remove(srcTable);
                return ResponseBuilder.ok(DTOAssembler.fromEvening(currentEvening));
            } else {
                return ResponseBuilder.badRequest("Impossibile fondere tavoli chiusi");
            }
        }
        return ResponseBuilder.badRequest("Tavoli non corretti");

    }

    @DELETE
    @Path("diningTables")
    @Transactional
    public Response deleteDiningTable(String uuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable toRemove = dtDao.findByUuid(uuid);
        if (currentEvening != null && currentEvening.equals(toRemove.getEvening())) {
            if (toRemove.getBills().size() > 0) {
                return ResponseBuilder.badRequest("Al tavolo sono collegati degli scontrini");
            }
            if (toRemove.getOrdinations().size() > 0) {
                return ResponseBuilder.badRequest("Il tavolo contiene delle comande");
            }
            dtDao.deleteByUuid(uuid);
            return ResponseBuilder.ok(DTOAssembler.fromEvening(currentEvening));
        }
        return ResponseBuilder.badRequest("Serata non correttamente selezionata");

    }
}
