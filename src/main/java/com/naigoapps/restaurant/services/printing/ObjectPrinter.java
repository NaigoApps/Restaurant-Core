/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing;

import com.naigoapps.restaurant.services.PrinterService;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author naigo
 * @param <T>
 */
@FunctionalInterface
public interface ObjectPrinter<T>{
    public PrinterService apply(PrinterService ps, T obj, LocalDateTime time) throws IOException;
}
