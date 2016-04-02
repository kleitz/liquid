package liquid.transport.service;

import liquid.transport.domain.BargeContainer;
import liquid.transport.domain.RailContainer;
import liquid.transport.domain.ShippingContainer;
import liquid.transport.domain.VesselContainer;
import liquid.transport.model.Truck;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ShippingContainerService {
    Iterable<RailContainer> initializeRailContainers(Long orderId);

    Iterable<VesselContainer> initVesselContainers(Long orderId);

    VesselContainer findVesselContainer(long containerId);

    void saveVesselContainer(long containerId, VesselContainer formBean);

    Iterable<BargeContainer> initBargeContainers(Long orderId);

    BargeContainer findBargeContainer(long containerId);

    void saveBargeContainer(long containerId, BargeContainer formBean);

    Truck findTruck(long railContainerId);

    void saveTruck(Truck truck);

    List<ShippingContainer> findByShipmentId(Long shipmentid);

    Iterable<ShippingContainer> save(Iterable<ShippingContainer> shippingContainerEntities);

    ShippingContainer save(ShippingContainer shippingContainer);

    ShippingContainer find(Long id);
}
