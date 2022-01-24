package com.naigoapps.restaurant.services.dto;

import java.util.List;

public class StatisticsEntryDTO {

    private String uuid;
    private String name;
    private Long count;
    private Double value;
    private List<StatisticsEntryDTO> children;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setChildren(List<StatisticsEntryDTO> children) {
        this.children = children;
    }

    public List<StatisticsEntryDTO> getChildren() {
        return children;
    }
}
