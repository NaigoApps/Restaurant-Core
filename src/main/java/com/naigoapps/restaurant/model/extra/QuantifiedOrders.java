/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.extra;

import com.naigoapps.restaurant.model.Order;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author naigo
 */
public class QuantifiedOrders {

    private Map<Order, Integer> map;

    public QuantifiedOrders() {
        map = new HashMap<>();
    }
    
    public void addOrder(Order o){
        Order target = null;
        for(Order order : map.keySet()){
            if(o.isTheSame(order)){
                target = order;
            }
        }
        if(target != null){
            map.put(target, map.get(target) + 1);
        }else{
            map.put(o, 1);
        }
    }
    
    public Map<Order, Integer> getOrders(){
        return map;
    }
}
