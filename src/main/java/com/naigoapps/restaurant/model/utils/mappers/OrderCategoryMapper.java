/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.utils.mappers;

import com.naigoapps.restaurant.model.Category;
import com.naigoapps.restaurant.model.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author naigo
 */
public class OrderCategoryMapper implements Collector<Order, Map<Category, List<Order>>, Map<Category, List<Order>>>{

    public static OrderCategoryMapper toCategoryMap(){
        return new OrderCategoryMapper();
    }
    
    @Override
    public Supplier<Map<Category, List<Order>>> supplier() {
        return HashMap<Category, List<Order>>::new;
    }

    @Override
    public BiConsumer<Map<Category, List<Order>>, Order> accumulator() {
        return new OrderAdder();
    }

    @Override
    public BinaryOperator<Map<Category, List<Order>>> combiner() {
        return (Map<Category, List<Order>> map1, Map<Category, List<Order>> map2) -> {
            OrderAdder adder = new OrderAdder();
            map2.values().forEach(list -> {
                list.forEach(order -> adder.accept(map1, order));
            });
            return map1;
        };
    }

    @Override
    public Function<Map<Category, List<Order>>, Map<Category, List<Order>>> finisher() {
        return t -> t;
    }

    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> set = new HashSet<>();
        set.add(Characteristics.IDENTITY_FINISH);
        set.add(Characteristics.UNORDERED);
        return set;
    }

    private class OrderAdder implements BiConsumer<Map<Category, List<Order>>, Order>{
        @Override
        public void accept(Map<Category, List<Order>> map, Order o) {
            List<Order> orders = map.get(o.getDish().getCategory());
            if(orders == null){
                orders = new ArrayList<>();
                map.put(o.getDish().getCategory(), orders);
            }
            orders.add(o);
        }
    }
    
}
