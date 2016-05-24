package liquid.accounting.service;

import liquid.accounting.domain.PayableSummary;
import liquid.accounting.domain.Payment;
import liquid.accounting.domain.PurchaseInvoice;
import liquid.accounting.repository.PayableSummaryRepository;
import liquid.core.model.SearchBarForm;
import liquid.core.service.AbstractService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mat on 5/24/16.
 */
@Service
public class PayableSummaryServiceImpl extends AbstractService<PayableSummary, PayableSummaryRepository>
        implements PayableSummaryService {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private PurchaseInvoiceService purchaseInvoiceService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public PayableSummary findByServiceProviderId(Long serviceProviderId) {
        return repository.findByServiceProviderId(serviceProviderId);
    }

    @Override
    public void doSaveBefore(PayableSummary entity) {

    }

    @Override
    public Page<PayableSummary> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<PayableSummary> findAll(SearchBarForm searchBarForm, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Transactional("transactionManager")
    @Override
    public PayableSummary addInvoice(Long serviceProviderId, PurchaseInvoice purchaseInvoice) {
        ServiceProvider serviceProvider = serviceProviderService.find(serviceProviderId);
        purchaseInvoice.setServiceProvider(serviceProvider);
        purchaseInvoiceService.save(purchaseInvoice);

        PayableSummary payableSummary = findByServiceProviderId(serviceProviderId);
        payableSummary.setTotalInvoicedCny(payableSummary.getTotalInvoicedCny().add(purchaseInvoice.getAmountCny()));
        payableSummary.setTotalInvoicedUsd(payableSummary.getTotalInvoicedUsd().add(purchaseInvoice.getAmountUsd()));
        save(payableSummary);
        return payableSummary;
    }

    @Override
    public PayableSummary voidInvoice(Long serviceProviderId, Long invoiceId) {
        throw new UnsupportedOperationException();
    }

    @Transactional("transactionManager")
    @Override
    public PayableSummary addPayment(Long serviceProviderId, Payment payment) {
        ServiceProvider serviceProvider = serviceProviderService.find(serviceProviderId);
        payment.setServiceProvider(serviceProvider);
        paymentService.save(payment);

        PayableSummary payableSummary = findByServiceProviderId(serviceProviderId);
        payableSummary.setTotalPaidCny(payableSummary.getTotalPaidCny().add(payment.getAmountCny()));
        payableSummary.setTotalPaidUsd(payableSummary.getTotalPaidUsd().add(payment.getAmountUsd()));
        save(payableSummary);
        return payableSummary;
    }

    @Override
    public PayableSummary voidPayment(Long serviceProviderId, Long paymentId) {
        throw new UnsupportedOperationException();
    }
}
