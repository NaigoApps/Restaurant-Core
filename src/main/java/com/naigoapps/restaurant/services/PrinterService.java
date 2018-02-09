/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.services.printing.ObjectPrinter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;

/**
 *
 * @author naigo
 */
public class PrinterService {

    public static final DocFlavor.BYTE_ARRAY FLAVOR = DocFlavor.BYTE_ARRAY.AUTOSENSE;
    public static final AttributeSet ATTRIBUTES = new HashPrintRequestAttributeSet();

    private final int ESC = 0x1B;
    private final int GS = 0x1D;

    private final PrintService service;

    private Printer printer;

    private final ByteArrayOutputStream text;

    private boolean debugMode = true;

    public PrinterService(Printer printer) {
        this.printer = printer;
        service = Arrays
                .stream(PrintServiceLookup.lookupPrintServices(FLAVOR, ATTRIBUTES))
                .filter(s -> s.getName().equals(printer.getName()))
                .findFirst()
                .orElse(null);
        text = new ByteArrayOutputStream();
    }

    public Printer getPrinter() {
        return printer;
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public PrinterService reset() {
        text.reset();
        return this;
    }

    public PrinterService printLeft(String line) throws IOException {
        return align(Align.LEFT).printString(line).lineFeed();
    }

    public PrinterService printCenter(String line) throws IOException {
        return align(Align.CENTER).printString(line).lineFeed();
    }

    public PrinterService printRight(String line) throws IOException {
        return align(Align.RIGHT).printString(line).lineFeed();
    }

    public PrinterService printLine(String left, String right) throws IOException {
        int lineChars = printer.getLineCharacters();
        if (left.length() + right.length() > lineChars) {
            align(Align.LEFT).printString(left).lineFeed();
            align(Align.RIGHT).printString(right).lineFeed();
        } else {
            int spaces = lineChars - left.length() - right.length();
            align(Align.LEFT).printString(left).space(spaces).printString(right).lineFeed();
        }
        return this;
    }

    public PrinterService print(int s) throws IOException {
        return this.printString(String.valueOf(s));
    }

    public PrinterService lf() throws IOException {
        return lineFeed();
    }

    public PrinterService lf(int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            lineFeed();
        }
        return this;
    }
    
    public PrinterService separator(char c) throws IOException{
        int lines = printer.getLineCharacters();
        align(Align.LEFT);
        for (int i = 0; i < lines; i++) {
            text.write(c);
        }
        return lineFeed();
    }

    public PrinterService cut() throws IOException {
        lineFeed();
        align(Align.LEFT);
        if (!debugMode) {
            text.write(GS);
            text.write('V');
            text.write(0x01);
        }
        return this;
    }

    public void doPrint() throws PrintException {
        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(text.toByteArray(), FLAVOR, null);
        job.print(doc, null);
    }

    public PrinterService underline(Underline u) throws IOException {
        if (!debugMode) {
            text.write(ESC);
            text.write('-');
            text.write(u.value);
        }
        return this;
    }

    public <T> PrinterService accept(ObjectPrinter printer, T obj) throws IOException {
        return printer.apply(this, obj);
    }

    public enum Underline {
        NONE(0), THIN(1), THICK(2);

        private final int value;

        Underline(int val) {
            this.value = val;
        }
    }

    public PrinterService emph(Emph e) throws IOException {
        if (!debugMode) {
            text.write(ESC);
            text.write('E');
            text.write(e.value);
        }
        return this;
    }

    public enum Emph {
        NO(0), YES(1);

        private final int value;

        Emph(int val) {
            this.value = val;
        }
    }

    public PrinterService font(Font f) throws IOException {
        if (!debugMode) {
            text.write(ESC);
            text.write('M');
            text.write(f.value);
        }
        return this;
    }

    public enum Font {
        A(0), B(1);

        private final int value;

        Font(int val) {
            this.value = val;
        }
    }

    public enum Align {
        LEFT(0), CENTER(1), RIGHT(2);

        private final int value;

        Align(int val) {
            this.value = val;
        }
    }

    public PrinterService size(Size s) throws IOException {
        if (!debugMode) {
            text.write(GS);
            text.write('!');
            text.write(s.value);
        }
        return this;
    }

    public enum Size {
        SMALL(0x00), NORMAL(0x11), STANDARD(0x01), BIG(0x22), BIGGER(0x33), HUGE(0x44);

        private final int value;

        Size(int val) {
            this.value = val;
        }
    }

    public static String formatPrice(float f) {
        return String.format("%.2f", f);
    }

    private PrinterService align(Align a) throws IOException {
        if (!debugMode) {
            text.write(ESC);
            text.write('a');
            text.write(a.value);
        }
        return this;
    }

    private PrinterService printString(String s) throws IOException {
        text.write(s.getBytes("CP437"));
        return this;
    }

    private PrinterService lineFeed() throws IOException {
        text.write(0x0A);
        return this;
    }

    private PrinterService space(int spaces) throws IOException {
        for (int i = 0; i < spaces; i++) {
            this.printString(" ");
        }
        return this;
    }

}
