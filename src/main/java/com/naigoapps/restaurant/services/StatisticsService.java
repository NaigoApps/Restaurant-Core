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
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsDao dao;

    @Autowired
    private PrinterDao pDao;

    public void printCategoriesStatistics(LocalDate from, LocalDate to, boolean dishes) throws IOException, PrintException {
        Printer mainPrinter = pDao.findMainPrinter();
        PrintingService ps = PrintingServiceProvider.get(mainPrinter);

        ps.size(PrintingService.Size.STANDARD)
                .lf(3);
        ps.printCenter("Riepilogo")
                .printLeft("Da: " + from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .printLeft("A:  " + to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .lf();

        Map.Entry<Long, Double> coverCharges = dao.getCoverCharges(from, to);
        ps.printLine(coverCharges.getKey() + " coperti", String.valueOf(coverCharges.getValue()));

        ps.printCenter(dishes ? "Piatti piu' venduti" : "Categorie");
        StatisticsDTO categories = dao.getMostSoldCategories(from, to, 0);
        for (StatisticsEntryDTO entry : categories.getEntries()) {
            ps.printLine(String.valueOf(entry.getCount()), entry.getName());
            ps.printRight(PrintingService.formatPrice(entry.getValue()));
            if (dishes && !entry.getChildren().isEmpty()) {
                StatisticsEntryDTO dish = entry.getChildren().iterator().next();
                ps.printLine(String.valueOf(dish.getCount()), dish.getName());
                ps.printRight(PrintingService.formatPrice(dish.getValue()));
            }
            ps.lf();
        }

        ps.lf();

        ps.printLine("Incasso totale:", PrintingService.formatPrice(dao.getProfit(from, to) + coverCharges.getValue()))
                .lf(3);

        ps.cut();

        ps.doPrint();
    }

    public StatisticsDTO getMostSoldDishes(LocalDate from, LocalDate to) {
        return dao.getMostSoldDishes(from, to, 10);
    }

    public StatisticsDTO getMostSoldCategories(LocalDate from, LocalDate to) {
        return dao.getMostSoldCategories(from, to, 10);
    }

}
