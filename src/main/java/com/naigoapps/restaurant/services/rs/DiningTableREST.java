package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.*;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.DiningTablesUpdatedEvent;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.DiningTableSkeletonDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.DiningTableMapper;
import com.naigoapps.restaurant.services.exceptions.AlreadyRegisteredCoverChargesException;
import com.naigoapps.restaurant.services.websocket.DiningTableWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author naigo
 */
@RequestMapping("/rest/dining-tables")
@RestController
@Transactional
public class DiningTableREST {

    private static final String EVENING_NOT_FOUND = "Serata non selezionata";
    private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

    @Autowired
    private EveningManager eveningManager;

    @Autowired
    private WaiterDao wDao;

    @Autowired
    private RestaurantTableDao rtDao;

    @Autowired
    private DiningTableDao dao;

    @Autowired
    private DiningTableWS dtWS;

    @Autowired
    private DiningTableMapper mapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping
    public DiningTableSkeletonDTO addDiningTable(@RequestBody DiningTableSkeletonDTO table) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable diningTable = new DiningTableBuilder().date(LocalDateTime.now()).evening(currentEvening)
                .ccs(table.getCoverCharges()).waiter(wDao.findByUuid(table.getWaiter().getUuid()))
                .table(rtDao.findByUuid(table.getTable().getUuid())).getContent();
        dao.persist(diningTable);
        notifyDiningTablesUpdate();
        return mapper.mapSkeleton(diningTable);
    }

    @EventListener(DiningTablesUpdatedEvent.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyDiningTablesUpdate(DiningTablesUpdatedEvent evt) {
        dtWS.update();
    }

    public void notifyDiningTablesUpdate() {
        publisher.publishEvent(new DiningTablesUpdatedEvent());
    }


    @PutMapping("{uuid}")
    public DiningTableSkeletonDTO editDiningTable(@PathVariable("uuid") String uuid, @RequestBody DiningTableSkeletonDTO table) {
        DiningTable diningTable = dao.findByUuid(uuid);
        diningTable.setCoverCharges(table.getCoverCharges());
        diningTable.setWaiter(wDao.findByUuid(table.getWaiter().getUuid()));
        diningTable.setTable(rtDao.findByUuid(table.getTable().getUuid()));
        notifyDiningTablesUpdate();
        return mapper.mapSkeleton(diningTable);
    }

    @GetMapping
    public List<DiningTableDTO> list() {
        Evening currentEvening = eveningManager.getSelectedEvening();
        return currentEvening.getDiningTables().stream().filter(table -> DiningTableStatus.CLOSED != table.getStatus())
                .map(mapper::map).sorted(Collections.reverseOrder(DiningTableDTO.comparator()))
                .collect(Collectors.toList());
    }

    @GetMapping("open")
    public List<DiningTableDTO> open() {
        Evening currentEvening = eveningManager.getSelectedEvening();
        return currentEvening.getDiningTables().stream().filter(table -> DiningTableStatus.OPEN == table.getStatus())
                .map(mapper::map).sorted(Collections.reverseOrder(DiningTableDTO.comparator()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{uuid}")
    public DiningTableDTO load(@PathVariable("uuid") String uuid) {
        return mapper.map(dao.findByUuid(uuid));
    }

    @GetMapping("/{uuid}/skeleton")
    public DiningTableSkeletonDTO loadSkeleton(@PathVariable("uuid") String uuid) {
        return mapper.mapSkeleton(dao.findByUuid(uuid));
    }

    @PutMapping("{uuid}/coverCharges")
    public DiningTableDTO updateCoverCharges(@PathVariable(value = "uuid") String tableUuid,
                                             WrapperDTO<Integer> coverCharges) {
        return updateTableProperty(tableUuid, table -> {
            if (table.getBills().stream().collect(Collectors.summingInt(Bill::getCoverCharges)) <= coverCharges
                    .getValue()) {
                table.setCoverCharges(coverCharges.getValue());
            } else {
                throw new AlreadyRegisteredCoverChargesException("Coperti già inseriti in un conto");
            }
        });
    }

    @PutMapping("{uuid}/waiter")
    public DiningTableDTO updateWaiter(@PathVariable("uuid") String tableUuid, @RequestBody WrapperDTO<String> waiterUuid) {
        return updateTableProperty(tableUuid, table -> table.setWaiter(wDao.findByUuid(waiterUuid.getValue())));
    }

    @PutMapping("{uuid}/table")
    public DiningTableDTO updateTable(@PathVariable("uuid") String diningTableUuid, @RequestBody WrapperDTO<String> tableUuid) {
        return updateTableProperty(diningTableUuid, table -> table.setTable(rtDao.findByUuid(tableUuid.getValue())));
    }

    @PostMapping("{uuid}/lock")
    public void lockTable(@PathVariable("uuid") String tableUuid) {
        DiningTable table = dao.findByUuid(tableUuid);
        table.setStatus(DiningTableStatus.CLOSED);
        notifyDiningTablesUpdate();
    }

    private DiningTableDTO updateTableProperty(String tableUuid, Consumer<DiningTable> updater) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        if (currentEvening != null) {
            DiningTable toUpdate = dao.findByUuid(tableUuid);
            if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
                updater.accept(toUpdate);
                notifyDiningTablesUpdate();
                return mapper.map(toUpdate);
            } else {
                throw new RuntimeException(TABLE_NOT_FOUND);
            }
        } else {
            throw new RuntimeException(EVENING_NOT_FOUND);
        }
    }

    @GetMapping("{uuid}/mergeTargets")
    public List<DiningTableDTO> mergeTargets(@PathVariable("uuid") String sourceUuid) {
        Evening evening = eveningManager.getSelectedEvening();
        return evening.getDiningTables().stream().filter(dt -> !dt.getUuid().equals(sourceUuid))
                .filter(dt -> dt.getStatus() != DiningTableStatus.CLOSED).map(mapper::map).collect(Collectors.toList());
    }

    @PostMapping("{sid}/merge/{did}")
    public DiningTableDTO mergeDiningTables(@PathVariable("sid") String uuid1, @PathVariable("did") String uuid2) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable srcTable = dao.findByUuid(uuid1);
        DiningTable dstTable = dao.findByUuid(uuid2);
        if (currentEvening != null && currentEvening.equals(srcTable.getEvening())
                && currentEvening.equals(dstTable.getEvening())) {
            if (!DiningTableStatus.CLOSED.equals(srcTable.getStatus())
                    && !DiningTableStatus.CLOSED.equals(dstTable.getStatus())) {
                List<Ordination> ordinationsToMove = new ArrayList<>(srcTable.getOrdinations());
                ordinationsToMove.forEach(o -> o.setTable(dstTable));

                List<Bill> billsToMove = new ArrayList<>(srcTable.getBills());
                billsToMove.forEach(b -> b.setTable(dstTable));

                dstTable.setCoverCharges(dstTable.getCoverCharges() + srcTable.getCoverCharges());
                srcTable.setEvening(null);
                dao.getEntityManager().remove(srcTable);
                notifyDiningTablesUpdate();
                return mapper.map(dstTable);
            } else {
                throw new RuntimeException("Impossibile fondere tavoli chiusi");
            }
        }
        throw new RuntimeException("Tavoli non corretti");
    }

    @DeleteMapping("{uuid}")
    public void deleteDiningTable(@PathVariable("uuid") String uuid) {
        Evening currentEvening = eveningManager.getSelectedEvening();
        DiningTable toRemove = dao.findByUuid(uuid);
        if (currentEvening != null && currentEvening.equals(toRemove.getEvening())) {
            if (!toRemove.getBills().isEmpty()) {
                throw new RuntimeException("Al tavolo cono collegati degli scontrini");
            }
            if (!toRemove.getOrdinations().isEmpty()) {
                throw new RuntimeException("Il tavolo contiene delle comande");
            }
            dao.deleteByUuid(uuid);
            notifyDiningTablesUpdate();
        } else {
            throw new RuntimeException(EVENING_NOT_FOUND);
        }
    }
}
