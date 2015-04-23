package liquid.process.handler;

/**
 * Created by Tao Ma on 4/16/15.
 */
public interface DefinitionKey {
    String feedDistyPrice = "feedDistyPrice";
    String planShipment = "planShipment";
    String planRoute = "planRoute"; // FIXME - Will delete it after GA.
    String bookingShippingSpace = "bookingShippingSpace";
    // FIXME - We can't create the controller for this task.
    String allocateContainers = "allocateContainers";
    String feedContainerNo = "feedContainerNo";
    String sendLoadingOnYard = "sendLoadingOnYard";
    String sendTruck = "sendTruck";
    // FIXME - Will delete it after GA.
    @Deprecated
    String sendLoadingByTruck = "sendLoadingByTruck";
    // FIXME - We can't move the pages of this task into the specific folder.
    // The following 4 tasks are opearate rail_container entity.
    String applyRailwayPlan = "applyRailwayPlan";
    String loadOnYard = "loadOnYard";
    String loadByTruck = "loadByTruck";
    String recordTory = "recordTory";
    String recordTod = "recordTod";
    String CDCI = "CDCI";
    String doBargeOps = "doBargeOps";
    String doVesselOps = "doVesselOps";
    String deliver = "deliver";
    String sendInvoicing = "sendInvoicing";
}
