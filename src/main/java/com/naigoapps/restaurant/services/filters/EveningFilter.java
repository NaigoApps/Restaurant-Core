/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.naigoapps.restaurant.main.EveningManager;
import com.naigoapps.restaurant.services.utils.ResponseBuilder;

/**
 *
 * @author naigo
 */
@Provider
public class EveningFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Inject
	private EveningManager manager;

	@Override
	@Transactional
	public void filter(ContainerRequestContext requestContext) throws IOException {
		boolean auth = false;

		if (resourceInfo.getResourceClass().isAnnotationPresent(Accessible.class)
				|| resourceInfo.getResourceMethod().isAnnotationPresent(Accessible.class)) {
			auth = true;
		} else {
			auth = manager.getSelectedEvening() != null;
		}

		if (!auth) {
			requestContext.abortWith(ResponseBuilder.badRequest("Serata non selezionata"));
		}
	}

}
