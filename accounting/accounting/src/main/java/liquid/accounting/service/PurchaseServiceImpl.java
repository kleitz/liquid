package liquid.accounting.service;

import liquid.accounting.domain.PayableSummary;
import liquid.accounting.domain.Purchase;
import liquid.accounting.domain.PurchaseStatus;
import liquid.accounting.repository.ChargeRepository;
import liquid.accounting.repository.PurchaseRepository;
import liquid.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mat on 9/27/14.
 */
@Service
public class PurchaseServiceImpl extends AbstractService<Purchase, PurchaseRepository> implements PurchaseService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PayableSummaryService payableSummaryService;

    @Override
    public void doSaveBefore(Purchase entity) {
    }

    @Deprecated
    @Override
    public void deleteByLegId(Long legId) {
        chargeRepository.deleteByLegId(legId);
    }

    @Override
    public List<Purchase> findByOrderId(Long orderId) {
        return purchaseRepository.findByOrderId(orderId);
    }

    @Override
    public List<Purchase> findByServiceProviderId(Long serviceProviderId) {
        return repository.findBySpId(serviceProviderId);
    }

    @Transactional("transactionManager")
    @Override
    public Purchase addOne(Purchase purchase) {
        purchase.setStatus(PurchaseStatus.VALID.ordinal());
        save(purchase);

        PayableSummary payableSummary = payableSummaryService.findByServiceProviderId(purchase.getSp().getId());
        if(null == payableSummary) {
            payableSummary = new PayableSummary();
            payableSummary.setServiceProvider(purchase.getSp());
        }
        switch (purchase.getCurrency()) {
            case CNY:
                payableSummary.setTotalCny(payableSummary.getTotalCny().add(purchase.getTotalAmount()));
                break;
            case USD:
                payableSummary.setTotalUsd(payableSummary.getTotalUsd().add(purchase.getTotalAmount()));
                break;
            default:
                break;
        }
        payableSummaryService.save(payableSummary);

        return purchase;
    }

    @Override
    public Purchase voidOne(Purchase purchase) {
        Purchase originalOne = purchaseRepository.findOne(purchase.getId());
        originalOne.setStatus(PurchaseStatus.INVALID.ordinal());
        originalOne.setComment(purchase.getComment());
        return purchaseRepository.save(originalOne);
    }
}
