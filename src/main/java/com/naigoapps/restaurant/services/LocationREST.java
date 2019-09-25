/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.print.PrintException;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.LocationDTO;
import com.naigoapps.restaurant.services.dto.MessageRequestDTO;
import com.naigoapps.restaurant.services.dto.mappers.LocationMapper;
import com.naigoapps.restaurant.services.filters.Accessible;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingService.Size;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;

/**
 *
 * @author naigo
 */
@Accessible
@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class LocationREST {

    @Inject
    private LocationDao dao;

    @Inject
    private PrinterDao pDao;

    @Inject
    private DiningTableDao dtDao;
    
    @Inject
    private LocationMapper mapper;
    
    @GET
    public List<LocationDTO> getLocations() {
        return dao.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GET
    @Path("{uuid}")
    public LocationDTO findLocation(@PathParam("uuid") String uuid) {
    	return mapper.map(dao.findByUuid(uuid));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createLocation() {
    	Location location = new Location();
    	dao.persist(location);
    	return location.getUuid();
    }

    @PUT
    @Path("{uuid}/name")
    public LocationDTO updateLocationName(@PathParam("uuid") String uuid, String name) {
        Location l = dao.findByUuid(uuid);
        if (l != null) {
            l.setName(name);
            return mapper.map(l);
        } else {
            throw new BadRequestException("Postazione non trovata");
        }
    }

    @PUT
    @Path("{uuid}/printer")
    public LocationDTO updateLocationPrinter(@PathParam("uuid") String uuid, String printer) {
        Location l = dao.findByUuid(uuid);
        Printer p = pDao.findByUuid(printer);
		if (l != null) {
			if (p != null) {
				l.setPrinter(p);
				return mapper.map(l);
			} else {
				throw new BadRequestException("Stampante non trovata");
			}
		} else {
			throw new BadRequestException("Postazione non trovata");
        }
    }
    
    @DELETE
    @Path("{uuid}")
    public void deleteLocation(@PathParam("uuid") String uuid){
        dao.deleteByUuid(uuid);   
    }



	@POST
	@Path("message")
	@Transactional
	public Response printMessage(MessageRequestDTO message) throws PrintException {
		DiningTable table = null;
		if(message.getTableUuid() != null) {
			table = dtDao.findByUuid(message.getTableUuid());
		}
		List<Location> locations = Arrays.stream(message.getLocationUuids())
				.map(dao::findByUuid)
				.collect(Collectors.toList());
		
		Set<Printer> printers = locations.stream()
				.map(Location::getPrinter)
				.collect(Collectors.toSet());
		for(Printer p : printers) {
			try {
				if (p != null) {
					PrintingService service = PrintingServiceProvider.get(p);
					service.lf(5).size(Size.STANDARD).printCenter("MESSAGGIO");
					if(table != null) {
						service.printCenter("Tavolo: " + table.getTable().getName())
						.printCenter("Cam. " + table.getWaiter().getName());
					}
					service.printCenter(message.getMessage())
					.lf(5)
					.cut()
					.doPrint();
				}
			} catch (IOException ex) {
				return ResponseBuilder.badRequest(ex.getMessage());
			}
		}
		return ResponseBuilder.ok();
	}
    
}
