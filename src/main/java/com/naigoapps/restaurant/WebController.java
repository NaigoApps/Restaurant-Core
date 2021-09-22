/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author naigo
 */
@Controller
public class WebController implements ErrorController {

    @RequestMapping(value = "/restaurant")
    public String index() {
        return "index";
    }

    @RequestMapping("/error")
    public String pages() {
        return "forward:/";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
