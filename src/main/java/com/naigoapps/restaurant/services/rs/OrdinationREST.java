/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.print.PrintException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.builders.OrderBuilder;
import com.naigoapps.restaurant.model.builders.OrdinationBuilder;
import com.naigoapps.restaurant.model.dao.AdditionDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.DishDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PhaseDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.model.extra.QuantifiedOrders;
import com.naigoapps.restaurant.services.OrdinationsUpdatedEvent;
import com.naigoapps.restaurant.services.dto.OrdersGroupDTO;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.PhaseOrdersDTO;
import com.naigoapps.restaurant.services.dto.mappers.OrdinationMapper;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingService.Size;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import com.naigoapps.restaurant.services.websocket.OrdinationWS;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/dining-tables/{tableUuid}/ordinations")
@RestController
@Transactional
public class OrdinationREST {

	private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

	@Autowired
	private EveningManager eveningManager;

	@Autowired
	private OrdinationDao ordDao;

	@Autowired
	private OrderDao oDao;

	@Autowired
	private DishDao dDao;

	@Autowired
	private DiningTableDao dTDao;

	@Autowired
	private PhaseDao pDao;

	@Autowired
	private AdditionDao aDao;

	@Autowired
	private PrinterDao printerDao;

	@Autowired
	private SettingsDao sDao;

	@Autowired
	private OrdinationWS wsService;

	@Autowired
	private OrdinationMapper mapper;

	@Autowired
    private ApplicationEventPublisher publisher;

	@PostMapping("skeleton")
	public OrdinationDTO createOrdination(@PathVariable("tableUuid") String tableUuid) {
		if (tableUuid != null) {
			Evening e = eveningManager.getSelectedEvening();
			DiningTable table = dTDao.findByUuid(tableUuid);
			if (table != null && table.getEvening().equals(e)) {
				Ordination ordination;
				ordination = new OrdinationBuilder().progressive(ordDao.nextProgressive(e))
						.creationTime(LocalDateTime.now()).table(table).dirty(false).getContent();
				oDao.persist(ordination);
				if (table.getBills().isEmpty()) {
					table.setStatus(DiningTableStatus.OPEN);
				} else {
					table.setStatus(DiningTableStatus.CLOSING);
				}
				notifyOrdinationsUpdate(tableUuid);
				return mapper.map(ordination);
			}
			throw new RuntimeException(TABLE_NOT_FOUND);
		}
		throw new RuntimeException("Tavolo o ordini non validi");
	}

	@PostMapping
	public OrdinationDTO createOrdination(@PathVariable("tableUuid") String tableUuid, @RequestBody OrdinationDTO ordDto) {
		if (tableUuid != null && ordDto != null && ordDto.getOrders() != null && !ordDto.getOrders().isEmpty()) {
			Evening e = eveningManager.getSelectedEvening();
			DiningTable table = dTDao.findByUuid(tableUuid);
			if (table != null && table.getEvening().equals(e)) {
				Ordination ordination;
				ordination = new OrdinationBuilder().progressive(ordDao.nextProgressive(e))
						.creationTime(LocalDateTime.now()).table(table).dirty(true).getContent();
				oDao.persist(ordination);
				persistOrders(ordination, ordDto.getOrders());
				if (table.getBills().isEmpty()) {
					table.setStatus(DiningTableStatus.OPEN);
				} else {
					table.setStatus(DiningTableStatus.CLOSING);
				}
				notifyOrdinationsUpdate(tableUuid);
				return mapper.map(ordination);
			}
			throw new RuntimeException(TABLE_NOT_FOUND);
		}
		throw new RuntimeException("Tavolo o ordini non validi");
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

	@GetMapping("{uuid}")
	public OrdinationDTO getOrdination(@PathVariable("uuid") String ordinationUuid) {
		return mapper.map(ordDao.findByUuid(ordinationUuid));
	}

	@GetMapping
	public List<OrdinationDTO> getOrdinations(@PathVariable("tableUuid") String tableUuid) {
		Evening e = eveningManager.getSelectedEvening();
		DiningTable table = dTDao.findByUuid(tableUuid);
		if (e != null) {
			return table.getOrdinations().stream().map(mapper::map).collect(Collectors.toList());
		} else {
			throw new RuntimeException("Serata non selezionata");
		}
	}

	@PutMapping("{uuid}")
	public OrdinationDTO editOrdination(@PathVariable("tableUuid") String tableUuid,
			@PathVariable("uuid") String ordinationUuid, @RequestBody OrdinationDTO dto) {
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
						throw new RuntimeException("Impossibile modificare ordini associati ad un conto");
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
				notifyOrdinationsUpdate(tableUuid);
				return mapper.map(ordination);
			}
			throw new RuntimeException("Comanda non trovata");
		}
		throw new RuntimeException("Dati non validi");
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

	@PutMapping("{uuid}/abort")
	public void sendOrdinationAbort(@PathVariable("tableUuid") String tableUuid,
			@PathVariable("uuid") String ordinationUuid) {
		Evening e = eveningManager.getSelectedEvening();
		Ordination ordination = ordDao.findByUuid(ordinationUuid);
		if (ordination != null && e.equals(ordination.getTable().getEvening())) {
			try {
				Set<Printer> printers = targetPrinters(ordination);
				for (Printer p : printers) {
					PrintingService service = PrintingServiceProvider.get(p);
					service.lf(5).size(Size.STANDARD).printCenter("ANNULLAMENTO")
							.printCenter("Comanda " + ordination.getProgressive() + " "
									+ PrintingService.formatTime(ordination.getCreationTime()))
							.printCenter("Tavolo: " + ordination.getTable().getTable().getName())
							.printCenter("Cam. " + ordination.getTable().getWaiter().getName()).lf(5).cut().doPrint();
				}
				ordination.setDirty(false);
				notifyOrdinationsUpdate(tableUuid);
			} catch (PrintException | IOException ex) {
				throw new RuntimeException("Impossibile stampare");
			}
		} else {
			throw new RuntimeException("Dati non validi");
		}
	}

	@PostMapping("{uuid}/print")
	public void printOrdination(@PathVariable("tableUuid") String tableUuid,
			@PathVariable("uuid") String ordinationUuid) throws PrintException {
		Ordination ord = ordDao.findByUuid(ordinationUuid);
		Map<Phase, QuantifiedOrders> phasesMap = new HashMap<>();
		Settings s = sDao.find();
		Set<Printer> targets = targetPrinters(ord);
		try {
			for (Printer p : targets) {
				PrintingService service = PrintingServiceProvider.get(p);
				phasesMap.clear();
				for (Order order : ord.getOrders()) {
					if (p.equals(order.getDish().getCategory().getLocation().getPrinter())
							|| !s.getShrinkOrdinations()) {
						if (!phasesMap.containsKey(order.getPhase())) {
							phasesMap.put(order.getPhase(), new QuantifiedOrders());
						}
						QuantifiedOrders phaseOrders = phasesMap.get(order.getPhase());
						phaseOrders.addOrder(order);
					}
				}
				service.lf(3).size(Size.STANDARD)
						.printCenter(
								"Comanda " + ord.getProgressive() + " Tavolo: " + ord.getTable().getTable().getName())
						.printCenter(PrintingService.formatTime(ord.getCreationTime()))
						.printCenter("Cam. " + ord.getTable().getWaiter().getName());
				printPhases(service, phasesMap).lf(6).cut().doPrint();
				notifyOrdinationsUpdate(tableUuid);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Errore di stampa");
		}
		ord.setDirty(false);
	}

	private PrintingService printPhases(PrintingService service, Map<Phase, QuantifiedOrders> phasesMap)
			throws IOException {
		List<Phase> usedPhases = new ArrayList<>(phasesMap.keySet());
		usedPhases.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));
		for (Phase p : usedPhases) {
			service.size(Size.STANDARD);
			service.printLeft(p.getName() + "...............");
			printPhaseOrders(service, phasesMap.get(p));
		}
		return service;
	}

	private PrintingService printPhaseOrders(PrintingService service, QuantifiedOrders orders) throws IOException {
		for (Order o : orders.getOrders().keySet()) {
			printOrder(service, o, orders.getOrders().get(o));
		}
		return service;
	}

	private PrintingService printOrder(PrintingService service, Order o, Integer quantity) throws IOException {
		if (service.getPrinter().equals(o.getDish().getCategory().getLocation().getPrinter())) {
			service.size(Size.STANDARD);
		} else {
			service.size(Size.SMALL);
		}
		service.printLeft(quantity + " " + o.getDish().getName());
		if (!o.getAdditions().isEmpty()) {
			for (Addition a : o.getAdditions()) {
				service.printLeft(a.getName() + " ");
			}
		}
		if (o.getNotes() != null && !o.getNotes().trim().isEmpty()) {
			service.printLeft(o.getNotes());
		}
		return service;
	}

	private Set<Printer> targetPrinters(Ordination ordination) {
		Set<Printer> result = new HashSet<>();
		List<Printer> printers = printerDao.findAll();
		Map<Phase, QuantifiedOrders> phasesMap = new HashMap<>();
		for (Printer p : printers) {
			phasesMap.clear();
			for (Order order : ordination.getOrders()) {
				if (p.equals(order.getDish().getCategory().getLocation().getPrinter())) {
					result.add(p);
					break;
				}
			}
		}
		return result;
	}

	@DeleteMapping("{uuid}")
	public void deleteOrdination(@PathVariable("tableUuid") String tableUuid, @PathVariable("uuid") String ordUuid) {
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
					notifyOrdinationsUpdate(tableUuid);
				} else {
					throw new RuntimeException("Alcuni ordini hanno conti associati");
				}
			}
		} else {
			throw new RuntimeException("Ordinazione non trovata");
		}
	}

	@EventListener(OrdinationsUpdatedEvent.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void notifyOrdinationsUpdate(OrdinationsUpdatedEvent evt) {
		wsService.update(evt.getDiningTableUuid());
	}

	public void notifyOrdinationsUpdate(String tableUuid) {
		publisher.publishEvent(new OrdinationsUpdatedEvent(tableUuid));
	}

}
