package com.naigoapps.restaurant.services;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.dao.PrinterDao;
import com.naigoapps.restaurant.model.dao.StatisticsDao;
import com.naigoapps.restaurant.services.dto.StatisticsDTO;
import com.naigoapps.restaurant.services.dto.StatisticsEntryDTO;
import com.naigoapps.restaurant.services.printing.services.PrintingService;
import com.naigoapps.restaurant.services.printing.services.PrintingServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.PrintException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsDao dao;

    @Autowired
    private PrinterDao pDao;

    public void printStatistics(LocalDate from, LocalDate to) throws IOException, PrintException {
        Printer mainPrinter = pDao.findMainPrinter();
        PrintingService ps = PrintingServiceProvider.get(mainPrinter);

        ps.size(PrintingService.Size.STANDARD)
                .lf(3);
        ps.printCenter("Riepilogo")
                .printLeft("Da: " + from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .printLeft("A:  " + to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .lf();

        ps.printCenter("Categorie più vendute");
        StatisticsDTO mostSoldCategories = dao.getMostSoldCategories(from, to, 0);
        for (StatisticsEntryDTO entry : mostSoldCategories.getEntries()) {
            ps.printLine(entry.getName(), "" + entry.getValue());
        }

        ps.lf();

        ps.printCenter("Categorie più redditizie");
        StatisticsDTO mostProfitableCategories = dao.getMostProfitableCategories(from, to, 0);
        for (StatisticsEntryDTO entry : mostProfitableCategories.getEntries()) {
            ps.printLine(entry.getName(), PrintingService.formatPrice(entry.getValue()));
        }

        ps.lf();

        ps.printLine("Incasso totale:", PrintingService.formatPrice(dao.getProfit(from, to)));
        ps.doPrint();
    }

    public StatisticsDTO getMostSoldDishes(LocalDate from, LocalDate to) {
        return dao.getMostSoldDishes(from, to, 5);
    }

    public StatisticsDTO getMostProfitableDishes(LocalDate from, LocalDate to) {
        return dao.getMostProfitableDishes(from, to, 5);
    }

    public StatisticsDTO getMostSoldCategories(LocalDate from, LocalDate to) {
        return dao.getMostSoldCategories(from, to, 0);
    }

    public StatisticsDTO getMostProfitableCategories(LocalDate from, LocalDate to) {
        return dao.getMostProfitableCategories(from, to, 0);
    }
}
