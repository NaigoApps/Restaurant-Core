/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.printing.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.print.PrintException;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.services.printing.ObjectPrinter;

/**
 *
 * @author naigo
 */
public class StringsPrintingService implements PrintingService {

	private Printer printer;
	private List<String> result;

	public StringsPrintingService(Printer printer) {
		this.printer = printer;
		result = new ArrayList<>();
	}

	@Override
	public Printer getPrinter() {
		return printer;
	}

	@Override
	public StringsPrintingService reset() {
		result = new ArrayList<>();
		return this;
	}

	@Override
	public StringsPrintingService printLeft(String line) throws IOException {
		return printString(line).lineFeed();
	}

	@Override
	public StringsPrintingService printCenter(String line) throws IOException {
		int max = printer.getLineCharacters();
		int spaces = (max - line.length()) / 2;
		return space(spaces).printString(line).lineFeed();
	}

	@Override
	public StringsPrintingService printRight(String line) throws IOException {
		int max = printer.getLineCharacters();
		int spaces = max - line.length();
		return space(spaces).printString(line).lineFeed();
	}

	@Override
	public StringsPrintingService printLine(String left, String right) throws IOException {
		int lineChars = printer.getLineCharacters();
		if (left.length() + right.length() > lineChars) {
			return printString(left).lineFeed().printString(right).lineFeed();
		} else {
			int spaces = lineChars - left.length() - right.length();
			return printString(left).space(spaces).printString(right).lineFeed();
		}
	}

	@Override
	public String getText() {
		return this.result.stream().collect(Collectors.joining("\n"));
	}

	@Override
	public StringsPrintingService print(int s) throws IOException {
		return this.printString(String.valueOf(s));
	}

	@Override
	public StringsPrintingService lf() throws IOException {
		return lineFeed();
	}

	@Override
	public StringsPrintingService lf(int lines) throws IOException {
		for (int i = 0; i < lines; i++) {
			lineFeed();
		}
		return this;
	}

	@Override
	public StringsPrintingService separator(char c) throws IOException {
		int lines = printer.getLineCharacters();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lines; i++) {
			builder.append(c);
		}
		return printCenter(builder.toString());
	}

	@Override
	public StringsPrintingService cut() throws IOException {
		return lineFeed();
	}

	@Override
	public void doPrint() throws PrintException {
		// Nothing to do
	}

	public List<String> getLines() {
		return result;
	}

	@Override
	public StringsPrintingService underline(Underline u) throws IOException {
		return this;
	}

	@Override
	public <T> PrintingService accept(ObjectPrinter printer, T obj, LocalDateTime time) throws IOException {
		return printer.apply(this, obj, time);
	}

	@Override
	public StringsPrintingService emph(Emph e) throws IOException {
		return this;
	}

	@Override
	public StringsPrintingService font(Font f) throws IOException {
		return this;
	}

	@Override
	public StringsPrintingService size(Size s) throws IOException {
		return this;
	}

	@Override
	public StringsPrintingService align(Align a) throws IOException {
		return this;
	}

	private StringsPrintingService printString(String s) throws IOException {
		if (result.isEmpty()) {
			lineFeed();
		}
		String last = result.remove(result.size() - 1);
		result.add(last + s);
		return this;
	}

	private StringsPrintingService lineFeed() {
		result.add("");
		return this;
	}

	private StringsPrintingService space(int spaces) throws IOException {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < spaces; i++) {
			builder.append(' ');
		}
		return printString(builder.toString());
	}

}
