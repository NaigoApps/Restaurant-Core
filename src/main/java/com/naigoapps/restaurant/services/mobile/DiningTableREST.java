package com.naigoapps.restaurant.services.mobile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.DiningTableWS;
import com.naigoapps.restaurant.services.dto.mobile.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.mobile.mappers.DiningTableMapper;
import com.naigoapps.restaurant.services.exceptions.AlreadyRegisteredCoverChargesException;

/**
 *
 * @author naigo
 */
@Path("/mobile/dining-tables")
@Produces(MediaType.APPLICATION_JSON)
public class DiningTableREST {

    private static final String EVENING_NOT_FOUND = "Serata non selezionata";
    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

    @Inject
    private EveningManager eveningManager;

    @Inject
    private WaiterDao wDao;

    @Inject
    private RestaurantTableDao rtDao;

    @Inject
    private DiningTableDao dtDao;

    @Inject
    private DiningTableWS dtWS;
    
    @Inject
    private DiningTableMapper dtMapper;
    
    @POST
    @Transactional
    public DiningTableDTO addDiningTable(DiningTableDTO newDiningTable) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            Waiter w = wDao.findByUuid(newDiningTable.getWaiter().getUuid());
            RestaurantTable rt = rtDao.findByUuid(newDiningTable.getTable().getUuid());
            if (w != null && rt != null) {
                DiningTable diningTable = createDiningTable(currentEvening, w, rt, newDiningTable.getCoverCharges());
                dtDao.persist(diningTable);
                dtWS.update();
                return dtMapper.map(diningTable);
            }else {
            	throw new IllegalArgumentException("Dati del tavolo non validi");
            }
        } else {
        	throw new IllegalStateException(EVENING_NOT_FOUND);
        }
    }
    
    private DiningTable createDiningTable(Evening e, Waiter w, RestaurantTable rt, int ccs) {
        return new DiningTableBuilder()
                .date(LocalDateTime.now())
                .evening(e)
                .waiter(w)
                .table(rt)
                .ccs(ccs)
                .getContent();
    }

    @GET
    @Transactional
    public Response list() {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            List<DiningTableDTO> result = currentEvening.getDiningTables()
                    .stream()
                    .filter(table -> DiningTableStatus.CLOSED != table.getStatus())
                    .map(dtMapper::map)
                    .sorted(DiningTableDTO.comparator())
                    .collect(Collectors.toList());
            return Response.ok().entity(result).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{uuid}")
    @Transactional
    public DiningTableDTO load(@PathParam("uuid") String uuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        
        if (currentEvening == null) {
        	throw new BadRequestException(EVENING_NOT_FOUND);
        }
        
        return dtMapper.map(dtDao.findByUuid(uuid));
    }

    @PUT
    @Path("{uuid}")
    @Transactional
    public DiningTableDTO updateDiningTable(@PathParam(value = "uuid") String tableUuid, DiningTableDTO dto) {
    	Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
            	if (toUpdate.getBills().stream()
                        .collect(Collectors.summingInt(Bill::getCoverCharges)) <= dto.getCoverCharges()) {
                    toUpdate.setCoverCharges(dto.getCoverCharges());
                } else {
                    throw new AlreadyRegisteredCoverChargesException("I coperti sono già stati assegnati ad un conto");
                }
                toUpdate.setWaiter(wDao.findByUuid(dto.getWaiter().getUuid()));
                toUpdate.setTable(rtDao.findByUuid(dto.getTable().getUuid()));
            	return dtMapper.map(toUpdate);
            } else {
                throw new IllegalArgumentException(TABLE_NOT_FOUND);
            }
        } else {
            throw new IllegalStateException(EVENING_NOT_FOUND);
        }
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public boolean deleteDiningTable(@PathParam("uuid") String uuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable toRemove = dtDao.findByUuid(uuid);
        if (currentEvening != null && currentEvening.equals(toRemove.getEvening())) {
            if (!toRemove.getBills().isEmpty()) {
            	throw new BadRequestException("Al tavolo cono collegati degli scontrini");
            }
            if (!toRemove.getOrdinations().isEmpty()) {
            	throw new BadRequestException("Il tavolo contiene delle comande");
            }
            dtDao.deleteByUuid(uuid);
            return true;
        }
        throw new BadRequestException(EVENING_NOT_FOUND);
    }
}
