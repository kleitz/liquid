package liquid.accounting.service;

import liquid.accounting.domain.SalesStatement;
import liquid.accounting.repository.SalesStatementRepository;
import liquid.core.service.AbstractService;
import liquid.operation.domain.Customer;
import liquid.operation.service.CustomerService;
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
 * Created by mat on 6/6/16.
 */
@Service
public class SalesStatementServiceImpl extends AbstractService<SalesStatement, SalesStatementRepository>
        implements SalesStatementService {

    @Autowired
    private SalesStatementRepository salesStatementRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<SalesStatement> findSalesStatementByCustomerId(Long customerId) {
        return salesStatementRepository.findByCustomerId(customerId);
    }

    @Transactional(value = "transactionManager")
    @Override
    public SalesStatement save(Long customerId, Long[] orderIds) {
        Customer customer = customerService.find(customerId);
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

        SalesStatement salesStatement = new SalesStatement();
        salesStatement.setCustomer(customer);
        salesStatement.setTotalCny(totalCny);
        salesStatement.setTotalUsd(totalUsd);
        salesStatement.setOrders(orders);
        return save(salesStatement);
    }

    @Transactional(value = "transactionManager")
    @Override
    public SalesStatement find(Long id) {
        SalesStatement statement = super.find(id);
        statement.getOrders().size();
        return statement;
    }

    @Override
    public void doSaveBefore(SalesStatement entity) {}
}
