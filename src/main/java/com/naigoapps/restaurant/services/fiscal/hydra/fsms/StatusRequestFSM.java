package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.ACK_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.NAK_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.STATUS_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.FAILURE;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.SUCCESS;

import com.naigoapps.restaurant.services.fiscal.hydra.HydraGateway;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.DeviceStatusRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.OpenDrawerRequest;

public class StatusRequestFSM extends BasicHydraFSM {

    public StatusRequestFSM(HydraGateway gateway) {
        super(gateway);
    }

    @Override
    protected void defineStates(StatesBuilder builder) {
        //@formatter:off
        builder.state(TestPrintStateNames.OPEN_ENQ)
                .onEnter(sendEnquireRunnable())
                .transition(ACK_ARRIVED, TestPrintStateNames.OPEN)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(TestPrintStateNames.OPEN)
                .onEnter(sendStatusRunnable())
                .transition(STATUS_ARRIVED, SUCCESS)
                .transition(NAK_ARRIVED, FAILURE)
            .end();
        //@formatter:on
    }

    private Runnable sendStatusRunnable() {
        DeviceStatusRequest request = new DeviceStatusRequest();
        return sendCommandRunnable(request, true);
    }

    @Override
    protected StateNames firstState() {
        return TestPrintStateNames.OPEN_ENQ;
    }

    private enum TestPrintStateNames implements StateNames {
        OPEN_ENQ, OPEN
    }

}
