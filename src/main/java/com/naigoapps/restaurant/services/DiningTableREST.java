package com.naigoapps.restaurant.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
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

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.builders.DiningTableBuilder;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.RestaurantTableDao;
import com.naigoapps.restaurant.model.dao.WaiterDao;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.DiningTableExportDTO;
import com.naigoapps.restaurant.services.dto.DiningTableSkeletonDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.DiningTableMapper;
import com.naigoapps.restaurant.services.exceptions.AlreadyRegisteredCoverChargesException;

/**
 *
 * @author naigo
 */
@Path("/dining-tables")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
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
	private DiningTableDao dao;

	@Inject
	private DiningTableWS dtWS;

	@Inject
	private DiningTableMapper mapper;

	@Inject
	private Event<DiningTablesUpdatedEvent> evt;
	
	@POST
	public DiningTableSkeletonDTO addDiningTable(DiningTableSkeletonDTO table) {
		Evening currentEvening = eveningManager.getSelectedEvening();
		DiningTable diningTable = new DiningTableBuilder().date(LocalDateTime.now()).evening(currentEvening)
				.ccs(table.getCoverCharges()).waiter(wDao.findByUuid(table.getWaiter().getUuid()))
				.table(rtDao.findByUuid(table.getTable().getUuid())).getContent();
		dao.persist(diningTable);
		notifyDiningTablesUpdate();
		return mapper.mapSkeleton(diningTable);
	}
	
	public void notifyDiningTablesUpdate(@Observes(during = TransactionPhase.AFTER_COMPLETION) DiningTablesUpdatedEvent evt) {		
		dtWS.update();
	}
	
	public void notifyDiningTablesUpdate() {
		evt.fire(new DiningTablesUpdatedEvent());
	}
	

	@PUT
	@Path("{uuid}")
	public DiningTableSkeletonDTO editDiningTable(@PathParam("uuid") String uuid, DiningTableSkeletonDTO table) {
		DiningTable diningTable = dao.findByUuid(uuid);
		diningTable.setCoverCharges(table.getCoverCharges());
		diningTable.setWaiter(wDao.findByUuid(table.getWaiter().getUuid()));
		diningTable.setTable(rtDao.findByUuid(table.getTable().getUuid()));
		notifyDiningTablesUpdate();
		return mapper.mapSkeleton(diningTable);
	}

	@GET
	public List<DiningTableDTO> list() {
		Evening currentEvening = eveningManager.getSelectedEvening();
		return currentEvening.getDiningTables().stream().filter(table -> DiningTableStatus.CLOSED != table.getStatus())
				.map(mapper::map).sorted(Collections.reverseOrder(DiningTableDTO.comparator()))
				.collect(Collectors.toList());
	}

	@GET
	@Path("open")
	public List<DiningTableDTO> open() {
		Evening currentEvening = eveningManager.getSelectedEvening();
		return currentEvening.getDiningTables().stream().filter(table -> DiningTableStatus.OPEN == table.getStatus())
				.map(mapper::map).sorted(Collections.reverseOrder(DiningTableDTO.comparator()))
				.collect(Collectors.toList());
	}

	@GET
	@Path("/{uuid}")
	public DiningTableDTO load(@PathParam("uuid") String uuid) {
		return mapper.map(dao.findByUuid(uuid));
	}

	@GET
	@Path("/{uuid}/skeleton")
	public DiningTableSkeletonDTO loadSkeleton(@PathParam("uuid") String uuid) {
		return mapper.mapSkeleton(dao.findByUuid(uuid));
	}

	@PUT
	@Path("{uuid}/coverCharges")
	public DiningTableDTO updateCoverCharges(@PathParam(value = "uuid") String tableUuid,
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

	@PUT
	@Path("{uuid}/waiter")
	public DiningTableDTO updateWaiter(@PathParam("uuid") String tableUuid, WrapperDTO<String> waiterUuid) {
		return updateTableProperty(tableUuid, table -> table.setWaiter(wDao.findByUuid(waiterUuid.getValue())));
	}

	@PUT
	@Path("{uuid}/table")
	public DiningTableDTO updateTable(@PathParam("uuid") String diningTableUuid, WrapperDTO<String> tableUuid) {
		return updateTableProperty(diningTableUuid, table -> table.setTable(rtDao.findByUuid(tableUuid.getValue())));
	}

	@POST
	@Path("{uuid}/lock")
	public void lockTable(@PathParam("uuid") String tableUuid) {
		DiningTable table = dao.findByUuid(tableUuid);
		boolean ordersOk = table.getOrders().stream().allMatch(order -> order.getBill() != null);
		if (ordersOk) {
			table.setStatus(DiningTableStatus.CLOSED);
			notifyDiningTablesUpdate();
		} else {
			throw new BadRequestException("Il tavolo non può ancora essere chiuso");
		}
	}

	@GET
	@Path("{uuid}/export")
	@Produces("application/json")
	public DiningTableExportDTO exportTable(@PathParam("uuid") String tableUuid) {
		return mapper.mapForExport(dao.findByUuid(tableUuid));
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
				throw new BadRequestException(TABLE_NOT_FOUND);
			}
		} else {
			throw new BadRequestException(EVENING_NOT_FOUND);
		}
	}

	@GET
	@Path("{uuid}/mergeTargets")
	public List<DiningTableDTO> mergeTargets(@PathParam("uuid") String sourceUuid) {
		Evening evening = eveningManager.getSelectedEvening();
		return evening.getDiningTables().stream().filter(dt -> !dt.getUuid().equals(sourceUuid))
				.filter(dt -> dt.getStatus() != DiningTableStatus.CLOSED).map(mapper::map).collect(Collectors.toList());
	}

	@POST
	@Path("{sid}/merge/{did}")
	public DiningTableDTO mergeDiningTables(@PathParam("sid") String uuid1, @PathParam("did") String uuid2) {
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
				throw new BadRequestException("Impossibile fondere tavoli chiusi");
			}
		}
		throw new BadRequestException("Tavoli non corretti");
	}

	@DELETE
	@Path("{uuid}")
	public void deleteDiningTable(@PathParam("uuid") String uuid) {
		Evening currentEvening = eveningManager.getSelectedEvening();
		DiningTable toRemove = dao.findByUuid(uuid);
		if (currentEvening != null && currentEvening.equals(toRemove.getEvening())) {
			if (!toRemove.getBills().isEmpty()) {
				throw new BadRequestException("Al tavolo cono collegati degli scontrini");
			}
			if (!toRemove.getOrdinations().isEmpty()) {
				throw new BadRequestException("Il tavolo contiene delle comande");
			}
			dao.deleteByUuid(uuid);
			notifyDiningTablesUpdate();
		} else {
			throw new BadRequestException(EVENING_NOT_FOUND);
		}
	}
}
