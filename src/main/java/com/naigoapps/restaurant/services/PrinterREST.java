/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.model.extra.QuantifiedOrders;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.mappers.PrinterMapper;
import com.naigoapps.restaurant.services.filters.Accessible;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraPrintingService;
import com.naigoapps.restaurant.services.fiscal.hydra.Pair;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.DeviceStatusRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.FeedRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.OpenDrawerRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.DefaultResponse;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingService.Size;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/printers")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class PrinterREST {

	@Inject
	private SettingsDao sDao;

	@Inject
	private OrdinationDao ordDao;

	@Inject
	private DiningTableDao dtDao;

	@Inject
	private PrinterDao dao;

	@Inject
	private LocationDao lDao;

	@Inject
	private PrinterMapper mapper;

	@Inject
	private HydraPrintingService fpm;

	@GET
	@Path("fiscal/status")
	public List<Pair> getFiscalPrinterStatus() {
		DefaultResponse response = new DefaultResponse();
		fpm.sendRequest(new DeviceStatusRequest(), response);
		if(!response.wasSuccessful()) {
			throw new InternalServerErrorException("Errore di comunicazione");
		}
		return response.getResult();
	}
	
	@GET
	@Path("fiscal/open")
	public void open() {
		DefaultResponse response = new DefaultResponse();
		fpm.sendRequest(new OpenDrawerRequest(), response);
		if(!response.wasSuccessful()) {
			throw new InternalServerErrorException("Operazione non eseguita");
		}
	}
	
	@GET
	@Path("fiscal/feed")
	public void feed() {
		DefaultResponse response = new DefaultResponse();
		fpm.sendRequest(new FeedRequest(), response);
		if(!response.wasSuccessful()) {
			throw new InternalServerErrorException("Operazione non eseguita");
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String createPrinter() {
		Printer printer = new Printer();
		dao.persist(printer);
		return printer.getUuid();
	}

	@PUT
	@Path("{uuid}/name")
	public void updateName(@PathParam("uuid") String uuid, String name) {
		dao.findByUuid(uuid).setName(name);
	}

	@PUT
	@Path("{uuid}/lineCharacters")
	public void updatePrinterLineCharacters(@PathParam("uuid") String uuid, int chars) {
		dao.findByUuid(uuid).setLineCharacters(chars);
	}

	@GET
	@Path("services")
	public List<String> getPrintServices() {
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, attributes);

		return Arrays.stream(printServices).map(PrintService::getName).collect(Collectors.toList());
	}

	@GET
	public List<PrinterDTO> getPrinters() {
		return dao.findAll().stream().map(mapper::map).collect(Collectors.toList());
	}
	
	@GET
	@Path("{uuid}")
	public PrinterDTO findByUuid(@PathParam("uuid") String uuid) {
		return mapper.map(dao.findByUuid(uuid));
	}

	@DELETE
	@Path("{uuid}")
	@Produces(MediaType.TEXT_PLAIN)
	public void deletePrinter(@PathParam("uuid") String uuid) {
		Printer p = dao.findByUuid(uuid);
		if (p != null) {
			List<Location> locations = lDao.findByPrinter(p);
			if (locations.isEmpty()) {
				dao.deleteByUuid(uuid);
			} else if (locations.size() > 1) {
				throw new BadRequestException("Stampante utilizzata nelle postazioni "
						+ locations.stream().map(Location::getName).collect(Collectors.joining(", ")));
			} else {
				throw new BadRequestException("Stampante utilizzata nella postazione " + locations.get(0).getName());
			}
		}else {
			throw new BadRequestException("Stampante non trovata");
		}
	}

	@POST
	@Path("print")
	@Transactional
	public void printOrdination(String ordinationUuid) throws PrintException {
		Ordination ord = ordDao.findByUuid(ordinationUuid);
		List<Printer> printers = dao.findAll();
		Map<Phase, QuantifiedOrders> phasesMap = new HashMap<>();
		Settings s = sDao.find();
		try {
			for (Printer p : printers) {
				PrintingService service = PrintingServiceProvider.get(p);
				phasesMap.clear();
				boolean needToPrint = false;
				for (Order order : ord.getOrders()) {
					if (p.equals(order.getDish().getCategory().getLocation().getPrinter())) {
						needToPrint = true;
					}
				}
				if (needToPrint) {
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
							.printCenter("Comanda " + ord.getProgressive() + " Tavolo: "
									+ ord.getTable().getTable().getName())
							.printCenter(formatTime(ord.getCreationTime()))
							.printCenter("Cam. " + ord.getTable().getWaiter().getName());
					printPhases(service, phasesMap).lf(6).cut().doPrint();
				}
			}
		} catch (IOException ex) {
			throw new InternalServerErrorException("Errore di stampa");
		}
		ord.setDirty(false);
	}

	@POST
	@Path("abort")
	@Transactional
	public void printOrdinationAbort(String ordinationUuid) throws PrintException {
		Ordination ord = ordDao.findByUuid(ordinationUuid);
		List<Printer> printers = dao.findAll();
		try {
			for (Printer p : printers) {
				PrintingService service = PrintingServiceProvider.get(p);
				boolean needToPrint = false;
				for (Order order : ord.getOrders()) {
					if (p.equals(order.getDish().getCategory().getLocation().getPrinter())) {
						needToPrint = true;
					}
				}
				if (needToPrint) {
					service.lf(5).size(Size.STANDARD).printCenter("ANNULLAMENTO")
							.printCenter("Comanda " + ord.getProgressive() + " " + formatTime(ord.getCreationTime()))
							.printCenter("Tavolo: " + ord.getTable().getTable().getName())
							.printCenter("Cam. " + ord.getTable().getWaiter().getName()).lf(5).cut().doPrint();
				}
			}
		} catch (IOException ex) {
			throw new BadRequestException("Errore di stampa");
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

	private String formatTime(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return date.format(formatter);
	}

}
