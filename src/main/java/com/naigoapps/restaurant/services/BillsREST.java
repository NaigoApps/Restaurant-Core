package com.naigoapps.restaurant.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.print.PrintException;
import javax.transaction.SystemException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.DiningTableStatus;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.builders.BillBuilder;
import com.naigoapps.restaurant.model.dao.BillDao;
import com.naigoapps.restaurant.model.dao.CustomerDao;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.mappers.BillMapper;
import com.naigoapps.restaurant.services.dto.mappers.OrdinationMapper;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraFilePrintingService;
import com.naigoapps.restaurant.services.printing.BillPrinter;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;

/**
 *
 * @author naigo
 */
@Path("/bills")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class BillsREST {

	private static final String EVENING_NOT_FOUND = "Serata non selezionata";
	private static final String TABLE_NOT_FOUND = "Tavolo non trovato";

	@Inject
	private EveningManager eveningManager;

	@Inject
	private DiningTableDao dtDao;

	@Inject
	private OrderDao oDao;

	@Inject
	private PrinterDao prDao;

	@Inject
	private BillDao dao;

	@Inject
	private CustomerDao cDao;

	@Inject
	private BillMapper mapper;

	@Inject
	private OrdinationMapper oMapper;

//	@Inject
//	private HydraPrintingService fpm;

	@GET
	public List<BillDTO> getBills() {
		Evening e = eveningManager.getSelectedEvening();
		if (e != null) {
			List<BillDTO> bills = new ArrayList<>();
			e.getDiningTables()
					.forEach(dt -> bills.addAll(dt.getBills().stream().map(mapper::map).collect(Collectors.toList())));
			return bills;
		} else {
			throw new BadRequestException(EVENING_NOT_FOUND);
		}
	}

	@GET
	@Path("{billUuid}")
	public BillDTO getBill(@PathParam("billUuid") String billUuid) {
		return mapper.map(dao.findByUuid(billUuid));
	}

	@GET
	@Path("remaining/{tableUuid}")
	public DiningTableDTO getRemaining(@PathParam("tableUuid") String tableUuid) throws SystemException {
		DiningTable table = dtDao.findByUuid(tableUuid);

		int closedCC = table.getBills().stream().mapToInt(Bill::getCoverCharges).sum();

		DiningTableDTO result = new DiningTableDTO();
		result.setCoverCharge(table.getEvening().getCoverCharge());
		result.setCoverCharges(table.getCoverCharges() - closedCC);
		result.setOrders(oMapper
				.group(table.getOrders().stream().filter(o -> o.getBill() == null).collect(Collectors.toList())));
		result.setTotal(result.getCoverCharges() * result.getCoverCharge()
				+ result.getOrders().stream().mapToDouble(o -> o.getPrice() * o.getQuantity()).sum());

		return result;
	}

	@POST
	public String addBill(@QueryParam("table") String tableUuid) {
		Evening currentEvening = eveningManager.getSelectedEvening();
		if (currentEvening != null) {
			DiningTable toUpdate = dtDao.findByUuid(tableUuid);
			if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
				if (!toUpdate.canBeClosed()) {
					Bill bill = new Bill();
					bill.setProgressive(dao.nextBillProgressive(currentEvening));
					bill.setTable(toUpdate);
					dao.persist(bill);
					return bill.getUuid();
				} else {
					throw new BadRequestException("Conto già completato");
				}
			} else {
				throw new BadRequestException(TABLE_NOT_FOUND);
			}
		} else {
			throw new BadRequestException(EVENING_NOT_FOUND);
		}
	}

	@POST
	@Path("quick")
	public BillDTO quickBill(@QueryParam("table") String tableUuid) {
		Evening currentEvening = eveningManager.getSelectedEvening();
		if (currentEvening != null) {
			DiningTable toUpdate = dtDao.findByUuid(tableUuid);
			if (toUpdate != null && toUpdate.getEvening().equals(currentEvening)) {
				List<Order> orders = toUpdate.getOrders().stream().filter(order -> order.getBill() == null)
						.collect(Collectors.toList());
				if (!orders.isEmpty()) {
					if (orders.stream().allMatch(order -> order.getPrice() != 0)) {
						int doneCcs = toUpdate.getBills().stream()
								.collect(Collectors.summingInt(Bill::getCoverCharges));
						int coverCharges = toUpdate.getCoverCharges() - doneCcs;
						float total = orders.stream().collect(Collectors.summingDouble(Order::getPrice)).floatValue();
						total += coverCharges * currentEvening.getCoverCharge();
						Bill b = createBill(toUpdate, orders, coverCharges, total);
						toUpdate.updateStatus();
						return mapper.map(b);
					} else {
						throw new BadRequestException("Assegnare un prezzo a tutti gli ordini");
					}
				} else {
					throw new BadRequestException("Nessun ordine disponibile");
				}
			} else {
				throw new BadRequestException(TABLE_NOT_FOUND);
			}
		} else {
			throw new BadRequestException(EVENING_NOT_FOUND);
		}
	}

	private Bill createBill(DiningTable table, List<Order> orders, int coverCharges, float total) {
		Bill bill = new BillBuilder().progressive(dao.nextBillProgressive(table.getEvening())).table(table)
				.orders(orders).coverCharges(coverCharges).total(total).getContent();
		dtDao.persist(bill);
		return bill;
	}

	private static int getRemainingCoverCharges(DiningTable table) {
		return table.getCoverCharges() - getClosedCoverCharges(table);
	}

	private static int getClosedCoverCharges(DiningTable table) {
		return table.getBills().stream().mapToInt(Bill::getCoverCharges).sum();
	}

	@PUT
	@Path("{billUuid}/ccs")
	public void updateCoverCharges(@PathParam("billUuid") String uuid, Integer ccs) {
		Bill target = dao.findByUuid(uuid);
		DiningTable table = target.getTable();
		target.setCoverCharges(target.getCoverCharges() + ccs);
		target.setTotal(target.getEstimatedTotal());
		if (target.getCoverCharges() < 0 || getClosedCoverCharges(table) > table.getCoverCharges()) {
			throw new BadRequestException("Numero di coperti non valido");
		}
	}

	@PUT
	@Path("{billUuid}/addOrders")
	public void addOrders(@PathParam("billUuid") String uuid, List<OrderDTO> orders) {
		Bill target = dao.findByUuid(uuid);
		orders.stream().map(o -> oDao.findByUuid(o.getUuid()))
				.filter(o -> o.getOrdination().getTable().equals(target.getTable())).filter(o -> o.getBill() == null)
				.forEach(target::addOrder);
		target.setTotal(target.getEstimatedTotal());
	}

	@PUT
	@Path("{billUuid}/removeOrders")
	public void removeOrders(@PathParam("billUuid") String uuid, List<OrderDTO> orders) {
		Bill target = dao.findByUuid(uuid);
		orders.stream().map(o -> oDao.findByUuid(o.getUuid()))
				.filter(o -> o.getOrdination().getTable().equals(target.getTable())).forEach(target::removeOrder);
		target.setTotal(target.getEstimatedTotal());
	}

	@PUT
	@Path("{billUuid}/addAll")
	public void addAll(@PathParam("billUuid") String uuid) {
		Bill target = dao.findByUuid(uuid);
		DiningTable table = target.getTable();
		target.setCoverCharges(target.getCoverCharges() + getRemainingCoverCharges(table));
		table.getOrders().stream().filter(o -> o.getBill() == null).forEach(o -> o.setBill(target));
		target.setTotal(target.getEstimatedTotal());
	}

	@PUT
	@Path("{billUuid}/removeAll")
	public void removeAll(@PathParam("billUuid") String uuid) {
		Bill target = dao.findByUuid(uuid);
		target.clearOrders();
		target.setCoverCharges(0);
		target.setTotal(target.getEstimatedTotal());
	}

	@PUT
	@Path("{billUuid}/total")
	public void updateTotal(@PathParam("billUuid") String uuid, float total) {
		dao.findByUuid(uuid).setTotal(total);
	}

	@POST
	@Path("{uuid}/soft-print")
	public void printSoftBill(@PathParam("uuid") String billUuid, @QueryParam("generic") Boolean generic) {
		Bill bill = dao.findByUuid(billUuid);
		if (bill != null) {
			Printer mainPrinter = prDao.findMainPrinter();
			if (mainPrinter != null) {
				try {
					PrintingService service = PrintingServiceProvider.get(mainPrinter);
					service.accept(new BillPrinter(Boolean.TRUE.equals(generic)), bill, LocalDateTime.now()).lf(3).cut()
							.doPrint();
					mapper.map(bill);
				} catch (IOException ex) {
					throw new InternalServerErrorException("Problema di stampa inaspettato");
				} catch (PrintException ex) {
					throw new InternalServerErrorException("Problema di stampa");
				}
			} else {
				throw new IllegalStateException("Stampante principale non trovata");
			}
		} else {
			throw new BadRequestException("Conto non trovato");
		}
	}

//	@POST
//	@Path("{uuid}/soft-print")
//	public void printBill(@PathParam("uuid") String billUuid, @QueryParam("generic") Boolean generic) {
//		Bill bill = dao.findByUuid(billUuid);
//		if (bill != null) {
//			try {
//				bill.getTable().setStatus(DiningTableStatus.CLOSING);
//				Printer hydraPrinter = new Printer();
//				hydraPrinter.setLineCharacters(48);
//				StringsPrintingService service = new StringsPrintingService(hydraPrinter);
//				service.accept(new SoftBillPrinter(Boolean.TRUE.equals(generic)), bill, LocalDateTime.now());
//
//				bill.getTable().updateStatus();
//
//				List<String> lines = service.getLines();
//				for (int i = 0; i < lines.size(); i++) {
//					String line = lines.get(i);
//					DefaultResponse response = new DefaultResponse();
//					PrintRequest request = new PrintRequest();
//					request.setLine(line);
//					if (i == lines.size() - 1) {
//						request.setLast(true);
//					}
//					fpm.sendRequest(request, response);
//					if (!response.wasSuccessful()) {
//						throw new IllegalStateException("Problema di stampa");
//					}
//				}
//			} catch (IOException ex) {
//				throw new InternalServerErrorException("Problema di stampa inaspettato");
//			}
//		}else {
//			throw new BadRequestException("Conto non trovato");
//		}
//	}

	@POST
	@Path("{uuid}/print")
	@Transactional
	public void printBill(@PathParam("uuid") String billUuid, String customerId,
			@QueryParam("generic") Boolean generic) {
		Bill bill = dao.findByUuid(billUuid);
		Customer customer = null;
		if (customerId != null && !customerId.isEmpty()) {
			customer = cDao.findByUuid(customerId);
		}
		if (bill != null && (customer != null || customerId == null || customerId.isEmpty())) {
			Printer fiscalPrinter = prDao.findMainPrinter();
			if (fiscalPrinter != null) {
				try {
					bill.getTable().setStatus(DiningTableStatus.CLOSING);
					if (customer != null) {
						bill.setCustomer(customer);
						bill.setProgressive(dao.nextInvoiceProgressive(LocalDate.now()));
					} else {
						bill.setProgressive(dao.nextReceiptProgressive(LocalDate.now()));
					}
					bill.setPrintTime(LocalDateTime.now());

					HydraFilePrintingService service = new HydraFilePrintingService();
					service.print(bill);

					bill.getTable().updateStatus();

				} catch (IOException ex) {
					throw new InternalServerErrorException("Problema di stampa inaspettato", ex);
				}
			} else {
				throw new BadRequestException("Stampante fiscale non trovata");
			}
		} else {
			throw new BadRequestException("Conto o cliente non trovato");
		}
	}

	@DELETE
	@Path("{uuid}")
	public void removeBill(@PathParam("uuid") String billUuid) {
		Bill bill = dao.findByUuid(billUuid);
		if (bill != null) {
			DiningTable table = bill.getTable();
			bill.clearOrders();
			bill.setTable(null);
			dao.getEntityManager().remove(bill);
			table.updateStatus();
		} else {
			throw new BadRequestException("Conto non trovato");
		}
	}

	@DELETE
	@Path("{uuid}/deep")
	public void removeBillAndOrders(@PathParam("uuid") String billUuid) {
		Bill bill = dao.findByUuid(billUuid);
		if (bill != null) {
			Set<Order> ordersToRemove = new HashSet<>(bill.getOrders());
			Set<Ordination> ordinationsToCheck = ordersToRemove.stream()
					.map(Order::getOrdination)
					.collect(Collectors.toSet());

			ordersToRemove.forEach(order -> {
				order.setOrdination(null);
				dao.delete(order);
			});

			ordinationsToCheck.stream()
			.filter(ordination -> ordination.getOrders().isEmpty())
			.forEach(ordination -> {
				ordination.setTable(null);
				dao.delete(ordination);
			});

			DiningTable table = bill.getTable();
			table.setCoverCharges(table.getCoverCharges() - bill.getCoverCharges());
			bill.setTable(null);
			bill.clearOrders();
			dao.delete(bill);
			table.updateStatus();
		} else {
			throw new BadRequestException("Conto non trovato");
		}
	}

}
