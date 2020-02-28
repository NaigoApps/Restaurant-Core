/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.services.dto.WrapperDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.print.PrintException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.services.dto.LocationDTO;
import com.naigoapps.restaurant.services.dto.MessageRequestDTO;
import com.naigoapps.restaurant.services.dto.mappers.LocationMapper;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingService.Size;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/locations")
@RestController
@Transactional
public class LocationREST {

    @Autowired
    private LocationDao dao;

    @Autowired
    private PrinterDao pDao;

    @Autowired
    private DiningTableDao dtDao;
    
    @Autowired
    private LocationMapper mapper;
    
    @GetMapping
    public List<LocationDTO> getLocations() {
        return dao.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    
    @GetMapping("{uuid}")
    public LocationDTO findLocation(@PathVariable("uuid") String uuid) {
    	return mapper.map(dao.findByUuid(uuid));
    }

    @PostMapping
    public String createLocation() {
    	Location location = new Location();
    	dao.persist(location);
    	return location.getUuid();
    }

    @PutMapping("{uuid}/name")
    public LocationDTO updateLocationName(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> name) {
        Location l = dao.findByUuid(uuid);
        if (l != null) {
            l.setName(name.getValue());
            return mapper.map(l);
        } else {
            throw new RuntimeException("Postazione non trovata");
        }
    }

    @PutMapping("{uuid}/printer")
    public LocationDTO updateLocationPrinter(@PathVariable("uuid") String uuid, @RequestBody WrapperDTO<String> printer) {
        Location l = dao.findByUuid(uuid);
        Printer p = pDao.findByUuid(printer.getValue());
		if (l != null) {
			if (p != null) {
				l.setPrinter(p);
				return mapper.map(l);
			} else {
				throw new RuntimeException("Stampante non trovata");
			}
		} else {
			throw new RuntimeException("Postazione non trovata");
        }
    }
    
    @DeleteMapping("{uuid}")
    public void deleteLocation(@PathVariable("uuid") String uuid){
        dao.deleteByUuid(uuid);   
    }



	@PostMapping("message")
	public void printMessage(@RequestBody MessageRequestDTO message) throws PrintException {
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
				throw new RuntimeException(ex);
			}
		}
	}
    
}
