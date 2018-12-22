/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing.services;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.services.printing.ObjectPrinter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author naigo
 */
public class PDFPrintingService implements PrintingService {

    private static final int X_MARGIN = 30;
    private static final int Y_MARGIN = 30;
    
    private final PrintService service;

    private Printer printer;

    private PDDocument document;
    private PDPageContentStream stream;

    private PDFont currentFont;
    private float currentSize;
    private float currentLine;

    public PDFPrintingService(Printer printer) throws IOException {
        this.printer = printer;
        service = Arrays
                .stream(PrintServiceLookup.lookupPrintServices(FLAVOR, ATTRIBUTES))
                .filter(s -> s.getName().equals(printer.getName()))
                .findFirst()
                .orElse(null);
        resetImpl();
    }

    @Override
    public Printer getPrinter() {
        return printer;
    }

    @Override
    public PDFPrintingService reset() throws IOException {
        resetImpl();
        return this;
    }

    private void resetImpl() throws IOException {
        if(stream != null){
            stream.close();
        }
        if (document != null) {
            document.close();
        }
        document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        currentLine = page.getBBox().getHeight() - Y_MARGIN;
        this.stream = new PDPageContentStream(document, page);
        font(Font.A);
        size(Size.STANDARD);
        this.stream.beginText();
        this.stream.setFont(currentFont, currentSize);
        this.stream.newLineAtOffset(X_MARGIN, currentLine);
    }

    @Override
    public PDFPrintingService printLeft(String line) throws IOException {
        return align(Align.LEFT).printString(line).lineFeed();
    }

    @Override
    public PDFPrintingService printCenter(String line) throws IOException {
        return align(Align.CENTER).printString(line).lineFeed();
    }

    @Override
    public PDFPrintingService printRight(String line) throws IOException {
        return align(Align.RIGHT).printString(line).lineFeed();
    }

    @Override
    public PDFPrintingService printLine(String left, String right) throws IOException {
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
        return this.document.toString();
    }

    @Override
    public PDFPrintingService print(int s) throws IOException {
        return this.printString(String.valueOf(s));
    }

    @Override
    public PDFPrintingService lf() throws IOException {
        return lineFeed();
    }

    @Override
    public PDFPrintingService lf(int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            lineFeed();
        }
        return this;
    }

    @Override
    public PDFPrintingService separator(char c) throws IOException {
        int lines = printer.getLineCharacters();
        align(Align.LEFT);
        lineFeed();
        for (int i = 0; i < lines; i++) {
            stream.showText(Character.toString(c));
        }
        return lineFeed();
    }

    @Override
    public PDFPrintingService cut() throws IOException {
        separator('#');
        align(Align.LEFT);
        return this;
    }

    @Override
    public void doPrint() throws PrintException, IOException {
        stream.endText();
        stream.close();
        document.save(printer.getName() + ".pdf");
        document.close();
    }

    @Override
    public <T> PrintingService accept(ObjectPrinter printer, T obj, LocalDateTime time) throws IOException {
        return printer.apply(this, obj, time);
    }

    @Override
    public PDFPrintingService underline(Underline u) throws IOException {
        return this;
    }

    @Override
    public PDFPrintingService emph(Emph e) throws IOException {
        return this;
    }

    @Override
    public PDFPrintingService font(Font f) throws IOException {
        if (f == Font.A) {
            this.currentFont = PDType1Font.COURIER;
        } else {
            this.currentFont = PDType1Font.HELVETICA;
        }
        return this;
    }

    @Override
    public PDFPrintingService size(Size s) throws IOException {
        switch (s) {
            case SMALL:
                this.currentSize = 6;
                break;
            case STANDARD:
                this.currentSize = 12;
                break;
            case NORMAL:
                this.currentSize = 16;
                break;
            case BIG:
                this.currentSize = 20;
                break;
            case BIGGER:
                this.currentSize = 26;
                break;
            case HUGE:
                this.currentSize = 32;
                break;
        }
        stream.setFont(currentFont, currentSize);
        return this;
    }

    @Override
    public PDFPrintingService align(Align a) throws IOException {
        return this;
    }

    public static String formatPrice(float f) {
        return String.format("%.2f", f);
    }

    private PDFPrintingService printString(String s) throws IOException {
        stream.showText(s);
        return this;
    }

    private PDFPrintingService lineFeed() throws IOException {
        float line = currentFont.getBoundingBox().getHeight() / 1000 * currentSize;
        stream.newLineAtOffset(0, -line);
        return this;
    }

    private PDFPrintingService space(int spaces) throws IOException {
        for (int i = 0; i < spaces; i++) {
            this.printString(" ");
        }
        return this;
    }

}
