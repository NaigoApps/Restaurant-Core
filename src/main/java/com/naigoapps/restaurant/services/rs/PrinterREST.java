/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.SUCCESS;

import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.dao.LocationDao;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.WrapperDTO;
import com.naigoapps.restaurant.services.dto.mappers.PrinterMapper;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraGateway;
import com.naigoapps.restaurant.services.fiscal.hydra.HydraPrintingService;
import com.naigoapps.restaurant.services.fiscal.hydra.Pair;
import com.naigoapps.restaurant.services.fiscal.hydra.fsms.FeedFSM;
import com.naigoapps.restaurant.services.fiscal.hydra.fsms.OpenDrawerFSM;
import com.naigoapps.restaurant.services.fiscal.hydra.fsms.StatusRequestFSM;
import com.naigoapps.restaurant.services.fiscal.hydra.fsms.TestPrintFSM;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.DefaultResponse;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author naigo
 */
@RequestMapping("/rest/printers")
@RestController
@Transactional
public class PrinterREST {

    @Autowired
    private PrinterDao dao;

    @Autowired
    private LocationDao lDao;

    @Autowired
    private PrinterMapper mapper;

    @Autowired
    private HydraPrintingService fpm;

    @Autowired
    private SettingsDao sDao;

    @Autowired
    private HydraGateway gateway;

    @GetMapping("fiscal/status")
    public List<Pair> getFiscalPrinterStatus() {
        StatusRequestFSM fsm = new StatusRequestFSM(gateway);
        fsm.start();
        if (fsm.getCurrentStatus() != SUCCESS) {
            throw new RuntimeException("Operazione non eseguita");
        }
        Response r = new DefaultResponse();
        r.populate(fsm.getData());
        return r.getResult();
    }

    @GetMapping("fiscal/open")
    public void open(@RequestParam("password") String password) {
        if (sDao.find().getCashPassword().equals(password)) {
            OpenDrawerFSM fsm = new OpenDrawerFSM(gateway);
            fsm.start();
            if (fsm.getCurrentStatus() != SUCCESS) {
                throw new RuntimeException("Operazione non eseguita");
            }
        } else {
            throw new RuntimeException("Password non corretta");
        }
    }

    @GetMapping("fiscal/feed")
    public void feed() {
        FeedFSM fsm = new FeedFSM(gateway);
        fsm.start();
        if (fsm.getCurrentStatus() != SUCCESS) {
            throw new RuntimeException("Operazione non eseguita");
        }
    }

    @PostMapping("fiscal/test-print")
    public void testPrint() {
        TestPrintFSM fsm = new TestPrintFSM(gateway);
        fsm.start();
        if (fsm.getCurrentStatus() != SUCCESS) {
            throw new RuntimeException("Operazione non eseguita");
        }
    }

    @PostMapping
    public String createPrinter() {
        Printer printer = new Printer();
        dao.persist(printer);
        return printer.getUuid();
    }

    @PutMapping("{uuid}/name")
    public void updateName(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> name) {
        dao.findByUuid(uuid).setName(name.getValue());
    }

    @PutMapping("{uuid}/address")
    public void updateAddress(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> address) {
        dao.findByUuid(uuid).setAddress(address.getValue());
    }

    @PutMapping("{uuid}/port")
    public void updatePort(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<String> port) {
        dao.findByUuid(uuid).setPort(port.getValue());
    }

    @PutMapping("{uuid}/lineCharacters")
    public void updatePrinterLineCharacters(@PathVariable("uuid") String uuid,
        @RequestBody WrapperDTO<Integer> chars) {
        dao.findByUuid(uuid).setLineCharacters(chars.getValue());
    }

    @GetMapping("services")
    public List<String> getPrintServices() {
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, attributes);

        return Arrays.stream(printServices).map(PrintService::getName).collect(Collectors.toList());
    }

    @GetMapping
    public List<PrinterDTO> getPrinters() {
        return dao.findAll().stream().map(mapper::map).collect(Collectors.toList());
    }

    @GetMapping("{uuid}")
    public PrinterDTO findByUuid(@PathVariable("uuid") String uuid) {
        return mapper.map(dao.findByUuid(uuid));
    }

    @DeleteMapping("{uuid}")
    public void deletePrinter(@PathVariable("uuid") String uuid) {
        Printer p = dao.findByUuid(uuid);
        if (p != null) {
            List<Location> locations = lDao.findByPrinter(p);
            if (locations.isEmpty()) {
                dao.deleteByUuid(uuid);
            } else if (locations.size() > 1) {
                throw new RuntimeException("Stampante utilizzata nelle postazioni "
                    + locations.stream().map(Location::getName).collect(Collectors.joining(", ")));
            } else {
                throw new RuntimeException(
                    "Stampante utilizzata nella postazione " + locations.get(0).getName());
            }
        } else {
            throw new RuntimeException("Stampante non trovata");
        }
    }

}
