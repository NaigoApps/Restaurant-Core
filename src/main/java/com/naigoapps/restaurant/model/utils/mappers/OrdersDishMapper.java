/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.utils.mappers;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Order;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author naigo
 */
public class OrdersDishMapper implements Collector<Order, Map<Dish, List<Order>>, Map<Dish, List<Order>>> {

    public static OrdersDishMapper toDishMap() {
        return new OrdersDishMapper();
    }

    @Override
    public Supplier<Map<Dish, List<Order>>> supplier() {
        return HashMap<Dish, List<Order>>::new;
    }

    @Override
    public BiConsumer<Map<Dish, List<Order>>, Order> accumulator() {
        return new OrderAdder();
    }

    @Override
    public BinaryOperator<Map<Dish, List<Order>>> combiner() {
        return (Map<Dish, List<Order>> map1, Map<Dish, List<Order>> map2) -> {
            OrderAdder adder = new OrderAdder();
            map2.values().forEach(list -> {
                list.forEach(order -> adder.accept(map1, order));
            });
            return map1;
        };
    }

    @Override
    public Function<Map<Dish, List<Order>>, Map<Dish, List<Order>>> finisher() {
        return t -> t;
    }

    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> set = new HashSet<>();
        set.add(Characteristics.IDENTITY_FINISH);
        set.add(Characteristics.UNORDERED);
        return set;
    }

    private static class OrderAdder implements BiConsumer<Map<Dish, List<Order>>, Order> {
        @Override
        public void accept(Map<Dish, List<Order>> map, Order o) {
            if (o.getDish() == null) {
                return;
            }
            List<Order> orders = map.computeIfAbsent(o.getDish(), k -> new ArrayList<>());
            orders.add(o);
        }
    }

}
