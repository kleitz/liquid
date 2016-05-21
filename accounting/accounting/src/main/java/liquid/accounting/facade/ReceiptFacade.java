package liquid.accounting.facade;

import liquid.accounting.domain.AccountingOperator;
import liquid.accounting.domain.AccountingType;
import liquid.accounting.domain.Receipt;
import liquid.accounting.service.InternalReceiptService;
import liquid.accounting.service.InternalReceivableSummaryService;
import liquid.accounting.service.ReceiptServiceImpl;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class ReceiptFacade implements InternalReceiptService {
    @Autowired
    private ReceiptServiceImpl receiptService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InternalReceivableSummaryService receivableSummaryService;

    @Transactional(value = "transactionManager")
    public liquid.accounting.model.Receipt save(liquid.accounting.model.Receipt receipt) {
        Order orderEntity = orderService.find(receipt.getOrderId());

        Receipt receiptEntity = new Receipt();
        receiptEntity.setId(receipt.getId());
        receiptEntity.setCustomer(orderEntity.getCustomer());
        receiptEntity.setAmountCny(receipt.getCny());
        receiptEntity.setAmountUsd(receipt.getUsd());
        receiptEntity.setReceivedAt(DateUtil.dayOf(receipt.getIssuedAt()));
        receiptService.save(receiptEntity);
        receivableSummaryService.update(receipt.getOrderId(), AccountingType.PAYMENT, AccountingOperator.PLUS, receipt.getCny(), receipt.getUsd());
        return receipt;
    }

    @Transactional(value = "transactionManager")
    public liquid.accounting.model.Receipt update(liquid.accounting.model.Receipt receipt) {
        Order orderEntity = orderService.find(receipt.getOrderId());

        Receipt receiptEntity = new Receipt();
        receiptEntity.setId(receipt.getId());
        receiptEntity.setCustomer(orderEntity.getCustomer());
        receiptEntity.setAmountCny(receipt.getCny());
        receiptEntity.setAmountUsd(receipt.getUsd());
        receiptEntity.setReceivedAt(DateUtil.dayOf(receipt.getIssuedAt()));
        receiptService.save(receiptEntity);

        return receipt;
    }
}
