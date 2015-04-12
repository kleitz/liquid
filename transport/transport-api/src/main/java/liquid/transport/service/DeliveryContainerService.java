package liquid.transport.service;

import liquid.transport.domain.DeliveryContainerEntity;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface DeliveryContainerService {
    Iterable<DeliveryContainerEntity> initDeliveryContainers(Long orderId);

    DeliveryContainerEntity findDeliveryContainer(long containerId);

    void saveDeliveryContainer(long containerId, DeliveryContainerEntity formBean);
}
