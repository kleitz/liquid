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
    String feedContainerNo = "feedContainerNo";
    String sendLoadingOnYard = "sendLoadingOnYard";
    // FIXME - We can't move the pages of this task into the specific folder.
    String applyRailwayPlan = "applyRailwayPlan";
    String sendTruck = "sendTruck";
    String loadOnYard = "loadOnYard";
    String recordTory = "recordTory";
    String recordTod = "recordTod";
    String CDCI = "CDCI";
    String doBargeOps = "doBargeOps";
    String doVesselOps = "doVesselOps";
    String deliver = "deliver";
    String sendInvoicing = "sendInvoicing";
}
