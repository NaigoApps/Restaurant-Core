/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto.utils;

import com.naigoapps.restaurant.model.Addition;
import com.naigoapps.restaurant.model.BaseEntity;
import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.services.dto.AdditionDTO;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.DishDTO;
import com.naigoapps.restaurant.services.dto.EveningDTO;
import com.naigoapps.restaurant.services.dto.LocationDTO;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.PhaseDTO;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import java.util.stream.Collectors;

/**
 *
 * @author naigo
 */
public class DTOAssembler {

    public static RestaurantTableDTO fromRestaurantTable(RestaurantTable table) {
        if (table != null) {
            return new RestaurantTableDTO(table.getUuid(), table.getName());
        }
        return null;
    }

    public static EveningDTO fromEvening(Evening evening) {
        return new EveningDTO(
                evening.getUuid(),
                evening.getDay(),
                evening.getCoverCharge(),
                evening.getDiningTables().stream()
                        .map(DTOAssembler::fromDiningTable)
                        .collect(Collectors.toList()));
    }

    public static WaiterDTO fromWaiter(Waiter waiter) {
        if (waiter != null) {
            return new WaiterDTO(
                    waiter.getUuid(),
                    waiter.getName(),
                    waiter.getSurname(),
                    waiter.getCf(),
                    waiter.getStatus());
        }
        return null;
    }

    public static PhaseDTO fromPhase(Phase phase) {
        if (phase != null) {
            return new PhaseDTO(phase.getUuid(), phase.getName());
        }
        return null;
    }

    public static PrinterDTO fromPrinter(Printer printer) {
        return new PrinterDTO(printer.getUuid(), printer.getName(), printer.isMain(), printer.getLineCharacters());
    }

    public static LocationDTO fromLocation(Location location) {
        if (location != null) {
            return new LocationDTO(
                    location.getUuid(),
                    location.getName(),
                    location.getPrinter().getUuid());
        } else {
            return null;
        }
    }

    public static CategoryDTO fromCategory(Category category) {
        return new CategoryDTO(
                category.getUuid(),
                category.getName(),
                category.getLocation() != null ? category.getLocation().getUuid() : null,
                category.getDishes().stream()
                        .map(DTOAssembler::fromDish)
                        .collect(Collectors.toList()),
                category.getAdditions().stream()
                        .map(DTOAssembler::fromAddition)
                        .collect(Collectors.toList())
        );
    }

    public static DishDTO fromDish(Dish dish) {
        if (dish != null) {
            return new DishDTO(
                    dish.getUuid(),
                    dish.getCategory().getUuid(),
                    dish.getName(),
                    dish.getPrice(),
                    dish.getDescription(),
                    dish.getStatus());
        }
        return null;
    }

    public static AdditionDTO fromAddition(Addition addition) {
        return new AdditionDTO(
                addition.getUuid(),
                addition.getName(),
                addition.getPrice(),
                addition.isGeneric());
    }

    public static OrdinationDTO fromOrdination(Ordination o) {
        return new OrdinationDTO(
                o.getCreationTime(),
                o.getProgressive(),
                o.getOrders().stream()
                        .map(DTOAssembler::fromOrder)
                        .collect(Collectors.toList()),
                o.isDirty(),
                o.getUuid());
    }

    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(
                order.getDish().getUuid(),
                order.getAdditions().stream()
                        .map(a -> a.getUuid())
                        .collect(Collectors.toList()),
                order.getPrice(),
                order.getNotes(),
                uuid(order.getPhase()),
                uuid(order.getBill()),
                order.getUuid());
    }

    public static DiningTableDTO fromDiningTable(DiningTable diningTable) {
        return new DiningTableDTO(
                diningTable.getCoverCharges(),
                uuid(diningTable.getWaiter()),
                diningTable.getOrdinations().stream()
                        .map(DTOAssembler::fromOrdination)
                        .collect(Collectors.toList()),
                diningTable.getBills().stream()
                        .map(DTOAssembler::fromBill)
                        .collect(Collectors.toList()),
                diningTable.getDate(),
                diningTable.getTable().getUuid(),
                diningTable.isClosed(),
                diningTable.getUuid());
    }

    public static BillDTO fromBill(Bill bill) {
        return new BillDTO(
                bill.getUuid(),
                uuid(bill.getTable()),
                bill.getOrders().stream()
                        .map(order -> uuid(order))
                        .collect(Collectors.toList()));
    }

    private static String uuid(BaseEntity entity) {
        return entity != null ? entity.getUuid() : null;
    }
}
