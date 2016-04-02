package liquid.transport.service;

import liquid.container.domain.ContainerEntity;
import liquid.container.domain.ContainerStatus;
import liquid.container.service.ContainerService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.Order;
import liquid.order.domain.OrderContainer;
import liquid.order.service.OrderService;
import liquid.core.service.AbstractService;
import liquid.transport.domain.*;
import liquid.transport.model.Truck;
import liquid.transport.repository.*;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: tao
 * Date: 10/12/13
 * Time: 12:36 AM
 */
@Service
public class ShippingContainerServiceImpl extends AbstractService<ShippingContainer, ShippingContainerRepository>
        implements InternalShippingContainerService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private InternalShipmentService shipmentService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private RailContainerRepository rcRepository;

    @Autowired
    private BargeContainerRepository bcRepository;

    @Autowired
    private VesselContainerRepository vcRepository;

    @Autowired
    private DeliveryContainerRepository dcRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Override
    public void doSaveBefore(ShippingContainer entity) { }

    public List<ShippingContainer> findByShipmentId(Long shipmentid) {
        return repository.findByShipmentId(shipmentid);
    }

    @Transactional("transactionManager")
    public void initialize(Long orderId) {
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity shipment : shipmentSet) {
            Collection<ShippingContainer> containers = findByShipmentId(shipment.getId());
            if (null == containers) containers = new ArrayList<>();
            if (containers.size() == 0) {
                for (int i = 0; i < shipment.getContainerQty(); i++) {
                    ShippingContainer container = new ShippingContainer();
                    container.setShipment(shipment);
                    container.setCreatedAt(new Date());
                    container.setUpdatedAt(new Date());
                    containers.add(container);
                }
                repository.save(containers);
            }
        }
    }

    public ShippingContainer find(long scId) {
        return repository.findOne(scId);
    }

    @Transactional("transactionManager")
    public void allocate(Long shipmentId, ShippingContainer formBean) {
        ShippingContainer oldOne = find(formBean.getId());
        formBean.setShipment(oldOne.getShipment());

        if (null != oldOne.getContainer()) {
            oldOne.getContainer().setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(oldOne.getContainer());
        }

        ContainerEntity containerEntity = containerService.find(formBean.getContainerId());
        formBean.setContainer(containerEntity);
        repository.save(formBean);

        containerEntity.setStatus(ContainerStatus.ALLOCATED.getValue());
        containerService.save(containerEntity);
    }

    @Transactional("transactionManager")
    public void remove(long scId) {
        ShippingContainer sc = repository.findOne(scId);
        ContainerEntity containerEntity = sc.getContainer();
        repository.delete(scId);

        if (null != containerEntity) {
            containerEntity.setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(containerEntity);
        }
    }

    public Iterable<RailContainer> initializeRailContainers(Long orderId) {
        Order order = orderService.find(orderId);
        Collection<RailContainer> rcList = rcRepository.findByOrder(order);
        if (rcList.size() > 0) {
            return rcList;
        }

        rcList = new ArrayList<RailContainer>();
        for (OrderContainer container : order.getContainers()) {
            RailContainer rc = new RailContainer();
            rc.setOrder(order);
            rc.setContainer(container);
            rcList.add(rc);
        }

        return rcRepository.save(rcList);
    }

    @Deprecated
    public Iterable<RailContainer> initializeRailContainers0(Long orderId) {
        Order order = orderService.find(orderId);
        Collection<RailContainer> rcList = rcRepository.findByOrder(order);
        if (rcList.size() > 0) {
            return rcList;
        }

        rcList = new ArrayList<RailContainer>();
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity shipment : shipmentSet) {
            List<LegEntity> legList = legRepository.findByShipmentAndTransMode(shipment, TransMode.RAIL.getType());
            if (legList.size() > 0) {
                List<ShippingContainer> shippingContainers = findByShipmentId(shipment.getId());
                for (ShippingContainer sc : shippingContainers) {
                    RailContainer rc = new RailContainer();
                    rc.setOrder(shipment.getOrder());
                    rc.setShipment(shipment);
                    rc.setLeg(legList.get(0));
                    rc.setSc(sc);
                    rcList.add(rc);
                }
            }
        }

        return rcRepository.save(rcList);
    }

    public Truck findTruck(long railContainerId) {
        RailContainer railContainer = rcRepository.findOne(railContainerId);
        return toTruck(railContainer);
    }

    private Truck toTruck(RailContainer railContainer) {
        Truck truck = new Truck();
        if (null != railContainer.getFleet())
            truck.setFleetId(railContainer.getFleet().getId());
        truck.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            truck.setBicCode(railContainer.getSc().getContainer().getBicCode());
        else
            truck.setBicCode(railContainer.getSc().getBicCode());
        truck.setPlateNo(railContainer.getPlateNo());
        truck.setTrucker(railContainer.getTrucker());
        if (null == railContainer.getLoadingToc()) {
            truck.setLoadingToc(DateUtil.stringOf(new Date()));
        } else {
            truck.setLoadingToc(DateUtil.stringOf(railContainer.getLoadingToc()));
        }
        return truck;
    }

    public void saveTruck(Truck truck) {
        RailContainer container = rcRepository.findOne(truck.getRailContainerId());
        ServiceProvider fleet = serviceProviderService.find(truck.getFleetId());

        if (truck.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByShipment(container.getShipment());
            for (RailContainer railContainer : containers) {
                railContainer.setFleet(fleet);
                railContainer.setPlateNo(truck.getPlateNo());
                railContainer.setTrucker(truck.getTrucker());
                railContainer.setLoadingToc(DateUtil.dateOf(truck.getLoadingToc()));
            }

            rcRepository.save(containers);
        } else {
            container.setFleet(fleet);
            container.setPlateNo(truck.getPlateNo());
            container.setTrucker(truck.getTrucker());
            container.setLoadingToc(DateUtil.dateOf(truck.getLoadingToc()));
            rcRepository.save(container);
        }
    }

    public Iterable<BargeContainer> initBargeContainers(Long orderId) {
        Order order = orderService.find(orderId);
        Collection<BargeContainer> bcList = bcRepository.findByOrder(order);
        if (bcList.size() > 0) {
            for (WaterContainerEntity container : bcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtil.dayStrOf(container.getEts()));
                }
            }
            return bcList;
        }

        bcList = new ArrayList<BargeContainer>();
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity shipment : shipmentSet) {
            List<LegEntity> legList = legRepository.findByShipmentAndTransMode(shipment, TransMode.BARGE.getType());
            if (legList.size() > 0) {
                List<ShippingContainer> shippingContainers = shipment.getContainers();
                for (ShippingContainer sc : shippingContainers) {
                    BargeContainer bc = new BargeContainer();
                    bc.setOrder(shipment.getOrder());
                    bc.setShipment(shipment);
                    bc.setLeg(legList.get(0));
                    bc.setSc(sc);
                    bcList.add(bc);
                }
            }
        }

        return bcRepository.save(bcList);
    }

    public BargeContainer findBargeContainer(long containerId) {
        BargeContainer bargeContainer = bcRepository.findOne(containerId);

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == bargeContainer.getEts())
            bargeContainer.setEtsStr(defaultDayStr);
        else
            bargeContainer.setEtsStr(DateUtil.dayStrOf(bargeContainer.getEts()));

        return bargeContainer;
    }

    public void saveBargeContainer(long containerId, BargeContainer formBean) {
        BargeContainer container = bcRepository.findOne(containerId);
        ShipmentEntity shipment = shipmentService.find(container.getShipment().getId());

        if (formBean.isBatch()) {

            Collection<BargeContainer> containers = bcRepository.findByShipmentId(shipment.getId());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (WaterContainerEntity rc : containers) {
                    rc.setEts(DateUtil.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                for (WaterContainerEntity rc : containers) {
                    rc.setBolNo(formBean.getBolNo());
                }
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                for (WaterContainerEntity rc : containers) {
                    rc.setSlot(formBean.getSlot());
                }
            }

            bcRepository.save(containers);
        } else {
            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                container.setEts(DateUtil.dayOf(formBean.getEtsStr()));
            }
            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                container.setBolNo(formBean.getBolNo());
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                container.setSlot(formBean.getSlot());
            }
            bcRepository.save(container);
        }
    }

    public Iterable<VesselContainer> initVesselContainers(Long orderId) {
        Order order = orderService.find(orderId);
        Collection<VesselContainer> vcList = vcRepository.findByOrder(order);
        if (vcList.size() > 0) {
            for (VesselContainer container : vcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtil.dayStrOf(container.getEts()));
                }
            }
            return vcList;
        }

        vcList = new ArrayList<VesselContainer>();
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity shipment : shipmentSet) {
            List<LegEntity> legList = legRepository.findByShipmentAndTransMode(shipment, TransMode.VESSEL.getType());
            if (legList.size() > 0) {
                List<ShippingContainer> shippingContainers = findByShipmentId(shipment.getId());
                for (ShippingContainer sc : shippingContainers) {
                    VesselContainer vc = new VesselContainer();
                    vc.setOrder(shipment.getOrder());
                    vc.setShipment(shipment);
                    vc.setLeg(legList.get(0));
                    vc.setSc(sc);
                    vcList.add(vc);
                }
            }
        }

        return vcRepository.save(vcList);
    }

    public VesselContainer findVesselContainer(long containerId) {
        VesselContainer vesselContainer = vcRepository.findOne(containerId);

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == vesselContainer.getEts())
            vesselContainer.setEtsStr(defaultDayStr);
        else
            vesselContainer.setEtsStr(DateUtil.dayStrOf(vesselContainer.getEts()));

        return vesselContainer;
    }

    public void saveVesselContainer(long containerId, VesselContainer formBean) {
        VesselContainer container = vcRepository.findOne(containerId);
        ShipmentEntity shipment = shipmentService.find(container.getShipment().getId());

        if (formBean.isBatch()) {
            Collection<VesselContainer> containers = vcRepository.findByShipmentId(shipment.getId());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (VesselContainer rc : containers) {
                    rc.setEts(DateUtil.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                for (VesselContainer rc : containers) {
                    rc.setBolNo(formBean.getBolNo());
                }
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                for (VesselContainer rc : containers) {
                    rc.setSlot(formBean.getSlot());
                }
            }

            vcRepository.save(containers);
        } else {
            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                container.setEts(DateUtil.dayOf(formBean.getEtsStr()));
            }
            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                container.setBolNo(formBean.getBolNo());
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                container.setSlot(formBean.getSlot());
            }
            vcRepository.save(container);
        }
    }
}
