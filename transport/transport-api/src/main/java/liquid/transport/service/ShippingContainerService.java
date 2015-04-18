package liquid.transport.service;

import liquid.transport.domain.BargeContainerEntity;
import liquid.transport.domain.RailContainerEntity;
import liquid.transport.domain.ShippingContainerEntity;
import liquid.transport.domain.VesselContainerEntity;
import liquid.transport.model.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ShippingContainerService {
    Iterable<RailContainerEntity> initializeRailContainers(Long orderId);

    RailYard findRailYardDto(long railContainerId);

    void saveRailYard(RailYard railYard);

    RailPlan findRailPlanDto(long railContainerId);

    void saveRailPlan(RailPlan railPlan);

    Iterable<VesselContainerEntity> initVesselContainers(Long orderId);

    VesselContainerEntity findVesselContainer(long containerId);

    void saveVesselContainer(long containerId, VesselContainerEntity formBean);

    Iterable<BargeContainerEntity> initBargeContainers(Long orderId);

    BargeContainerEntity findBargeContainer(long containerId);

    void saveBargeContainer(long containerId, BargeContainerEntity formBean);

    RailShipping findRailShippingDto(long railContainerId);

    void saveRailShipping(RailShipping railShipping);

    Truck findTruckDto(long railContainerId);

    void saveTruck(Truck truck);

    List<ShippingContainerEntity> findByShipmentId(Long shipmentid);

    Iterable<ShippingContainerEntity> save(Iterable<ShippingContainerEntity> shippingContainerEntities);

    ShippingContainerEntity save(ShippingContainerEntity shippingContainerEntity);

    ShippingContainerEntity find(Long id);

    RailArrival findRailArrivalDto(long railContainerId);

    void saveRailArrival(RailArrival railArrival);
}
