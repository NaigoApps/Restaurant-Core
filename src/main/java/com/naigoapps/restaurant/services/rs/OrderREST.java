/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.dao.DiningTableDao;
import com.naigoapps.restaurant.model.dao.OrderDao;
import com.naigoapps.restaurant.model.dao.OrdinationDao;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.mappers.OrderMapper;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/orders")
@RestController
@Transactional
public class OrderREST {

    @Autowired
    private EveningManager eveningManager;

    @Autowired
    private OrderDao rdDao;

    @Autowired
    private OrdinationDao oDao;

    @Autowired
    private DiningTableDao dTDao;
    
    @Autowired
    private OrderMapper mapper;

    @GetMapping
    public List<OrderDTO> getOrders() {
        Evening e = eveningManager.getSelectedEvening();
        List<OrderDTO> orders = new ArrayList<>();
        e.getDiningTables().forEach(table -> 
            orders.addAll(table.getOrders().stream()
                    .map(mapper::map)
                    .collect(Collectors.toList())));
        return orders;
    }

    @GetMapping("{tableUuid}")
    public List<OrderDTO> getOrders(@PathVariable("tableUuid") String tableUuid) {
        DiningTable table = dTDao.findByUuid(tableUuid);
        return table.getOrders().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PutMapping("{uuid}/price")
    public void updatePrice(@PathVariable("uuid") String orderUuid, @RequestBody float price) {
        rdDao.findByUuid(orderUuid).setPrice(price);
    }

    @DeleteMapping
    public void deleteOrders(@RequestBody String[] orderUuids) {
        List<Order> orders = Arrays.stream(orderUuids)
                .map(uuid -> rdDao.findByUuid(uuid))
                .collect(Collectors.toList());
        if (orders.stream().allMatch(order -> order.getOrdination().equals(orders.get(0).getOrdination()))) {
            if (orders.stream().allMatch(order -> order.getBill() == null)) {
                Ordination targetOrdination = orders.get(0).getOrdination();
                if (!targetOrdination.getOrders().stream()
                        .allMatch(orders::contains)) {
                    orders.forEach(order -> {
                        order.setOrdination(null);
                        rdDao.getEntityManager().remove(order);
                    });
                    if (targetOrdination.getOrders().isEmpty()) {
                        targetOrdination.setTable(null);
                        oDao.getEntityManager().remove(targetOrdination);
                    }
                } else {
                	throw new RuntimeException("La comanda verrebbe eliminata");
                }
            }
            throw new RuntimeException("Alcuni ordini hanno conti associati");
        }
        throw new RuntimeException("Ordini non provenienti dalla stessa comanda");
    }

}
