package liquid.accounting.facade;

import liquid.accounting.persistence.domain.ReceiptEntity;
import liquid.accounting.service.ReceiptService;
import liquid.accounting.web.domain.Receipt;
import liquid.accounting.web.domain.Statement;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.CustomerService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class ReceiptFacade {
    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    public Statement<Receipt> findByOrderId(Long orderId) {
        Statement<Receipt> statement = new Statement<>();
        List<Receipt> receiptList = new ArrayList<>();
        Long cnyTotal = 0L, usdTotal = 0L;

        Iterable<ReceiptEntity> receiptEntities = receiptService.findByOrderId(orderId);
        for (ReceiptEntity receiptEntity : receiptEntities) {
            Receipt receipt = new Receipt();
            receipt.setId(receiptEntity.getId());
            receipt.setOrderId(receiptEntity.getOrder().getId());
            receipt.setOrderNo(receiptEntity.getOrder().getOrderNo());
            receipt.setCny(receiptEntity.getCny());
            receipt.setUsd(receiptEntity.getUsd());
            receipt.setIssuedAt(DateUtil.dayStrOf(receiptEntity.getIssuedAt()));
            receiptList.add(receipt);
            cnyTotal += receipt.getCny();
            usdTotal += receipt.getUsd();
        }
        statement.setContent(receiptList);
        statement.setCnyTotal(cnyTotal);
        statement.setUsdTotal(usdTotal);

        return statement;
    }

    public Receipt save(Receipt receipt) {
        OrderEntity orderEntity = orderService.find(receipt.getOrderId());

        ReceiptEntity receiptEntity = new ReceiptEntity();
        receiptEntity.setId(receipt.getId());
        receiptEntity.setOrder(orderEntity);
        receiptEntity.setPayerId(orderEntity.getCustomerId());
        receiptEntity.setCny(receipt.getCny());
        receiptEntity.setUsd(receipt.getUsd());
        receiptEntity.setIssuedAt(DateUtil.dayOf(receipt.getIssuedAt()));
        receiptService.save(receiptEntity);
        return receipt;
    }
}
