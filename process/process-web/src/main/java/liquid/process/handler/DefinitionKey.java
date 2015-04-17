package liquid.process.handler;

/**
 * Created by Tao Ma on 4/16/15.
 */
public interface DefinitionKey {
    String feedDistyPrice = "feedDistyPrice";
    String planShipment = "planShipment";
    String planRoute = "planRoute"; // FIXME - Remove this after the old processes are completed.
    String bookingShippingSpace = "bookingShippingSpace";
    String feedContainerNo = "feedContainerNo";
    String CDCI = "CDCI";
    String allocateContainers = "allocateContainers";
    String sendInvoicing = "sendInvoicing";
    String sendTruck = "sendTruck";
}
