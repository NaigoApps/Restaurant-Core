/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.print.DocFlavor;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.mappers.PrinterMapper;
import com.naigoapps.restaurant.services.filters.Accessible;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraPrintingService;
import com.naigoapps.restaurant.services.fiscal.hydra.Pair;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.DeviceStatusRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.FeedRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.OpenDrawerRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.DefaultResponse;

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
	private PrinterDao dao;

	@Inject
	private LocationDao lDao;

	@Inject
	private PrinterMapper mapper;

	@Inject
	private HydraPrintingService fpm;

	@Inject
	private SettingsDao sDao;
	
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
	public void open(@QueryParam("password") String password) {
		if(sDao.find().getCashPassword().equals(password)) {
			DefaultResponse response = new DefaultResponse();
			fpm.sendRequest(new OpenDrawerRequest(), response);
			if(!response.wasSuccessful()) {
				throw new InternalServerErrorException("Operazione non eseguita");
			}
		}else {
			throw new BadRequestException("Password non corretta");
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
	@Path("{uuid}/address")
	public void updateAddress(@PathParam("uuid") String uuid, String address) {
		dao.findByUuid(uuid).setAddress(address);
	}

	@PUT
	@Path("{uuid}/port")
	public void updatePort(@PathParam("uuid") String uuid, String port) {
		dao.findByUuid(uuid).setPort(port);
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

}
