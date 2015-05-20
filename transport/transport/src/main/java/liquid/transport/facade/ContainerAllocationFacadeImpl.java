package liquid.transport.facade;

import liquid.container.domain.ContainerEntity;
import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.transport.domain.RailContainer;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.ShippingContainerEntity;
import liquid.transport.model.ContainerAllocation;
import liquid.transport.model.SelfContainerAllocation;
import liquid.transport.model.ShipmentContainerAllocation;
import liquid.transport.service.ContainerAllocationService;
import liquid.transport.service.RailContainerService;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import liquid.util.CollectionUtil;
import liquid.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by redbrick9 on 5/20/14.
 */
@Service
public class ContainerAllocationFacadeImpl implements ContainerAllocationFacade {

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ContainerAllocationService containerAllocationService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private RailContainerService railContainerService;

    public List<ShipmentContainerAllocation> computeContainerAllocation(Long orderId) {
        OrderEntity order = orderService.find(orderId);
        return computeContainerAllocation(order);
    }

    public List<ShipmentContainerAllocation> computeContainerAllocation(OrderEntity order) {
        List<ShipmentContainerAllocation> shipmentContainerAllocations = new ArrayList<>();

        int type = order.getContainerType();
        String subtypeName = order.getContainerSubtype().getName();

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity shipment : shipmentSet) {
            ShipmentContainerAllocation shipmentContainerAllocation = getShipmentContainerAllocation(type, subtypeName, shipment);
            shipmentContainerAllocations.add(shipmentContainerAllocation);
        }

        return shipmentContainerAllocations;
    }

    public ShipmentContainerAllocation getShipmentContainerAllocation(int type, String subtypeName, ShipmentEntity shipment) {
        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        shipmentContainerAllocation.setShipmentId(shipment.getId());
        shipmentContainerAllocation.setShipment(shipment);
        shipmentContainerAllocation.setContainerSubtype(subtypeName);
        shipmentContainerAllocation.setType(type);
        List<ContainerAllocation> containerAllocations = null;
        if (ContainerType.RAIL.getType() == type) {
            containerAllocations = computeRailContainerAllocations(subtypeName, shipment);
        } else {
            containerAllocations = computeSelfContainerAllocations(subtypeName, shipment);
        }
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        return shipmentContainerAllocation;
    }

    public List<ContainerAllocation> computeSelfContainerAllocations(String subtypeName, ShipmentEntity shipment) {
        List<ContainerAllocation> containerAllocations = new ArrayList<>();

        List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByShipmentId(shipment.getId());
        int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();

        Collection<RailContainer> railContainers = railContainerService.findByShipmentId(shipment.getId());
        for (int j = 0; j < allocatedQuantity; j++) {
            ContainerAllocation containerAllocation = new ContainerAllocation();
            ShippingContainerEntity shippingContainer = shippingContainers.get(j);
            if (null != shippingContainer.getContainer()) {
                containerAllocation.setAllocationId(shippingContainer.getId());
                containerAllocation.setShipmentId(shipment.getId());
                containerAllocation.setTypeNameKey(ContainerType.SELF.getI18nKey());
                containerAllocation.setSubtypeName(subtypeName);

                containerAllocation.setContainerId(shippingContainers.get(j).getContainer().getId());
                containerAllocation.setBicCode(shippingContainers.get(j).getContainer().getBicCode());
                containerAllocation.setOwner(shippingContainers.get(j).getContainer().getOwner().getName());
                containerAllocation.setYard(shippingContainers.get(j).getContainer().getYard().getName());

                for (RailContainer railContainer : railContainers) {
                    if (railContainer.getSc().getId().equals(shippingContainer.getId())) {
                        if (null != railContainer.getTruck())
                            containerAllocation.setTruckId(railContainer.getTruck().getId());
                    }
                }
                containerAllocations.add(containerAllocation);
            }
        }

        return containerAllocations;
    }

    private List<ContainerAllocation> computeRailContainerAllocations(String subtypeName, ShipmentEntity shipment) {
        List<ContainerAllocation> containerAllocations = new ArrayList<>();
        List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByShipmentId(shipment.getId());
        int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();
        for (int j = 0; j < allocatedQuantity; j++) {
            ContainerAllocation containerAllocation = new ContainerAllocation();
            containerAllocation.setAllocationId(shippingContainers.get(j).getId());
            containerAllocation.setShipmentId(shipment.getId());
            containerAllocation.setTypeNameKey(ContainerType.RAIL.getI18nKey());
            containerAllocation.setSubtypeName(subtypeName);
            containerAllocation.setBicCode(shippingContainers.get(j).getBicCode());
            containerAllocations.add(containerAllocation);
        }
        for (int j = allocatedQuantity; j < shipment.getContainerQty(); j++) {
            ContainerAllocation containerAllocation = new ContainerAllocation();
            containerAllocation.setShipmentId(shipment.getId());
            containerAllocation.setTypeNameKey(ContainerType.RAIL.getI18nKey());
            containerAllocation.setSubtypeName(subtypeName);
            containerAllocation.setBicCode("");
            containerAllocations.add(containerAllocation);
        }
        return containerAllocations;
    }

    public void allocate(ShipmentContainerAllocation shipmentContainerAllocation) {
        List<ContainerAllocation> containerAllocations = shipmentContainerAllocation.getContainerAllocations();

        List<ShippingContainerEntity> shippingContainers = new ArrayList<ShippingContainerEntity>();
        for (int i = 0; i < containerAllocations.size(); i++) {
            ContainerAllocation containerAllocation = containerAllocations.get(i);
            ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
            shippingContainer.setId(containerAllocation.getAllocationId());
            if (!StringUtil.valid(containerAllocation.getBicCode())) shippingContainer.setBicCode("");
            else shippingContainer.setBicCode(containerAllocation.getBicCode());
            shippingContainer.setShipment(ShipmentEntity.newInstance(ShipmentEntity.class, shipmentContainerAllocation.getShipmentId()));
            shippingContainers.add(shippingContainer);
        }

        shippingContainerService.save(shippingContainers);
    }

    public void allocate(SelfContainerAllocation selfContainerAllocation) {
        ShipmentEntity shipment = shipmentService.find(selfContainerAllocation.getShipmentId());

        List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByShipmentId(selfContainerAllocation.getShipmentId());
        if (CollectionUtil.isEmpty(shippingContainers))
            shippingContainers = new ArrayList<ShippingContainerEntity>();

        for (int i = shippingContainers.size(); i < shipment.getContainerQty(); i++) {
            ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
            shippingContainer.setShipment(shipment);
            shippingContainers.add(shippingContainer);
        }

        for (int i = 0, j = 0; i < shippingContainers.size() && j < selfContainerAllocation.getContainerIds().length; i++) {
            ShippingContainerEntity shippingContainer = shippingContainers.get(i);
            if (null == shippingContainer.getContainer()) {
                shippingContainer.setContainer(ContainerEntity.newInstance(ContainerEntity.class,
                        selfContainerAllocation.getContainerIds()[j]));
                shippingContainer.setShipment(shipment);
                j++;
            }
        }
        containerAllocationService.allocate(shippingContainers);
    }

    public void undo(long allocationId) {
        ShippingContainerEntity shippingContainer = shippingContainerService.find(allocationId);
        containerAllocationService.undo(shippingContainer);
    }
}
