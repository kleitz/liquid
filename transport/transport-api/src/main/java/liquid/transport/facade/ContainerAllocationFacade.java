package liquid.transport.facade;

import liquid.order.domain.Order;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.model.ContainerAllocation;
import liquid.transport.model.SelfContainerAllocation;
import liquid.transport.model.ShipmentContainerAllocation;

import java.util.List;

/**
 * Created by Tao Ma on 4/17/15.
 */
public interface ContainerAllocationFacade {

    List<ShipmentContainerAllocation> computeContainerAllocation(Long orderId);

    List<ShipmentContainerAllocation> computeContainerAllocation(Order order);

    ShipmentContainerAllocation getShipmentContainerAllocation(int type, String subtypeName, ShipmentEntity shipment);

    List<ContainerAllocation> computeSelfContainerAllocations(String subtypeName, ShipmentEntity shipment);

    void allocate(ShipmentContainerAllocation shipmentContainerAllocation);

    void allocate(SelfContainerAllocation selfContainerAllocation);

    void undo(long allocationId);
}
