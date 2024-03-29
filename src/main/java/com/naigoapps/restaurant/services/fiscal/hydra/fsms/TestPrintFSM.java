package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.ACK_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.NAK_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicEvents.STATUS_ARRIVED;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.FAILURE;
import static com.naigoapps.restaurant.services.fiscal.hydra.fsms.BasicHydraFSM.BasicStates.SUCCESS;

import com.naigoapps.restaurant.services.fiscal.hydra.HydraGateway;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.FeedRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.PrintRequest;

public class TestPrintFSM extends BasicHydraFSM {

    public TestPrintFSM(HydraGateway gateway) {
        super(gateway);
    }

    @Override
    protected void defineStates(StatesBuilder builder) {
        //@formatter:off
        builder.state(TestPrintStateNames.FEED_ENQ)
                .onEnter(sendEnquireRunnable())
                .transition(ACK_ARRIVED, TestPrintStateNames.FEED)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(TestPrintStateNames.FEED)
                .onEnter(sendFeedRunnable())
                .transition(STATUS_ARRIVED, TestPrintStateNames.PRINT_ENQ)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(TestPrintStateNames.PRINT_ENQ)
                .onEnter(sendEnquireRunnable())
                .transition(ACK_ARRIVED, TestPrintStateNames.PRINT)
                .transition(NAK_ARRIVED, FAILURE)
            .end()
            .state(TestPrintStateNames.PRINT)
                .onEnter(sendPrintRunnable())
                .transition(STATUS_ARRIVED, SUCCESS)
                .transition(NAK_ARRIVED, FAILURE)
            .end();
        //@formatter:on
    }

    private Runnable sendPrintRunnable() {
        PrintRequest request = new PrintRequest();
        request.setLine("Test Print");
        request.setLast(true);
        return sendCommandRunnable(request);
    }

    private Runnable sendFeedRunnable() {
        return sendCommandRunnable(new FeedRequest());
    }

    @Override
    protected StateNames firstState() {
        return TestPrintStateNames.FEED_ENQ;
    }

    private enum TestPrintStateNames implements StateNames {
        FEED_ENQ, FEED, PRINT_ENQ, PRINT
    }

}
