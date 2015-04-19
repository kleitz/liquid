package liquid.transport.service;

import liquid.transport.domain.BargeContainerEntity;
import liquid.transport.domain.RailContainer;
import liquid.transport.domain.ShippingContainerEntity;
import liquid.transport.domain.VesselContainerEntity;
import liquid.transport.model.Truck;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ShippingContainerService {
    Iterable<RailContainer> initializeRailContainers(Long orderId);

    Iterable<VesselContainerEntity> initVesselContainers(Long orderId);

    VesselContainerEntity findVesselContainer(long containerId);

    void saveVesselContainer(long containerId, VesselContainerEntity formBean);

    Iterable<BargeContainerEntity> initBargeContainers(Long orderId);

    BargeContainerEntity findBargeContainer(long containerId);

    void saveBargeContainer(long containerId, BargeContainerEntity formBean);

    Truck findTruckDto(long railContainerId);

    void saveTruck(Truck truck);

    List<ShippingContainerEntity> findByShipmentId(Long shipmentid);

    Iterable<ShippingContainerEntity> save(Iterable<ShippingContainerEntity> shippingContainerEntities);

    ShippingContainerEntity save(ShippingContainerEntity shippingContainerEntity);

    ShippingContainerEntity find(Long id);
}
