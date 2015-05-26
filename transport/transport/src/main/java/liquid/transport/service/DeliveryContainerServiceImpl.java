package liquid.transport.service;

import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.core.service.AbstractService;
import liquid.transport.domain.DeliveryContainerEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.ShippingContainerEntity;
import liquid.transport.repository.DeliveryContainerRepository;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by redbrick9 on 6/10/14.
 */
@Service
public class DeliveryContainerServiceImpl extends AbstractService<DeliveryContainerEntity, DeliveryContainerRepository>
        implements InternalDeliveryContainerService {

    @Autowired
    private DeliveryContainerRepository deliveryContainerRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Override
    public void doSaveBefore(DeliveryContainerEntity entity) {}

    public Iterable<DeliveryContainerEntity> initDeliveryContainers(Long orderId) {
        Order order = orderService.find(orderId);
        Collection<DeliveryContainerEntity> dcList = deliveryContainerRepository.findByOrder(order);
        if (dcList.size() > 0) {
            for (DeliveryContainerEntity container : dcList) {
                if (container.getEtd() != null) {
                    container.setEtdStr(DateUtil.dayStrOf(container.getEtd()));
                }
            }
            return dcList;
        }

        dcList = new ArrayList<DeliveryContainerEntity>();
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity shipment : shipmentSet) {
            List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByShipmentId(shipment.getId());
            for (ShippingContainerEntity sc : shippingContainers) {
                DeliveryContainerEntity dc = new DeliveryContainerEntity();
                dc.setOrder(shipment.getOrder());
                dc.setShipment(shipment);
                dc.setSc(sc);
                dc.setAddress(order.getConsigneeAddress());
                dcList.add(dc);
            }
        }

        return deliveryContainerRepository.save(dcList);
    }

    public DeliveryContainerEntity findDeliveryContainer(long containerId) {
        DeliveryContainerEntity deliveryContainer = deliveryContainerRepository.findOne(containerId);

        if (null == deliveryContainer.getAddress()) {
            deliveryContainer.setAddress(deliveryContainer.getOrder().getConsigneeAddress());
        }

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == deliveryContainer.getEtd())
            deliveryContainer.setEtdStr(defaultDayStr);
        else
            deliveryContainer.setEtdStr(DateUtil.dayStrOf(deliveryContainer.getEtd()));

        return deliveryContainer;
    }

    public void saveDeliveryContainer(long containerId, DeliveryContainerEntity formBean) {
        DeliveryContainerEntity container = deliveryContainerRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<DeliveryContainerEntity> containers = deliveryContainerRepository.findByOrder(container.getOrder());

            if (formBean.getEtdStr() != null && formBean.getEtdStr().trim().length() > 0) {
                for (DeliveryContainerEntity dc : containers) {
                    dc.setEtd(DateUtil.dayOf(formBean.getEtdStr()));
                }
            }

            if (formBean.getAddress() != null && formBean.getAddress().trim().length() > 0) {
                for (DeliveryContainerEntity dc : containers) {
                    dc.setAddress(formBean.getAddress());
                }
            }

            deliveryContainerRepository.save(containers);
        } else {
            if (formBean.getEtdStr() != null && formBean.getEtdStr().trim().length() > 0) {
                container.setEtd(DateUtil.dayOf(formBean.getEtdStr()));
            }
            if (formBean.getAddress() != null && formBean.getAddress().trim().length() > 0) {
                container.setAddress(formBean.getAddress());
            }
            deliveryContainerRepository.save(container);
        }
    }
}
