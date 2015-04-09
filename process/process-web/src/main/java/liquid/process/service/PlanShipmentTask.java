package liquid.process.service;

import liquid.transport.domain.TransMode;
import liquid.transport.domain.LegEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Service("planShipment")
public class PlanShipmentTask extends AbstractTaskProxy {
    private static final Logger logger = LoggerFactory.getLogger(PlanShipmentTask.class);

    @Autowired
    private ShipmentService shipmentService;

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Map<String, Object> transTypes = getTransTypes(taskId);
        variableMap.putAll(transTypes);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        boolean hasWaterTransport = false;
        for (ShipmentEntity shipment : shipmentSet) {
            Collection<LegEntity> legs = shipment.getLegs();
            for (LegEntity leg : legs) {
                switch (TransMode.valueOf(leg.getTransMode())) {
                    case BARGE:
                    case VESSEL:
                        hasWaterTransport = true;
                        break;
                    default:
                        break;
                }
                if (hasWaterTransport) break;
            }
            if (hasWaterTransport) break;
        }
        variableMap.put("hasWaterTransport", hasWaterTransport);
    }

    @Transactional("transactionManager")
    public Map<String, Object> getTransTypes(String taskId) {
        Map<String, Object> transTypes = new HashMap<String, Object>();
        transTypes.put("hasRailway", false);
        transTypes.put("hasBarge", false);
        transTypes.put("hasVessel", false);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity shipment : shipmentSet) {
            Collection<LegEntity> legs = shipment.getLegs();
            for (LegEntity leg : legs) {
                TransMode mode = TransMode.valueOf(leg.getTransMode());
                switch (mode) {
                    case RAIL:
                        transTypes.put("hasRailway", true);
                        break;
                    case BARGE:
                        transTypes.put("hasBarge", true);
                        break;
                    case VESSEL:
                        transTypes.put("hasVessel", true);
                        break;
                    default:
                        logger.warn("{} transportation mode is illegal.");
                        break;
                }
            }
        }

        logger.debug("The order has the transportation {}.", transTypes);
        return transTypes;
    }
}
