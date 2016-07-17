package liquid.accounting.service;

import liquid.accounting.domain.PurchaseStatement;
import liquid.accounting.repository.PurchaseStatementRepository;
import liquid.core.service.AbstractService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.Order;
import liquid.order.domain.OrderStatus;
import liquid.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mat on 7/17/16.
 */
@Service
public class PurchaseStatementServiceImpl extends AbstractService<PurchaseStatement, PurchaseStatementRepository>
        implements PurchaseStatementService  {

    @Autowired
    private PurchaseStatementRepository purchaseStatementRepository;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<PurchaseStatement> findSalesStatementByServiceProviderId(Long serviceProviderId) {
        return purchaseStatementRepository.findByServiceProviderId(serviceProviderId);
    }

    @Transactional(value = "transactionManager")
    @Override
    public PurchaseStatement save(Long serviceProviderId, Long[] orderIds) {
        ServiceProvider serviceProvider = serviceProviderService.find(serviceProviderId);
        List<Order> orders = new ArrayList<Order>(orderIds.length);
        BigDecimal totalCny = BigDecimal.ZERO;
        BigDecimal totalUsd = BigDecimal.ZERO;
        for (int i = 0; i < orderIds.length; i++) {
            Order order = orderService.find(orderIds[i]);
            orders.add(order);
            totalCny = totalCny.add(order.getTotalCny());
            totalUsd = totalUsd.add(order.getTotalUsd());
            order.setStatus(OrderStatus.PAID.getValue());
            orderService.save(order);
        }

        PurchaseStatement purchaseStatement = new PurchaseStatement();
        purchaseStatement.setServiceProvider(serviceProvider);
        purchaseStatement.setTotalCny(totalCny);
        purchaseStatement.setTotalUsd(totalUsd);
        purchaseStatement.setOrders(orders);
        return save(purchaseStatement);
    }

    @Transactional(value = "transactionManager")
    @Override
    public PurchaseStatement find(Long id) {
        PurchaseStatement statement = super.find(id);
        statement.getOrders().size();
        return statement;
    }

    @Override
    public void doSaveBefore(PurchaseStatement entity) {}
}
