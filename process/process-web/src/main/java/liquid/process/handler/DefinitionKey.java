package liquid.process.handler;

/**
 * Created by Tao Ma on 4/16/15.
 */
public interface DefinitionKey {
    String feedDistyPrice = "feedDistyPrice";
    String planShipment = "planShipment";
    String planRoute = "planRoute"; // FIXME - Remove this after the old processes are completed.
    String bookingShippingSpace = "bookingShippingSpace";
    // FIXME - We can't create the controller for this task.
    String allocateContainers = "allocateContainers";
    String sendLoadingOnYard = "sendLoadingOnYard";
    // FIXME - We can't move the pages of this task into the specific folder.
    String applyRailwayPlan = "applyRailwayPlan";
    String feedContainerNo = "feedContainerNo";
    String CDCI = "CDCI";
    String sendInvoicing = "sendInvoicing";
    String sendTruck = "sendTruck";
}
