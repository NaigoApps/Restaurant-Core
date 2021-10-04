/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;

import com.naigoapps.restaurant.model.dao.GenericDao;

/**
 *
 * @author naigo
 */
@SpringBootApplication
@PropertySource("classpath:restaurant.properties")
public class Main {

	@Value("${browser}")
	private String browser;

    @Autowired
    @Qualifier("generic")
    private GenericDao dao;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            Process p = Runtime.getRuntime().exec(browser + " http://localhost:8080");
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
}
