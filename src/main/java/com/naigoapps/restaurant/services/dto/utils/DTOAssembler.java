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
import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.Order;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.RestaurantTable;
import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.services.dto.AdditionDTO;
import com.naigoapps.restaurant.services.dto.BillDTO;
import com.naigoapps.restaurant.services.dto.CategoryDTO;
import com.naigoapps.restaurant.services.dto.CustomerDTO;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.DishDTO;
import com.naigoapps.restaurant.services.dto.EveningDTO;
import com.naigoapps.restaurant.services.dto.LocationDTO;
import com.naigoapps.restaurant.services.dto.OrdinationDTO;
import com.naigoapps.restaurant.services.dto.OrderDTO;
import com.naigoapps.restaurant.services.dto.PhaseDTO;
import com.naigoapps.restaurant.services.dto.PrinterDTO;
import com.naigoapps.restaurant.services.dto.RestaurantTableDTO;
import com.naigoapps.restaurant.services.dto.SettingsDTO;
import com.naigoapps.restaurant.services.dto.WaiterDTO;
import java.util.List;
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
                uuids(evening.getDiningTables()));
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
            return new PhaseDTO(phase.getUuid(), phase.getName(), phase.getPriority());
        }
        return null;
    }

    public static PrinterDTO fromPrinter(Printer printer) {
        return new PrinterDTO(printer.getUuid(), printer.getName(), printer.getLineCharacters());
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
        CategoryDTO result = new CategoryDTO();
        result.setUuid(category.getUuid());
        result.setName(category.getName());
        result.setLocation(uuid(category.getLocation()));
        if (category.getColor() != null) {
            result.setColor(category.getColor().getRGB() & 0x00FFFFFF);
        }
        result.setDishes(uuids(category.getDishes()));
        result.setAdditions(uuids(category.getAdditions()));
        return result;
    }

    public static DishDTO fromDish(Dish dish) {
        if (dish != null) {
            return new DishDTO(
                    dish.getUuid(),
                    uuid(dish.getCategory()),
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
                uuid(o.getTable()),
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
                uuid(order.getOrdination()),
                order.getDish().getUuid(),
                uuids(order.getAdditions()),
                order.getPrice(),
                order.getNotes(),
                uuid(order.getPhase()),
                uuid(order.getBill()),
                order.getUuid());
    }

    public static DiningTableDTO fromDiningTable(DiningTable diningTable) {
        return new DiningTableDTO(
                uuid(diningTable.getEvening()),
                diningTable.getCoverCharges(),
                uuid(diningTable.getWaiter()),
                uuids(diningTable.getOrdinations()),
                uuids(diningTable.getBills()),
                diningTable.getDate(),
                diningTable.getTable().getUuid(),
                diningTable.getStatus(),
                diningTable.getUuid());
    }

    public static BillDTO fromBill(Bill bill) {
        BillDTO result = new BillDTO();
        result.setProgressive(bill.getProgressive());
        result.setCustomer(uuid(bill.getCustomer()));
        result.setPrintTime(bill.getPrintTime());
        result.setTable(uuid(bill.getTable()));
        result.setOrders(uuids(bill.getOrders()));
        result.setCoverCharges(bill.getCoverCharges());
        result.setTotal(bill.getTotal());
        result.setUuid(bill.getUuid());
        return result;
    }

    public static CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO result = new CustomerDTO();
        result.setUuid(customer.getUuid());
        result.setName(customer.getName());
        result.setSurname(customer.getSurname());
        result.setPiva(customer.getPiva());
        result.setCf(customer.getCf());
        result.setAddress(customer.getAddress());
        result.setCity(customer.getCity());
        result.setCap(customer.getCap());
        result.setDistrict(customer.getDistrict());
        return result;
    }

    public static SettingsDTO fromSettings(Settings settings) {
        return new SettingsDTO(
                settings.getUuid(),
                settings.getDefaultCoverCharge(),
                uuid(settings.getMainPrinter()),
                uuid(settings.getFiscalPrinter()),
                settings.getUseCoverCharges(),
                settings.getShrinkOrdinations(),
                settings.getClientSettings());
    }

    private static String uuid(BaseEntity entity) {
        return entity != null ? entity.getUuid() : null;
    }
    
    private static List<String> uuids(List<? extends BaseEntity> entities){
        return entities.stream()
                .map(entity -> entity.getUuid())
                .collect(Collectors.toList());
    }
}
