/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.print.PrintException;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.builders.OrdinationBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.services.dto.OrdersGroupDTO;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.PhaseOrdersDTO;
import com.naigoapps.restaurant.services.dto.mappers.OrdinationMapper;

/**
 *
 * @author naigo
 */
@Path("/dining-tables/{tableUuid}/ordinations")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class OrdinationREST {

	@PathParam("tableUuid")
	private String tableUuid;

	private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

	@Inject
	private Locks locks;

	@Inject
	private EveningManager eveningManager;

	@Inject
	private OrdinationDao ordDao;

	@Inject
	private OrderDao oDao;

	@Inject
	private DishDao dDao;

	@Inject
	private DiningTableDao dTDao;

	@Inject
	private PhaseDao pDao;

	@Inject
	private AdditionDao aDao;

	@Inject
	private PrinterREST printService;

	@Inject
	private OrdinationWS wsService;

	@Inject
	private OrdinationMapper mapper;

	@POST
	@Path("skeleton")
	public OrdinationDTO createOrdination() {
		if (tableUuid != null) {
			Evening e = eveningManager.getSelectedEvening();
			DiningTable table = dTDao.findByUuid(tableUuid);
			if (table != null && table.getEvening().equals(e)) {
				Ordination ordination;
				synchronized (locks.ORDINATION_PROGRESSIVE()) {
					ordination = new OrdinationBuilder().progressive(ordDao.nextProgressive(e))
							.creationTime(LocalDateTime.now()).table(table).dirty(false).getContent();
					oDao.persist(ordination);
					if (table.getBills().isEmpty()) {
						table.setStatus(DiningTableStatus.OPEN);
					} else {
						table.setStatus(DiningTableStatus.CLOSING);
					}
				}
				wsService.update(tableUuid);
				return mapper.map(ordination);
			}
			throw new BadRequestException(TABLE_NOT_FOUND);
		}
		throw new BadRequestException("Tavolo o ordini non validi");
	}
	
	@POST
	public OrdinationDTO createOrdination(OrdinationDTO ordDto) {
		if (tableUuid != null && ordDto != null && ordDto.getOrders() != null && !ordDto.getOrders().isEmpty()) {
			Evening e = eveningManager.getSelectedEvening();
			DiningTable table = dTDao.findByUuid(tableUuid);
			if (table != null && table.getEvening().equals(e)) {
				Ordination ordination;
				synchronized (locks.ORDINATION_PROGRESSIVE()) {
					ordination = new OrdinationBuilder().progressive(ordDao.nextProgressive(e))
							.creationTime(LocalDateTime.now()).table(table).dirty(true).getContent();
					oDao.persist(ordination);
					persistOrders(ordination, ordDto.getOrders());
					if (table.getBills().isEmpty()) {
						table.setStatus(DiningTableStatus.OPEN);
					} else {
						table.setStatus(DiningTableStatus.CLOSING);
					}
				}
				wsService.update(tableUuid);
				return mapper.map(ordination);
			}
			throw new BadRequestException(TABLE_NOT_FOUND);
		}
		throw new BadRequestException("Tavolo o ordini non validi");
	}

	private void persistOrders(Ordination ordination, List<PhaseOrdersDTO> orders) {
		orders.forEach(phase -> {
			phase.getOrders().forEach(group -> {
				for (int i = 0; i < group.getQuantity(); i++) {
					OrderBuilder oBuilder = new OrderBuilder();
					Order o = oBuilder.dish(dDao.findByUuid(group.getDish().getUuid())).ordination(ordination)
							.price(group.getPrice()).phase(pDao.findByUuid(phase.getPhase().getUuid()))
							.notes(group.getNotes()).getContent();
					group.getAdditions().forEach(a -> o.addAddition(aDao.findByUuid(a.getUuid())));
					oDao.persist(o);
				}
			});
		});
	}

	@GET
	@Path("{uuid}")
	public OrdinationDTO getOrdination(@PathParam("uuid") String ordinationUuid) {
		return mapper.map(ordDao.findByUuid(ordinationUuid));
	}

	@GET
	public List<OrdinationDTO> getOrdinations() {
		Evening e = eveningManager.getSelectedEvening();
		DiningTable table = dTDao.findByUuid(tableUuid);
		if (e != null) {
			return table.getOrdinations().stream().map(mapper::map).collect(Collectors.toList());
		} else {
			throw new BadRequestException("Serata non selezionata");
		}
	}

	@PUT
	@Path("{uuid}")
	public OrdinationDTO editOrdination(@PathParam("uuid") String ordinationUuid, OrdinationDTO dto) {
		if (dto != null && !dto.getOrders().isEmpty()) {
			Ordination ordination = ordDao.findByUuid(ordinationUuid);
			if (ordination != null) {
				List<Order> oldOrders = new ArrayList<>(ordination.getOrders());
				List<Order> newOrders = buildOrders(dto.getOrders());

				List<Order> oldFixedOrders = oldOrders.stream().filter(order -> order.getBill() != null)
						.collect(Collectors.toList());

				for (Order requiredOrder : oldFixedOrders) {
					Order newMatchingOrder = findMatchingOrder(newOrders, requiredOrder);
					if (newMatchingOrder != null) {
						newMatchingOrder.setBill(requiredOrder.getBill());
					} else {
						throw new BadRequestException("Impossibile modificare ordini associati ad un conto");
					}
				}

				ordination.getOrders().forEach(o -> {
					if (o.getBill() != null) {
						o.getBill().removeOrder(o);
					}
					o.clearAdditions();
					oDao.delete(o);
				});
				ordination.clearOrders();

				for (Order order : newOrders) {
					order.setOrdination(ordination);
					oDao.persist(order);
				}
				ordination.setDirty(true);
				ordination.getTable().updateStatus();
				return mapper.map(ordination);
			}
			throw new BadRequestException("Comanda non trovata");
		}
		throw new BadRequestException("Dati non validi");
	}

	private Order findMatchingOrder(List<Order> orders, Order target) {
		for (Order order : orders) {
			if (order.isTheSame(target) && order.getBill() == null) {
				return order;
			}
		}
		return null;
	}

	private List<Order> buildOrders(List<PhaseOrdersDTO> dtos) {
		OrderBuilder builder = new OrderBuilder();
		List<Order> result = new ArrayList<>();
		for (PhaseOrdersDTO order : dtos) {
			for (OrdersGroupDTO group : order.getOrders()) {
				for (int i = 0; i < group.getQuantity(); i++) {
					Order o = builder.dish(dDao.findByUuid(group.getDish().getUuid())).price(group.getPrice())
							.phase(pDao.findByUuid(order.getPhase().getUuid())).notes(group.getNotes()).getContent();
					List<Addition> additions = new ArrayList<>();
					group.getAdditions().stream().forEach(a -> {
						Addition addition = aDao.findByUuid(a.getUuid());
						additions.add(addition);
					});
					o.setAdditions(additions);
					result.add(o);
				}
			}
		}
		return result;
	}

	@PUT
	@Path("{uuid}/abort")
	@Produces(MediaType.TEXT_PLAIN)
	public void sendOrdinationAbort(@PathParam("uuid") String ordinationUuid) {
		Evening e = eveningManager.getSelectedEvening();
		Ordination ordination = ordDao.findByUuid(ordinationUuid);
		if (ordination != null && e.equals(ordination.getTable().getEvening())) {
			try {
				printService.printOrdinationAbort(ordinationUuid);
			} catch (PrintException ex) {
				throw new InternalServerErrorException("Impossibile stampare");
			}
		} else {
			throw new BadRequestException("Dati non validi");
		}
	}

	@DELETE
	@Path("{uuid}")
	public void deleteOrdination(@PathParam("uuid") String ordUuid) {
		Evening currentEvening = eveningManager.getSelectedEvening();
		Ordination ordination = ordDao.findByUuid(ordUuid);
		if (ordination != null) {
			DiningTable table = ordination.getTable();
			if (currentEvening != null && currentEvening.equals(ordination.getTable().getEvening())) {
				List<Order> orders = ordination.getOrders();
				if (orders.stream().allMatch(order -> order.getBill() == null)) {
					ordination.setTable(null);
					ordination.clearOrders();

					oDao.getEntityManager().remove(ordination);
					orders.forEach(order -> oDao.getEntityManager().remove(order));
					table.updateStatus();
				} else {
					throw new BadRequestException("Alcuni ordini hanno conti associati");
				}
			}
		} else {
			throw new BadRequestException("Ordinazione non trovata");
		}
	}
}
