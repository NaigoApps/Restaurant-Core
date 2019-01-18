package com.naigoapps.restaurant.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.RemovedContent;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import com.naigoapps.restaurant.services.exceptions.AlreadyRegisteredCoverChargesException;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.filechooser.FileSystemView;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author naigo
 */
@Path("/dining-tables")
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
    
    @POST
    @Transactional
    public Response addDiningTable(DiningTableDTO newDiningTable) {

        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            Waiter w = wDao.findByUuid(newDiningTable.getWaiter());
            RestaurantTable rt = rtDao.findByUuid(newDiningTable.getTable());
            if (w != null && rt != null) {
                DiningTable diningTable = createDiningTable(currentEvening, w, rt, newDiningTable.getCoverCharges());
                dtDao.persist(diningTable);
                dtWS.update();
                return ResponseBuilder.created(DTOAssembler.fromDiningTable(diningTable));
            }
            return ResponseBuilder.badRequest("Dati del tavolo non validi");
        } else {
            return ResponseBuilder.badRequest(EVENING_NOT_FOUND);
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
    public Response loadDiningTables() {
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

    @PUT
    @Path("{uuid}/cover-charges")
    @Transactional
    public Response updateCoverCharges(@PathParam(value = "uuid") String tableUuid, int coverCharges) {
        try {
            return updateTableProperty(tableUuid, table -> {
                if (table.getBills().stream()
                        .collect(Collectors.summingInt(Bill::getCoverCharges)) <= coverCharges) {
                    table.setCoverCharges(coverCharges);
                } else {
                    throw new AlreadyRegisteredCoverChargesException();
                }
            });
        } catch (AlreadyRegisteredCoverChargesException ex) {
            return ResponseBuilder.badRequest("Coperti già inseriti in un conto");
        }
    }

    @PUT
    @Path("{uuid}/waiter")
    @Transactional
    public Response updateWaiter(@PathParam("uuid") String tableUuid, String waiterUuid) {
        return updateTableProperty(tableUuid, table -> table.setWaiter(wDao.findByUuid(waiterUuid)));
    }

    @PUT
    @Path("{uuid}/table")
    @Transactional
    public Response updateTable(@PathParam("uuid") String diningTableUuid, String tableUuid) {
        return updateTableProperty(tableUuid, table -> table.setTable(rtDao.findByUuid(tableUuid)));
    }

    @POST
    @Path("{uuid}/lock")
    @Transactional
    public Response lockTable(@PathParam("uuid") String tableUuid) {
        return updateTableProperty(tableUuid, table -> {
            boolean ordersOk = table.listOrders().stream().allMatch(order -> order.getBill() != null);
            if (ordersOk) {
                table.setStatus(DiningTableStatus.CLOSED);
            }
            removeBadContent(table);
        });
    }

    private void removeBadContent(DiningTable table) {
        RemovedContent removedContent = new RemovedContent();
        removedContent.setDate(table.getEvening().getDay());
        
        List<Bill> badBills = table.getBills().stream()
                .filter(bill -> bill.isGeneric())
                .collect(Collectors.toList());
        
        removedContent.addBills(badBills.stream().map(DTOAssembler::fromBill).collect(Collectors.toList()));
        
        badBills.forEach(bill -> {
            removedContent.addOrders(bill.getOrders().stream().map(DTOAssembler::fromOrder).collect(Collectors.toList()));
            bill.setTable(null);
            bill.clearOrders();
            table.setCoverCharges(table.getCoverCharges() - bill.getCoverCharges());
            dtDao.getEntityManager().remove(bill);
        });

        table.getOrdinations().forEach(ordination -> {
            List<Order> badOrders = ordination.getOrders().stream()
                    .filter(order -> order.getBill() == null)
                    .collect(Collectors.toList());
            badOrders.forEach(order -> {
                order.setOrdination(null);
                dtDao.getEntityManager().remove(order);
            });
        });

        List<Ordination> badOrdinations = table.getOrdinations().stream()
                .filter(ordination -> ordination.getOrders().isEmpty())
                .collect(Collectors.toList());

        badOrdinations.forEach(ordination -> {
            ordination.setTable(null);
            dtDao.getEntityManager().remove(ordination);
        });

        if (table.getOrdinations().isEmpty()) {
            table.setEvening(null);
            dtDao.getEntityManager().remove(table);
        }
        
        try {
            saveRemovedContent(table, removedContent);
        } catch (IOException ex) {
            Logger.getLogger(DiningTableREST.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERRORE");
        }
    }
    
    private static void saveRemovedContent(DiningTable table, RemovedContent list) throws IOException{
        String tableUuid = table.getUuid();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File home = fsv.getHomeDirectory();
        int i = 0;
        File outFile = new File(home, tableUuid + String.format("%03d", i) + ".json");
        while(outFile.exists()){
            i++;
            outFile = new File(home, tableUuid + String.format("%03d", i) + ".json");
        }
        outFile.createNewFile();
        try (OutputStream stream = new FileOutputStream(outFile)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stream, list);
        }
    }

    private Response updateTableProperty(String tableUuid, Consumer<DiningTable> updater) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dtDao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
                try {
                    updater.accept(toUpdate);
                    return ResponseBuilder.ok(DTOAssembler.fromDiningTable(toUpdate));
                } catch (Exception ex) {
                    return ResponseBuilder.badRequest(ex.getMessage());
                }
            } else {
                return ResponseBuilder.notFound("Tavolo non trovato");
            }
        } else {
            return ResponseBuilder.badRequest("Serata non selezionata");
        }
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public Response deleteDiningTable(@PathParam("uuid") String uuid) {
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

    @POST
    @Path("{sid}/merge/{did}")
    @Transactional
    public Response mergeDiningTables(@PathParam("sid") String uuid1, @PathParam("did") String uuid2) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable srcTable = dtDao.findByUuid(uuid1);
        DiningTable dstTable = dtDao.findByUuid(uuid2);
        if (currentEvening != null && currentEvening.equals(srcTable.getEvening()) && currentEvening.equals(dstTable.getEvening())) {
            if (!DiningTableStatus.CLOSED.equals(srcTable.getStatus()) && !DiningTableStatus.CLOSED.equals(dstTable.getStatus())) {
                List<Ordination> ordinationsToMove = new ArrayList<>(srcTable.getOrdinations());
                ordinationsToMove.forEach(o -> o.setTable(dstTable));

                List<Bill> billsToMove = new ArrayList<>(srcTable.getBills());
                billsToMove.forEach(b -> b.setTable(dstTable));

                dstTable.setCoverCharges(dstTable.getCoverCharges() + srcTable.getCoverCharges());
                srcTable.setEvening(null);
                dtDao.getEntityManager().remove(srcTable);
                return ResponseBuilder.ok(DTOAssembler.fromDiningTable(dstTable));
            } else {
                return ResponseBuilder.badRequest("Impossibile fondere tavoli chiusi");
            }
        }
        return ResponseBuilder.badRequest("Tavoli non corretti");

    }
}
