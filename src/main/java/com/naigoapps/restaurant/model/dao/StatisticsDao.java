package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.services.dto.StatisticsDTO;
import com.naigoapps.restaurant.services.dto.StatisticsEntryDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class StatisticsDao {

    @PersistenceContext(name = "restaurant-pu")
    private EntityManager em;

    public StatisticsDTO getMostSoldDishes(LocalDate from, LocalDate to, int limit) {
        Query query = em.createQuery("SELECT " +
                        "d.uuid as uuid, d.name as name, count(d.id), sum(o.price) as value " +
                        "FROM Dish d, Order o, Ordination ot, DiningTable dt, Evening ev " +
                        "WHERE DATE(ev.day) >= ?1 AND DATE(ev.day) <= ?2 AND " +
                        "o.dish = d AND o.ordination = ot AND ot.table = dt AND dt.evening = ev " +
                        "GROUP BY d.id, d.uuid " +
                        "ORDER BY value DESC")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return getStatisticsDTO(from, to, limit, query, v -> (Long) v, v -> (Double) v);
    }

    public Map.Entry<Long, Double> getCoverCharges(LocalDate from, LocalDate to) {
        Query query = em.createQuery("SELECT " +
                        "sum(dt.coverCharges) as n, sum(dt.coverCharges) * e.coverCharge as p " +
                        "FROM DiningTable dt, Evening e " +
                        "WHERE DATE(e.day) >= ?1 AND DATE(e.day) <= ?2 AND " +
                        "dt.evening = e " +
                        "GROUP BY e")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        Long n = 0L;
        Double p = 0D;
        for (Object o : query.getResultList()) {
            Object[] array = (Object[]) o;
            n += (Long) array[0];
            p += (Float) array[1];
        }
        return new AbstractMap.SimpleEntry<>(n, p);
    }

    public StatisticsDTO getMostSoldCategories(LocalDate from, LocalDate to, int limit) {
        Query query = em.createQuery("SELECT " +
                        "c.uuid as uuid, c.name as name, count(c.id), sum(o.price) as value " +
                        "FROM Category c, Dish d, Order o, Ordination ot, DiningTable dt, Evening ev " +
                        "WHERE DATE(ev.day) >= ?1 AND DATE(ev.day) <= ?2 AND " +
                        "d.category = c AND o.dish = d AND o.ordination = ot AND ot.table = dt AND dt.evening = ev " +
                        "GROUP BY c.id, c.uuid " +
                        "ORDER BY value DESC")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        StatisticsDTO categoriesDTO = getStatisticsDTO(from, to, limit, query, Long.class::cast, Double.class::cast);

        for (StatisticsEntryDTO entry : categoriesDTO.getEntries()) {
            List<StatisticsEntryDTO> mostSoldDishes = getMostSoldDishes(from, to, entry.getUuid(), 1);
            entry.setChildren(mostSoldDishes);
        }

        return categoriesDTO;
    }

    public List<StatisticsEntryDTO> getMostSoldDishes(LocalDate from, LocalDate to, String category, int limit) {
        Query query = em.createQuery("SELECT " +
                        "d.uuid, d.name as name, count(d.id) as n, sum(o.price) as value " +
                        "FROM Dish d, Order o, Ordination ot, DiningTable dt, Evening ev " +
                        "WHERE DATE(ev.day) >= ?1 AND DATE(ev.day) <= ?2 AND " +
                        "d.category.uuid = ?3 AND o.dish = d AND o.ordination = ot AND ot.table = dt AND dt.evening = ev " +
                        "GROUP BY d.id " +
                        "ORDER BY value DESC")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to))
                .setParameter(3, category);
        return getEntryDTOs(limit, query, Long.class::cast, Double.class::cast);
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

    private List<StatisticsEntryDTO> getEntryDTOs(int limit, Query query, Function<Object, Long> longSupplier, Function<Object, Double> doubleSupplier) {
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        List<Object> resultList = (List<Object>) query.getResultList();
        return parseEntries(resultList, longSupplier, doubleSupplier);
    }

    private List<StatisticsEntryDTO> parseEntries(List<Object> resultList, Function<Object, Long> longSupplier, Function<Object, Double> doubleSupplier) {
        return resultList.stream().map(obj -> {
            Object[] row = (Object[]) obj;
            StatisticsEntryDTO dto = new StatisticsEntryDTO();
            dto.setUuid((String) row[0]);
            dto.setName((String) row[1]);
            dto.setCount(longSupplier.apply(row[2]));
            dto.setValue(doubleSupplier.apply(row[3]));
            return dto;
        }).collect(Collectors.toList());
    }

    public Double getProfit(LocalDate from, LocalDate to) {
        Query query = em.createQuery("SELECT " +
                        "sum(o.price) as value " +
                        "FROM Order o, Ordination ot, DiningTable dt, Evening ev " +
                        "WHERE DATE(ev.day) >= ?1 AND DATE(ev.day) <= ?2 AND " +
                        "o.ordination = ot AND ot.table = dt AND dt.evening = ev")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return (Double) query.getSingleResult();
    }

    public Double getFinalProfit(LocalDate from, LocalDate to) {
        Query query = em.createQuery("SELECT " +
                        "sum(b.total) as value " +
                        "FROM DiningTable dt, Evening ev, Bill b " +
                        "WHERE DATE(ev.day) >= ?1 AND DATE(ev.day) <= ?2 AND " +
                        "dt.evening = ev AND b.table = dt")
                .setParameter(1, Date.valueOf(from))
                .setParameter(2, Date.valueOf(to));
        return (Double) query.getSingleResult();
    }
}
