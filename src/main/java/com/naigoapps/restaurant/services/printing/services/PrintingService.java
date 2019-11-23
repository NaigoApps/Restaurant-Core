/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.services.printing.ObjectPrinter;

/**
 *
 * @author naigo
 */
public interface PrintingService {

    public static final DocFlavor.BYTE_ARRAY FLAVOR = DocFlavor.BYTE_ARRAY.AUTOSENSE;
    public static final AttributeSet ATTRIBUTES = new HashPrintRequestAttributeSet();

    public Printer getPrinter();

    public PrintingService reset() throws IOException;

    public PrintingService printLeft(String line) throws IOException;

    public PrintingService printCenter(String line) throws IOException;

    public PrintingService printRight(String line) throws IOException;

    public PrintingService printLine(String left, String right) throws IOException;

    public PrintingService print(int s) throws IOException;

    public String getText();

    public PrintingService lf() throws IOException;

    public PrintingService lf(int lines) throws IOException;

    public PrintingService separator(char c) throws IOException;

    public PrintingService cut() throws IOException;

    public PrintingService font(Font f) throws IOException;

    public PrintingService size(Size s) throws IOException;

    public PrintingService underline(Underline u) throws IOException;

    public PrintingService emph(Emph e) throws IOException;

    public PrintingService align(Align a) throws IOException;

    public static String formatPrice(double f) {
        return String.format("%.2f", f);
    }
    
	public static String formatTime(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return date.format(formatter);
	}


    public <T> PrintingService accept(ObjectPrinter<T> printer, T obj, LocalDateTime time) throws IOException;

    public void doPrint() throws PrintException, IOException;

    public enum Emph {
        NO(0), YES(1);

        public final int value;

        Emph(int val) {
            this.value = val;
        }
    }

    public enum Underline {
        NONE(0), THIN(1), THICK(2);

        public final int value;

        Underline(int val) {
            this.value = val;
        }
    }

    public enum Font {
        A(0), B(1);

        public final int value;

        Font(int val) {
            this.value = val;
        }
    }

    public enum Size {
        SMALL(0x00), NORMAL(0x11), STANDARD(0x01), BIG(0x22), BIGGER(0x33), HUGE(0x44);

        public final int value;

        Size(int val) {
            this.value = val;
        }
    }

    public enum Align {
        LEFT(0), CENTER(1), RIGHT(2);

        public final int value;

        Align(int val) {
            this.value = val;
        }
    }

}
