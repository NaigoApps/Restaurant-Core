package com.naigoapps.restaurant.services.rs;

import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.services.BillsService;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.mappers.BillMapper;
import com.naigoapps.restaurant.services.dto.mappers.OrdinationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author naigo
 */
@RequestMapping("/rest/bills")
@RestController
@Transactional
public class BillsREST {

    @Autowired
    private BillMapper mapper;

    @Autowired
    private OrdinationMapper oMapper;

    @Autowired
    private BillsService billsService;

    @GetMapping
    public List<BillDTO> getBills() {
        return billsService.findEveningBills().stream().map(mapper::map).collect(Collectors.toList());
    }

    @GetMapping("{billUuid}")
    public BillDTO getBill(@PathVariable("billUuid") String billUuid) {
        return mapper.map(billsService.findBill(billUuid));
    }

    @GetMapping("remaining/{tableUuid}")
    public DiningTableDTO getRemaining(@PathVariable("tableUuid") String tableUuid) {
        int ccsToPay = billsService.getCoverChargesToPay(tableUuid);
        List<Order> ordersToPay = billsService.getOrdersToPay(tableUuid);

        DiningTableDTO result = new DiningTableDTO();
        result.setCoverCharge(billsService.getTableCoverCharge(tableUuid));
        result.setCoverCharges(ccsToPay);
        result.setOrders(oMapper.group(ordersToPay));
        result.setTotal(result.getCoverCharges() * result.getCoverCharge()
                + result.getOrders().stream().mapToDouble(o -> o.getPrice() * o.getQuantity()).sum());

        return result;
    }

    @PostMapping
    public String addBill(@RequestParam("table") String tableUuid, @RequestBody CreateBillParams params) {
        return billsService.addBill(tableUuid, params);
    }

    @PostMapping("quick")
    public BillDTO quickBill(@RequestParam("table") String tableUuid) {
        return mapper.map(billsService.quickBill(tableUuid));
    }

    @PostMapping("{uuid}/soft-print")
    public void printSoftBill(@PathVariable("uuid") String billUuid,
                              @RequestParam(required = false, value = "generic") Boolean generic) {
        billsService.printSoftBill(billUuid, generic);
    }

    @PostMapping("{uuid}/print")
    public void printBill(@PathVariable("uuid") String billUuid, @RequestBody(required = false) String customerId,
                          @RequestParam(required = false, value = "generic") Boolean generic) {
        billsService.printBill(billUuid, customerId);
    }

    @DeleteMapping("{uuid}")
    public void removeBill(@PathVariable("uuid") String billUuid) {
        billsService.removeBill(billUuid);
    }

    @DeleteMapping("{uuid}/deep")
    public void removeBillAndOrders(@PathVariable("uuid") String billUuid) {
        billsService.removeBillAndOrders(billUuid);
    }

}
