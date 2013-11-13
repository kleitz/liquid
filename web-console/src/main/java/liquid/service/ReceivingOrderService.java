package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.ReceivingContainerRepository;
import liquid.persistence.repository.ReceivingOrderRepository;
import liquid.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:57 PM
 */
@Service
public class ReceivingOrderService {
    private static Logger logger = LoggerFactory.getLogger(ReceivingOrderService.class);

    // Repositories
    @Autowired
    private ReceivingOrderRepository recvOrderRepository;

    @Autowired
    private ReceivingContainerRepository recvContainerRepository;

    // Services
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private LocationService locationService;

    public ReceivingOrder newOrder(List<Location> locations) {
        ReceivingOrder order = new ReceivingOrder();
        Location second = CollectionUtils.tryToGet2ndElement(locations);
        order.setOrigination(second.getId());
        return order;
    }

    @Transactional(value = "transactionManager")
    public ReceivingOrder save(ReceivingOrder order) {
        Customer customer = customerService.find(order.getCustomerId());
        Goods goods = cargoTypeService.find(order.getGoodsId());
        Location srcLoc = locationService.find(order.getOrigination());
        Location dstLoc = locationService.find(order.getDestination());
        order.setCustomer(customer);
        order.setGoods(goods);
        order.setSrcLoc(srcLoc);
        order.setDstLoc(dstLoc);

        logger.debug("receivingOrder: {}", order);
        ReceivingOrder newOne = recvOrderRepository.save(order);

        Collection<ReceivingContainer> containers = recvContainerRepository.findByReceivingOrder(order);
        recvContainerRepository.delete(containers);

        containers.clear();
        for (String bicCode : order.getBicCodes()) {
            ReceivingContainer container = new ReceivingContainer();
            container.setReceivingOrder(newOne);
            container.setBicCode(bicCode);
            containers.add(container);
        }
        recvContainerRepository.save(containers);

        return newOne;
    }

    public Iterable<ReceivingOrder> findAll() {
        return recvOrderRepository.findAll();
    }

    public List<ReceivingOrder> findAllOrderByDesc() {
        return recvOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public ReceivingOrder find(long id) {
        ReceivingOrder order = recvOrderRepository.findOne(id);

        order.setOrigination(order.getSrcLoc().getId());
        order.setDestination(order.getDstLoc().getId());
        order.setCustomerId(order.getCustomer().getId());
        order.setGoodsId(order.getGoods().getId());

        Iterable<ReceivingContainer> containers = recvContainerRepository.findByReceivingOrder(order);
        for (ReceivingContainer container : containers) {
            order.getBicCodes().add(container.getBicCode());
        }

        return order;
    }

    public Iterable<ReceivingOrder> findByCustomerName(String customerName) {
        return recvOrderRepository.findByCustomerNameLike("%" + customerName + "%");
    }
}