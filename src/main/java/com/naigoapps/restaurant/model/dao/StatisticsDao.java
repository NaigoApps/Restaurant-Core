package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.services.dto.StatisticsDTO;
import com.naigoapps.restaurant.services.dto.StatisticsEntryDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class StatisticsDao {

    @PersistenceContext(name = "restaurant-pu")
    private EntityManager em;

    public StatisticsDTO getMostSoldDishes(LocalDate from, LocalDate to, int limit) {
        Query query = em.createQuery("SELECT " +
                        "d.name as name, count(d.id), sum(o.price) as value " +
                        "FROM Dish d, Order o, Ordination ot, DiningTable dt " +
                        "WHERE DATE(dt.openingTime) >= ?1 AND DATE(dt.openingTime) <= ?2 AND " +
                        "o.dish = d AND o.ordination = ot AND ot.table = dt " +
                        "GROUP BY d.id " +
                        "ORDER BY value DESC")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return getStatisticsDTO(from, to, limit, query, v -> (Long) v, v -> (Double) v);
    }

    public Long getCoverCharges(LocalDate from, LocalDate to) {
        Query query = em.createQuery("SELECT " +
                        "sum(dt.coverCharges) " +
                        "FROM DiningTable dt " +
                        "WHERE DATE(dt.openingTime) >= ?1 AND DATE(dt.openingTime) <= ?2")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return (Long) query.getSingleResult();
    }

    public StatisticsDTO getMostSoldCategories(LocalDate from, LocalDate to, int limit) {
        Query query = em.createQuery("SELECT " +
                        "c.name as name, count(c.id), sum(o.price) as value " +
                        "FROM Category c, Dish d, Order o, Ordination ot, DiningTable dt " +
                        "WHERE DATE(dt.openingTime) >= ?1 AND DATE(dt.openingTime) <= ?2 AND " +
                        "d.category = c AND o.dish = d AND o.ordination = ot AND ot.table = dt " +
                        "GROUP BY c.id " +
                        "ORDER BY value DESC")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return getStatisticsDTO(from, to, limit, query, v -> (Long) v, v -> (Double) v);
    }

    private StatisticsDTO getStatisticsDTO(LocalDate from, LocalDate to, int limit, Query query, Function<Object, Long> longSupplier, Function<Object, Double> doubleSupplier) {
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        List<Object> resultList = (List<Object>) query.getResultList();
        StatisticsDTO result = new StatisticsDTO();
        result.setFrom(from);
        result.setTo(to);
        result.setEntries(parseEntries(resultList, longSupplier, doubleSupplier));
        return result;
    }

    private List<StatisticsEntryDTO> parseEntries(List<Object> resultList, Function<Object, Long> longSupplier, Function<Object, Double> doubleSupplier) {
        return resultList.stream().map(obj -> {
            Object[] row = (Object[]) obj;
            StatisticsEntryDTO dto = new StatisticsEntryDTO();
            dto.setName((String) row[0]);
            dto.setCount(longSupplier.apply(row[1]));
            dto.setValue(doubleSupplier.apply(row[2]));
            return dto;
        }).collect(Collectors.toList());
    }

    public Double getProfit(LocalDate from, LocalDate to) {
        Query query = em.createQuery("SELECT " +
                        "sum(o.price) as value " +
                        "FROM Order o, Ordination ot, DiningTable dt " +
                        "WHERE DATE(dt.openingTime) >= ?1 AND DATE(dt.openingTime) <= ?2 AND " +
                        "o.ordination = ot AND ot.table = dt")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return (Double) query.getSingleResult();
    }
}
