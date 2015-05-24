package liquid.accounting.facade;

import liquid.accounting.domain.ReceivableSummaryEntity;
import liquid.accounting.model.Earning;
import liquid.accounting.model.ReceivableSummary;
import liquid.accounting.service.ExchangeRateService;
import liquid.accounting.service.InternalReceivableSummaryService;
import liquid.core.model.EnhancedPageImpl;
import liquid.core.model.SearchBarForm;
import liquid.order.domain.OrderEntity;
import liquid.order.facade.OrderFacade;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class ReceivableFacadeImpl implements ReceivableFacade {

    @Autowired
    private InternalReceivableSummaryService receivableSummaryService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private OrderFacade orderFacade;

    public Page<ReceivableSummary> findAll(SearchBarForm searchBar, Pageable pageable) {
        List<ReceivableSummary> receivableList = new ArrayList<>();
        Page<ReceivableSummaryEntity> entityPage = null;
        if ("customer".equals(searchBar.getType())) {
            entityPage = receivableSummaryService.findAll(searchBar.getStartDate(), searchBar.getEndDate(), null, searchBar.getId(), pageable);
        } else if ("order".equals(searchBar.getType())) {
            entityPage = receivableSummaryService.findAll(searchBar.getStartDate(), searchBar.getEndDate(), searchBar.getId(), null, pageable);
        } else {
            entityPage = receivableSummaryService.findAll(searchBar.getStartDate(), searchBar.getEndDate(), null, null, pageable);
        }

        ReceivableSummary sum = new ReceivableSummary();
        for (ReceivableSummaryEntity entity : entityPage) {
            ReceivableSummary receivable = convert(entity);
            // FIXME - convert receivable according to order fields.
            orderFacade.convert(entity.getOrder(), receivable);
            receivableList.add(receivable);

            sum.setContainerQuantity(sum.getContainerQuantity() + receivable.getContainerQuantity());
            sum.setCny(sum.getCny().add(receivable.getCny()));
            sum.setUsd(sum.getUsd().add(receivable.getUsd()));
            sum.setRemainingBalanceCny(sum.getRemainingBalanceCny() + receivable.getRemainingBalanceCny());
            sum.setRemainingBalanceUsd(sum.getRemainingBalanceUsd() + receivable.getRemainingBalanceUsd());
            sum.setPaidCny(sum.getPaidCny() + receivable.getPaidCny());
            sum.setPaidUsd(sum.getPaidUsd() + receivable.getPaidUsd());
            sum.setInvoicedCny(sum.getInvoicedCny() + receivable.getInvoicedCny());
            sum.setInvoicedUsd(sum.getInvoicedUsd() + receivable.getInvoicedUsd());

            sum.setDistyCny(sum.getDistyCny().add(receivable.getDistyCny()));
            sum.setDistyUsd(sum.getDistyUsd().add(receivable.getDistyUsd()));

            sum.setGrandTotal(sum.getGrandTotal().add(receivable.getGrandTotal()));
        }
        return new EnhancedPageImpl<ReceivableSummary>(receivableList, pageable, entityPage.getTotalElements(), sum);
    }

    public ReceivableSummary save(ReceivableSummary receivableSummary) {
        ReceivableSummaryEntity entity = convert(receivableSummary);
        receivableSummaryService.save(entity);
        return receivableSummary;
    }

    @Override
    public Earning calculateEarning(Long orderId) {
        Earning earning = new Earning();

        BigDecimal exchangeRate = exchangeRateService.getExchangeRate().getValue();

        ReceivableSummaryEntity receivableSummaryEntity = receivableSummaryService.findByOrderId(orderId);

        earning.setSalesPriceCny(receivableSummaryEntity.getCny());
        earning.setSalesPriceUsd(receivableSummaryEntity.getUsd());
        earning.setDistyPrice(receivableSummaryEntity.getOrder().getDistyCny());
        earning.setGrandTotal(receivableSummaryEntity.getOrder().getGrandTotal());
        earning.setGrossMargin(earning.getSalesPriceCny().add(earning.getSalesPriceUsd().multiply(exchangeRate)).subtract(receivableSummaryEntity.getOrder().getGrandTotal()));
        earning.setSalesProfit(receivableSummaryEntity.getCny().add(earning.getSalesPriceUsd().multiply(exchangeRate)).subtract(receivableSummaryEntity.getOrder().getDistyCny()));
        earning.setDistyProfit(earning.getDistyPrice().subtract(receivableSummaryEntity.getOrder().getGrandTotal()));
        return earning;
    }

    private ReceivableSummary convert(ReceivableSummaryEntity entity) {
        ReceivableSummary receivableSummary = new ReceivableSummary();
        receivableSummary.setId(entity.getId());
        receivableSummary.setCny(entity.getCny());
        receivableSummary.setUsd(entity.getUsd());
        receivableSummary.setPrepaidTime(DateUtil.stringOf(entity.getPrepaidTime()));
        receivableSummary.setRemainingBalanceCny(entity.getRemainingBalanceCny());
        receivableSummary.setRemainingBalanceUsd(entity.getRemainingBalanceUsd());
        receivableSummary.setPaidCny(entity.getPaidCny());
        receivableSummary.setPaidUsd(entity.getPaidUsd());
        receivableSummary.setInvoicedCny(entity.getInvoicedCny());
        receivableSummary.setInvoicedUsd(entity.getInvoicedUsd());
        receivableSummary.setOrderId(entity.getOrder().getId());
        return receivableSummary;
    }

    private ReceivableSummaryEntity convert(ReceivableSummary receivable) {
        ReceivableSummaryEntity entity = new ReceivableSummaryEntity();
        entity.setId(receivable.getId());
        entity.setCny(receivable.getCny());
        entity.setUsd(receivable.getUsd());
        entity.setPrepaidTime(DateUtil.dateOf(receivable.getPrepaidTime()));
        entity.setRemainingBalanceCny(receivable.getRemainingBalanceCny());
        entity.setRemainingBalanceUsd(receivable.getRemainingBalanceUsd());
        entity.setPaidCny(receivable.getPaidCny());
        entity.setPaidUsd(receivable.getPaidUsd());
        entity.setInvoicedCny(receivable.getInvoicedCny());
        entity.setInvoicedUsd(receivable.getInvoicedUsd());
        entity.setOrder(OrderEntity.newInstance(OrderEntity.class, receivable.getOrderId()));
        return entity;
    }
}
