/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
@Path("/dining-tables")
@Produces(MediaType.APPLICATION_JSON)
public class DiningTableREST {

    @Inject
    EveningManager eveningManager;

    @Inject
    WaiterDao wDao;

    @Inject
    RestaurantTableDao rtDao;

    @Inject
    DiningTableDao dtDao;

    @GET
    public Response getByEvening() {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            List<DiningTableDTO> result = currentEvening.getDiningTables()
                    .stream()
                    .map(DTOAssembler::fromDiningTable)
                    .collect(Collectors.toList());
            return Response.ok().entity(result).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
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
                    .entity("Serata non selezionata")
                    .build();
        } else {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

    }

    private DiningTable findTableInEvening(Evening e, String uuid) {
        return e.getDiningTables().stream()
                .filter(table -> table.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @PUT
    @Path("{uuid}/cover-charges")
    @Transactional
    public Response updateCoverCharges(@PathParam(value = "uuid") String tableUuid, int coverCharges) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                toUpdate.setCoverCharges(coverCharges);
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{uuid}/waiter")
    @Transactional
    public Response updateWaiter(@PathParam("uuid") String tableUuid, String waiterUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, tableUuid);
            if (toUpdate != null) {
                toUpdate.setWaiter(wDao.findByUuid(waiterUuid));
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{uuid}/table")
    @Transactional
    public Response updateTable(@PathParam("uuid") String diningTableUuid, String tableUuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = findTableInEvening(currentEvening, diningTableUuid);
            if (toUpdate != null) {
                toUpdate.setTable(rtDao.findByUuid(tableUuid));
                return Response.ok(DTOAssembler.fromDiningTable(toUpdate)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @DELETE
    @Transactional
    public Response deleteDiningTable(String uuid){
        
        dtDao.removeByUuid(uuid);
        
        return Response
                .status(Response.Status.OK)
                .build();
    }
}
