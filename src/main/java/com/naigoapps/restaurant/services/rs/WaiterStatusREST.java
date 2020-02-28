/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.rs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naigoapps.restaurant.model.WaiterStatus;

/**
 *
 * @author naigo
 */
@RequestMapping("/rest/waiter-statuses")
@RestController
public class WaiterStatusREST {

    @GetMapping
    public WaiterStatus[] findAll() {
        return WaiterStatus.values();
    }

}
