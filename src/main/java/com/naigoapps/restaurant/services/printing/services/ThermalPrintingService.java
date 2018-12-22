/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing.services;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.services.printing.ObjectPrinter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
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
public class ThermalPrintingService implements PrintingService {

    public static final DocFlavor.BYTE_ARRAY FLAVOR = DocFlavor.BYTE_ARRAY.AUTOSENSE;
    public static final AttributeSet ATTRIBUTES = new HashPrintRequestAttributeSet();

    private final int ESC = 0x1B;
    private final int GS = 0x1D;

    private final PrintService service;

    private Printer printer;

    private final ByteArrayOutputStream text;

    public ThermalPrintingService(Printer printer) {
        this.printer = printer;
        service = Arrays
                .stream(PrintServiceLookup.lookupPrintServices(FLAVOR, ATTRIBUTES))
                .filter(s -> s.getName().equals(printer.getName()))
                .findFirst()
                .orElse(null);
        text = new ByteArrayOutputStream();
    }

    @Override
    public Printer getPrinter() {
        return printer;
    }

    @Override
    public ThermalPrintingService reset() {
        text.reset();
        return this;
    }

    @Override
    public ThermalPrintingService printLeft(String line) throws IOException {
        return align(Align.LEFT).printString(line).lineFeed();
    }

    @Override
    public ThermalPrintingService printCenter(String line) throws IOException {
        return align(Align.CENTER).printString(line).lineFeed();
    }

    @Override
    public ThermalPrintingService printRight(String line) throws IOException {
        return align(Align.RIGHT).printString(line).lineFeed();
    }

    @Override
    public ThermalPrintingService printLine(String left, String right) throws IOException {
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

    @Override
    public String getText() {
        return this.text.toString();
    }

    @Override
    public ThermalPrintingService print(int s) throws IOException {
        return this.printString(String.valueOf(s));
    }

    @Override
    public ThermalPrintingService lf() throws IOException {
        return lineFeed();
    }

    @Override
    public ThermalPrintingService lf(int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            lineFeed();
        }
        return this;
    }

    @Override
    public ThermalPrintingService separator(char c) throws IOException {
        int lines = printer.getLineCharacters();
        align(Align.LEFT);
        for (int i = 0; i < lines; i++) {
            text.write(c);
        }
        return lineFeed();
    }

    @Override
    public ThermalPrintingService cut() throws IOException {
        lineFeed();
        align(Align.LEFT);
        text.write(GS);
        text.write('V');
        text.write(0x01);
        return this;
    }

    @Override
    public void doPrint() throws PrintException {
        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(text.toByteArray(), FLAVOR, null);
        job.print(doc, null);
    }

    @Override
    public ThermalPrintingService underline(Underline u) throws IOException {
        text.write(ESC);
        text.write('-');
        text.write(u.value);
        return this;
    }

    @Override
    public <T> PrintingService accept(ObjectPrinter printer, T obj, LocalDateTime time) throws IOException {
        return printer.apply(this, obj, time);
    }

    @Override
    public ThermalPrintingService emph(Emph e) throws IOException {
        text.write(ESC);
        text.write('E');
        text.write(e.value);
        return this;
    }

    @Override
    public ThermalPrintingService font(Font f) throws IOException {
        text.write(ESC);
        text.write('M');
        text.write(f.value);
        return this;
    }

    @Override
    public ThermalPrintingService size(Size s) throws IOException {
        text.write(GS);
        text.write('!');
        text.write(s.value);
        return this;
    }

    @Override
    public ThermalPrintingService align(Align a) throws IOException {
        text.write(ESC);
        text.write('a');
        text.write(a.value);
        return this;
    }

    private ThermalPrintingService printString(String s) throws IOException {
        text.write(s.getBytes("CP437"));
        return this;
    }

    private ThermalPrintingService lineFeed() throws IOException {
        text.write(0x0A);
        return this;
    }

    private ThermalPrintingService space(int spaces) throws IOException {
        for (int i = 0; i < spaces; i++) {
            this.printString(" ");
        }
        return this;
    }

}
