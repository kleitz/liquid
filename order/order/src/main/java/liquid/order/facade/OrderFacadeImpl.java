package liquid.order.facade;

import liquid.accounting.domain.Currency;
import liquid.accounting.facade.ReceivableFacade;
import liquid.accounting.model.ReceivableSummary;
import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.core.security.SecurityContext;
import liquid.core.validation.FormValidationResult;
import liquid.operation.domain.Customer;
import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.*;
import liquid.order.domain.*;
import liquid.order.model.Order;
import liquid.order.model.ServiceItem;
import liquid.order.service.OrderService;
import liquid.order.service.RailwayService;
import liquid.process.service.BusinessKey;
import liquid.process.service.ProcessService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by redbrick9 on 5/28/14.
 */
@Service
public class OrderFacadeImpl implements InternalOrderFacade {
    @Autowired
    private Environment env;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RailwayService railwayService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @Autowired
    private ReceivableFacade receivableFacade;

    // FIXME - REMOVE
    @Transactional("transactionManager")
    public OrderEntity save(Order order) {
        List<ServiceItem> serviceItemList = order.getServiceItems();
        Iterator<ServiceItem> serviceItemIterator = serviceItemList.iterator();
        while (serviceItemIterator.hasNext()) {
            ServiceItem serviceItem = serviceItemIterator.next();
            if (serviceItem.getQuotation() == null) serviceItemIterator.remove();
        }

        OrderEntity orderEntity = convert(order);
        OrderRail railwayEntity = convertRailway(order);
        railwayEntity.setOrder(orderEntity);
        orderEntity.setRailway(railwayEntity);
        orderEntity = orderService.save(orderEntity);

        ReceivableSummary receivableSummary = new ReceivableSummary();
        receivableSummary.setCny(order.getCnyTotal());
        receivableSummary.setUsd(order.getUsdTotal());
        receivableSummary.setOrderId(order.getId());
        order.setId(orderEntity.getId());
        convert(orderEntity, receivableSummary);
        // TODO: workaround
        receivableSummary.setId(null);
        receivableSummary.setOrderId(orderEntity.getId());
        receivableFacade.save(receivableSummary);

        return orderEntity;
    }

    // FIXME - REMOVE
    public OrderEntity submit(Order order) {
        // set role
        order.setRole(SecurityContext.getInstance().getRole());

        // compute order no.
        order.setOrderNo(orderService.computeOrderNo(order.getRole(), order.getServiceType().getCode()));
        OrderEntity orderEntity = save(order);

        boolean hasDelivery = false;
        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            if (serviceItem.getServiceSubtypeId() == Long.valueOf(env.getProperty("service.subtype.delivery.id"))) {
                hasDelivery = true;
                break;
            }
        }

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("loadingType", orderEntity.getLoadingType());
        variableMap.put("hasDelivery", hasDelivery);
        variableMap.put("orderOwner", orderEntity.getUpdatedBy());
        variableMap.put("tradeType", orderEntity.getTradeType());
        processService.startProcess(orderEntity.getUpdatedBy(), BusinessKey.encode(orderEntity.getId(), orderEntity.getOrderNo()), variableMap);
        return orderEntity;
    }

    public Order find(long id) {
        OrderEntity orderEntity = orderService.find(id);
        Order order = convert(orderEntity);
        return order;
    }

    public Page<Order> findByCreateUser(String username, Pageable pageable) {
        Page<OrderEntity> page = orderService.findByCreateUser(username, pageable);

        List<OrderEntity> entities = page.getContent();
        List<Order> orders = convert(entities);

        return new PageImpl<Order>(orders, pageable, page.getTotalElements());
    }

    public List<Order> convert(List<OrderEntity> entities) {
        List<Order> orders = new ArrayList<>(entities.size());
        for (OrderEntity entity : entities) {
            orders.add(convertBasic(entity));
        }
        return orders;
    }

    public Page<Order> findAll(Pageable pageable) {
        Page<OrderEntity> page = orderService.findAll(pageable);

        List<OrderEntity> entities = page.getContent();
        List<Order> orders = convert(entities);

        return new PageImpl<Order>(orders, pageable, page.getTotalElements());
    }

    public Page<Order> findAll(final String orderNo, final String customerName, final String username, final Pageable pageable) {
        Page<OrderEntity> page = orderService.findAll(orderNo, customerName, username, pageable);

        List<OrderEntity> entities = page.getContent();
        List<Order> orders = convert(entities);

        return new PageImpl<Order>(orders, pageable, page.getTotalElements());
    }

    public Page<Order> findByStatus(Integer status, Pageable pageable) {
        Page<OrderEntity> page = orderService.findByStatus(status, pageable);

        List<OrderEntity> entities = page.getContent();
        List<Order> orders = convert(entities);

        return new PageImpl<Order>(orders, pageable, page.getTotalElements());
    }

    // FIXME - REMOVE
    public Order duplicate(long id) {
        OrderEntity orderEntity = orderService.find(id);
        Order order = convert(orderEntity);
        order.setId(null);
        order.setRailwayId(null);
        order.setOrderNo(null);

        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            serviceItem.setId(null);
        }

        return order;
    }

    public OrderEntity convert(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setOrderNo(order.getOrderNo());
        orderEntity.setServiceType(order.getServiceType());
        orderEntity.setCustomer(order.getCustomer());
        orderEntity.setTradeType(order.getTradeType());
        orderEntity.setVerificationSheetSn(order.getVerificationSheetSn());
        orderEntity.setSource(order.getSource());
        orderEntity.setDestination(order.getDestination());
        orderEntity.setConsignee(order.getConsignee());
        orderEntity.setConsigneePhone(order.getConsigneePhone());
        orderEntity.setConsigneeAddress(order.getConsigneeAddress());
        orderEntity.setGoods(order.getGoods());
        orderEntity.setGoodsWeight(order.getGoodsWeight());
        orderEntity.setGoodsDimension(order.getGoodsDimension());
        orderEntity.setLoadingType(order.getLoadingType());
        orderEntity.setLoadingAddress(order.getLoadingAddress());
        orderEntity.setLoadingContact(order.getLoadingContact());
        orderEntity.setLoadingPhone(order.getLoadingPhone());
        orderEntity.setLoadingEt(DateUtil.dateOf(order.getLoadingEstimatedTime()));
        orderEntity.setContainerType(order.getContainerType());
        orderEntity.setContainerSubtype(order.getContainerSubtype());
        orderEntity.setContainerQty(order.getContainerQuantity());
        orderEntity.setContainerAttribute(order.getContainerAttribute());

        for (ServiceItem serviceItem : order.getServiceItems()) {
            ServiceItemEntity serviceItemEntity = new ServiceItemEntity();
            serviceItemEntity.setId(serviceItem.getId());
            serviceItemEntity.setOrder(orderEntity);
            serviceItemEntity.setServiceSubtype(ServiceSubtype.
                    newInstance(ServiceSubtype.class, serviceItem.getServiceSubtypeId()));
            serviceItemEntity.setCurrency(serviceItem.getCurrency());
            serviceItemEntity.setQuotation(serviceItem.getQuotation());
            orderEntity.getServiceItems().add(serviceItemEntity);
        }
//        ReceivableSummaryEntity receivableSummaryEntity = new ReceivableSummaryEntity();
//        receivableSummaryEntity.setId(order.getReceivableSummaryId());
//        receivableSummaryEntity.setOrder(orderEntity);
//        receivableSummaryEntity.setCny(order.getCnyTotal());
//        receivableSummaryEntity.setUsd(order.getUsdTotal());
//        receivableSummaryEntity.setRemainingBalanceCny(order.getCnyTotal());
//        receivableSummaryEntity.setRemainingBalanceUsd(order.getUsdTotal());

        orderEntity.setTotalCny(order.getCnyTotal());
        orderEntity.setTotalUsd(order.getUsdTotal());
        orderEntity.setCreateRole(SecurityContext.getInstance().getRole());
        orderEntity.setStatus(order.getStatus());

        return orderEntity;
    }

    // TODO: Should enhance it in one to one way.
    public Order convert(OrderEntity orderEntity) {
        Order order = convertBasic(orderEntity);

        List<ServiceItemEntity> serviceItemEntities = orderEntity.getServiceItems();
        for (ServiceItemEntity serviceItemEntity : serviceItemEntities) {
            ServiceItem serviceItem = new ServiceItem();
            serviceItem.setId(serviceItemEntity.getId());
            serviceItem.setServiceSubtypeId(serviceItemEntity.getServiceSubtype().getId());
            serviceItem.setServiceSubtype(serviceItemEntity.getServiceSubtype().getName());
            serviceItem.setCurrency(serviceItemEntity.getCurrency());
            serviceItem.setCurrencyText(Currency.valueOf(serviceItemEntity.getCurrency()).toString());
            serviceItem.setQuotation(serviceItemEntity.getQuotation());
            serviceItem.setComment(serviceItemEntity.getComment());
            order.getServiceItems().add(serviceItem);
        }
        return order;
    }

    public Order convertBasic(OrderEntity orderEntity) {
        Order order = new Order();
        convert(orderEntity, order);
        return order;
    }

    public void convert(OrderEntity orderEntity, Order order) {
        order.setId(orderEntity.getId());
        order.setOrderNo(orderEntity.getOrderNo());
        order.setServiceType(orderEntity.getServiceType());
        order.setCustomer(orderEntity.getCustomer());
        order.setTradeType(orderEntity.getTradeType());
        order.setTradeTypeName(TradeType.valueOf(orderEntity.getTradeType()).getI18nKey());
        order.setVerificationSheetSn(orderEntity.getVerificationSheetSn());
        order.setSource(orderEntity.getSource());
        order.setDestination(orderEntity.getDestination());
        order.setConsignee(orderEntity.getConsignee());
        order.setConsigneePhone(orderEntity.getConsigneePhone());
        order.setConsigneeAddress(orderEntity.getConsigneeAddress());
        order.setGoods(orderEntity.getGoods());
        order.setGoodsWeight(orderEntity.getGoodsWeight());
        order.setGoodsDimension(orderEntity.getGoodsDimension());
        order.setLoadingType(orderEntity.getLoadingType());
        order.setLoadingTypeName(LoadingType.valueOf(orderEntity.getLoadingType()).getI18nKey());
        order.setLoadingAddress(orderEntity.getLoadingAddress());
        order.setLoadingContact(orderEntity.getLoadingContact());
        order.setLoadingPhone(orderEntity.getLoadingPhone());
        order.setLoadingEstimatedTime(DateUtil.stringOf(orderEntity.getLoadingEt()));
        order.setContainerType(orderEntity.getContainerType());
        order.setContainerTypeName(ContainerType.valueOf(orderEntity.getContainerType()).getI18nKey());
        order.setContainerSubtype(orderEntity.getContainerSubtype());
        order.setContainerQuantity(orderEntity.getContainerQty());
        order.setContainerAttribute(orderEntity.getContainerAttribute());

        OrderRail railwayEntity = orderEntity.getRailway();
        if (null != railwayEntity) {
            order.setRailwayId(railwayEntity.getId());
            order.setPlanReportTime(DateUtil.stringOf(railwayEntity.getPlanReportTime()));
            order.setRailPlanType(railwayEntity.getPlanType());
            order.setProgramNo(railwayEntity.getProgramNo());
            order.setPlanGoods(railwayEntity.getPlanGoods());
            order.setRailSource(railwayEntity.getSource());
            order.setRailDestination(railwayEntity.getDestination());
            order.setComment(railwayEntity.getComment());
            order.setSameDay(railwayEntity.getSameDay());
        } else {
            order.setPlanReportTime(DateUtil.stringOf(new Date()));
        }


        order.setGrandTotal(orderEntity.getGrandTotal());
        order.setCnyTotal(orderEntity.getTotalCny());
        order.setUsdTotal(orderEntity.getTotalUsd());
        order.setDistyCny(orderEntity.getDistyCny());
        order.setDistyUsd(orderEntity.getDistyUsd());

        order.setCreatedBy(orderEntity.getCreatedBy());
        order.setCreatedAt(DateUtil.stringOf(orderEntity.getCreatedAt()));
        order.setUpdatedAt(DateUtil.stringOf(orderEntity.getUpdatedAt()));
        order.setRole(orderEntity.getCreateRole());
        order.setStatus(orderEntity.getStatus());
    }

    private OrderRail convertRailway(Order order) {
        OrderRail railwayEntity = new OrderRail();
        railwayEntity.setId(order.getRailwayId());
        railwayEntity.setPlanReportTime(DateUtil.dateOf(order.getPlanReportTime()));
        railwayEntity.setPlanType(order.getRailPlanType());
        railwayEntity.setPlanGoods(order.getPlanGoods());
        railwayEntity.setSource(order.getRailSource());
        railwayEntity.setDestination(order.getRailDestination());
        railwayEntity.setProgramNo(order.getProgramNo());
        railwayEntity.setComment(order.getComment());
        railwayEntity.setSameDay(order.getSameDay());
        return railwayEntity;
    }

    /**
     * If customer name is exactly equals to the one in database, the customer id is set.
     *
     * @param id
     * @param name
     * @return
     */
    public FormValidationResult validateCustomer(Long id, String name) {
        if (null == id) {
            if (null != name && name.trim().length() > 0) {
                Customer customer = customerService.findByName(name);
                if (null != customer) {
                    return FormValidationResult.newSuccess(customer.getId(), name);
                }
            }
            return FormValidationResult.newFailure();
        }

        Customer customer = customerService.find(id);
        if (name == null) return FormValidationResult.newFailure();
        if (null == customer) return FormValidationResult.newFailure();

        if (name.equals(customer.getName()))
            return FormValidationResult.newSuccess(customer.getId(), customer.getName());
        else return FormValidationResult.newFailure();
    }

    public FormValidationResult validateLocation(Long id, String name) {
        if (null == id) {
            if (null != name && name.trim().length() > 0) {
                Location location = locationService.findByName(name);
                if (null != location) {
                    return FormValidationResult.newSuccess(location.getId(), location.getName());
                }
            }
            return FormValidationResult.newFailure();
        }

        Location location = locationService.find(id);
        if (name == null) return FormValidationResult.newFailure();
        if (null == location) return FormValidationResult.newFailure();
        if (name.equals(location.getName()))
            return FormValidationResult.newSuccess(location.getId(), location.getName());
        else return FormValidationResult.newFailure();
    }

    public FormValidationResult validateLocation(Long id, String name, Byte typeId) {
        if (null == id) {
            if (null != name && name.trim().length() > 0) {
                Location location = locationService.findByTypeAndName(typeId, name);
                if (null != location) {
                    return FormValidationResult.newSuccess(location.getId(), location.getName());
                }
            }
            return FormValidationResult.newFailure();
        }

        Location location = locationService.find(id);
        if (name == null) return FormValidationResult.newFailure();
        if (null == location) return FormValidationResult.newFailure();
        if (name.equals(location.getName()))
            return FormValidationResult.newSuccess(location.getId(), location.getName());
        else return FormValidationResult.newFailure();
    }

    public Page<Order> findByCustomerId(Long customerId, Pageable pageable) {
        Page<OrderEntity> page = orderService.findByCustomerId(customerId, SecurityContext.getInstance().getUsername(), pageable);

        List<OrderEntity> entities = page.getContent();
        List<Order> orders = convert(entities);

        return new PageImpl<Order>(orders, pageable, page.getTotalElements());
    }
}
