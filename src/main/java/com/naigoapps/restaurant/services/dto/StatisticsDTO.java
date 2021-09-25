package com.naigoapps.restaurant.services.dto;

import java.time.LocalDate;
import java.util.List;

public class StatisticsDTO {
    private LocalDate from;
    private LocalDate to;
    private List<StatisticsEntryDTO> entries;

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public void setEntries(List<StatisticsEntryDTO> entries) {
        this.entries = entries;
    }

    public List<StatisticsEntryDTO> getEntries() {
        return entries;
    }
}
