package com.naigoapps.restaurant.services.fiscal.hydra;

import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HydraRoutesBuilder extends RouteBuilder {

	@Value("${fiscalPrinterAddress}")
	private String address;

	@Value("${fiscalPrinterPort}")
	private String port;
	
	@Autowired
	private ReplyHandler handler;

	@Autowired
	public HydraRoutesBuilder(CamelContext context, SettingsDao sDao) {
		Settings settings = sDao.find();
		System.setProperty("fiscalPrinterPort", String.valueOf(settings.getFiscalPrinterPort()));
		System.setProperty("fiscalPrinterAddress", String.valueOf(settings.getFiscalPrinterAddress()));
	}
	
	@Override
	public void configure() throws Exception {
		//@formatter:off
		from("direct:toHydra")
			.inOnly("netty4:udp://" + address + ":" + port + "?sync=false&udpByteArrayCodec=true");

		from("netty4:udp://0.0.0.0:" + port + "?sync=false&udpByteArrayCodec=true")
			.bean(handler, "handle");
		//@formatter:on
	}

}
