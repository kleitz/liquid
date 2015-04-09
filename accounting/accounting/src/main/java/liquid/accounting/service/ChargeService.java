package liquid.accounting.service;

import liquid.accounting.domain.ChargeEntity;
import liquid.accounting.domain.ChargeEntity_;
import liquid.accounting.model.ChargeWay;
import liquid.accounting.repository.ChargeRepository;
import liquid.operation.domain.ServiceProvider_;
import liquid.order.domain.OrderEntity;
import liquid.order.domain.OrderEntity_;
import liquid.order.service.OrderService;
import liquid.security.SecurityContext;
import liquid.service.AbstractService;
import liquid.transport.domain.LegEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.repository.LegRepository;
import liquid.transport.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 2:59 PM
 */
@Service
public class ChargeService extends AbstractService<ChargeEntity, ChargeRepository> {
    private static final Logger logger = LoggerFactory.getLogger(ChargeService.class);

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    // TODO: have to enhance this function.
    @Override
    public void doSaveBefore(ChargeEntity entity) {
        // update charge
        if (null != entity.getId()) return;

        // new charge
        ShipmentEntity shipment = null;
        if (null != entity.getShipment()) {
            shipment = shipmentService.find(entity.getShipment().getId());
            entity.setOrder(shipment.getOrder());
        }
        if (null != entity.getLeg()) {
            LegEntity leg = legRepository.findOne(entity.getLeg().getId());
            shipment = leg.getShipment();
            entity.setOrder(shipment.getOrder());
        }
//        if (null == entity.getOrder()) {
//            OrderEntity order = taskService.findOrderByTaskId(entity.getTaskId());
//            entity.setOrder(order);
//        }

        if (ChargeWay.PER_ORDER.getValue() == entity.getWay()) {
            entity.setTotalPrice(entity.getUnitPrice());
            entity.setUnitPrice(new BigDecimal("0"));
        } else if (ChargeWay.PER_CONTAINER.getValue() == entity.getWay()) {
            entity.setTotalPrice(entity.getUnitPrice().multiply(new BigDecimal(shipment.getContainerQty())));
        } else {
            logger.warn("{} is out of charge way range.", entity.getWay());
        }

        entity.setCreateRole(SecurityContext.getInstance().getRole());
        // Compute grand total
        OrderEntity order = orderService.find(entity.getOrder().getId());
        if (entity.getCurrency() == 0) {
            order.setGrandTotal(order.getGrandTotal().add(entity.getTotalPrice()));
        } else {
            order.setGrandTotal(order.getGrandTotal().add(entity.getTotalPrice().multiply(exchangeRateService.getExchangeRate())));
        }
        orderService.save(order);

    }

    public Iterable<ChargeEntity> getChargesByOrderId(long orderId) {
        return chargeRepository.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    public void removeCharge(long chargeId) {
        ChargeEntity charge = chargeRepository.findOne(chargeId);
        OrderEntity order = charge.getOrder();

        if (charge.getCurrency() == 0) {
            order.setGrandTotal(order.getGrandTotal().subtract(charge.getTotalPrice()));
        } else {
            order.setGrandTotal(order.getGrandTotal().subtract(charge.getTotalPrice().multiply(exchangeRateService.getExchangeRate())));
        }

        orderService.save(order);

        chargeRepository.delete(chargeId);
    }

    public Iterable<ChargeEntity> findByLegId(long legId) {
        return chargeRepository.findByLegId(legId);
    }

    public Iterable<ChargeEntity> findByShipmentId(long shipmentId) {
        return chargeRepository.findByShipmentId(shipmentId);
    }

    public Iterable<ChargeEntity> findByTaskId(String taskId) {
        return chargeRepository.findByTaskId(taskId);
    }

    public BigDecimal total(Iterable<ChargeEntity> charges) {
        BigDecimal total = BigDecimal.ZERO;
        for (ChargeEntity charge : charges) {
            if (charge.getCurrency() == 0) {
                total = total.add(charge.getTotalPrice());
            } else {
                total = total.add(charge.getTotalPrice().multiply(exchangeRateService.getExchangeRate()));
            }
        }
        return total;
    }

    public Iterable<ChargeEntity> findByOrderId(long orderId) {
        return chargeRepository.findByOrderId(orderId);
    }

    public Page<ChargeEntity> findByOrderId(long orderId, Pageable pageable) {
        return chargeRepository.findByOrderId(orderId, pageable);
    }

    public Iterable<ChargeEntity> findByOrderNo(String orderNo) {
        return chargeRepository.findByOrderOrderNoLike("%" + orderNo + "%");
    }

    public Iterable<ChargeEntity> findBySpName(String spName) {
        return chargeRepository.findBySpNameLike("%" + spName + "%");
    }

    public ChargeEntity find(long id) {
        return chargeRepository.findOne(id);
    }

    public Iterable<ChargeEntity> findAll() {
        return chargeRepository.findAll();
    }

    public Page<ChargeEntity> findAll(Pageable pageable) {
        return chargeRepository.findAll(pageable);
    }

    public Page<ChargeEntity> findAll(final String orderNo, final String spName, final Pageable pageable) {
        List<Specification<ChargeEntity>> specList = new ArrayList<>();

        if (null != orderNo) {
            Specification<ChargeEntity> orderNoSpec = new Specification<ChargeEntity>() {
                @Override
                public Predicate toPredicate(Root<ChargeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(ChargeEntity_.order).get(OrderEntity_.orderNo), "%" + orderNo + "%");
                }
            };
            specList.add(orderNoSpec);
        }


        if (null != spName) {
            Specification<ChargeEntity> spNameSpec = new Specification<ChargeEntity>() {
                @Override
                public Predicate toPredicate(Root<ChargeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(ChargeEntity_.sp).get(ServiceProvider_.name), "%" + spName + "%");
                }
            };
            specList.add(spNameSpec);
        }

        if (specList.size() > 0) {
            Specifications<ChargeEntity> specifications = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specifications.and(specList.get(i));
            }
            return repository.findAll(specifications, pageable);
        }

        return repository.findAll(pageable);
    }

    public Page<ChargeEntity> findAll(final Date start, final Date end, final Long orderId, final Long spId, final Pageable pageable) {
        // date range
        Specification<ChargeEntity> dateRangeSpec = new Specification<ChargeEntity>() {
            @Override
            public Predicate toPredicate(Root<ChargeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(ChargeEntity_.createdAt), start, end);
            }
        };
        Specifications<ChargeEntity> specifications = Specifications.where(dateRangeSpec);

        // order id
        if (null != orderId) {
            Specification<ChargeEntity> orderIdSpec = new Specification<ChargeEntity>() {
                @Override
                public Predicate toPredicate(Root<ChargeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(ChargeEntity_.order).get(OrderEntity_.id), orderId);
                }
            };
            specifications = specifications.and(orderIdSpec);
        }

        // sp id
        if (null != spId) {
            Specification<ChargeEntity> spIdSpec = new Specification<ChargeEntity>() {
                @Override
                public Predicate toPredicate(Root<ChargeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(ChargeEntity_.sp).get(ServiceProvider_.id), spId);
                }
            };
            specifications = specifications.and(spIdSpec);
        }
        return repository.findAll(specifications, pageable);
    }

    public Iterable<ChargeEntity> findByOrderIdAndCreateRole(long orderId, String createRole) {
        return chargeRepository.findByOrderIdAndCreateRole(orderId, createRole);
    }
}
