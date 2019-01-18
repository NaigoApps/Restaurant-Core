/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.dao.EveningDao;
import com.naigoapps.restaurant.services.DiningTableWS;
import com.naigoapps.restaurant.services.dto.DiningTableDTO;
import com.naigoapps.restaurant.services.dto.utils.DTOAssembler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

/**
 *
 * @author naigo
 */
@ApplicationScoped
public class EveningManager {

    private String selectedEveningUuid;

    private List<Session> dtSessions;

    @Inject
    EveningDao eveningDao;

    @PostConstruct
    public void init() {
        Logger.getLogger(this.getClass().getName()).info("***** CREATED_EVENING_MANAGER *****");
        dtSessions = new ArrayList<>();
    }

    public Evening getSelectedEvening() {
        if (selectedEveningUuid != null) {
            return eveningDao.findByUuid(selectedEveningUuid);
        } else {
            return null;
        }
    }

    public void setSelectedEvening(String selectedEveningUuid) {
        this.selectedEveningUuid = selectedEveningUuid;
    }

    public void addDtSession(Session s) {
        dtSessions.add(s);
    }

    public void removeDtSession(Session s) {
        dtSessions.remove(s);
    }

    public void updateDtSessions() {
        List<Session> toRemove = new ArrayList<>();

        List<DiningTableDTO> result = getSelectedEvening().getDiningTables()
                .stream()
                .map(DTOAssembler::fromDiningTable)
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        String tables = null;
        try {
            tables = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(EveningManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (tables != null) {
            final String message = tables;
            dtSessions.stream().forEach(session -> {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    Logger.getLogger(DiningTableWS.class.getName()).log(Level.SEVERE, null, ex);
                    toRemove.add(session);
                }
            });
            dtSessions.removeAll(toRemove);
        }
    }
}
