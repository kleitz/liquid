package liquid.accounting.service;

import liquid.accounting.domain.Receipt;
import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.domain.SalesInvoice;
import liquid.accounting.repository.ReceivableRepository;
import liquid.core.model.SearchBarForm;
import liquid.core.service.AbstractService;
import liquid.operation.domain.Customer;
import liquid.operation.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mat on 5/19/16.
 */
@Service
public class ReceivableServiceImpl extends AbstractService<ReceivableSummary, ReceivableRepository> implements ReceivableService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SalesInvoiceService salesInvoiceService;

    @Autowired
    private ReceiptService receiptService;

    @Override
    public ReceivableSummary findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Page<ReceivableSummary> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ReceivableSummary> findAll(SearchBarForm searchBarForm, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doSaveBefore(ReceivableSummary entity) {}

    @Override
    @Transactional("transactionManager")
    public ReceivableSummary addInvoice(Long customerId, SalesInvoice salesInvoice) {
        Customer customer = customerService.find(customerId);
        salesInvoice.setCustomer(customer);
        salesInvoiceService.save(salesInvoice);

        ReceivableSummary receivableSummary = findByCustomerId(customerId);
        receivableSummary.setTotalInvoicedCny(receivableSummary.getTotalInvoicedCny().add(salesInvoice.getAmountCny()));
        receivableSummary.setTotalInvoicedUsd(receivableSummary.getTotalInvoicedUsd().add(salesInvoice.getAmountUsd()));
        save(receivableSummary);
        return receivableSummary;
    }

    @Override
    @Transactional("transactionManager")
    public ReceivableSummary voidInvoice(Long customerId, Long invoiceId) {
        ReceivableSummary receivableSummary = findByCustomerId(customerId);

        return receivableSummary;
    }

    @Override
    @Transactional("transactionManager")
    public ReceivableSummary addReceipt(Long customerId, Receipt receipt) {
        Customer customer = customerService.find(customerId);
        receipt.setCustomer(customer);
        receiptService.save(receipt);

        ReceivableSummary receivableSummary = findByCustomerId(customerId);
        receivableSummary.setTotalReceivedCny(receivableSummary.getTotalReceivedUsd().add(receipt.getAmountCny()));
        receivableSummary.setTotalReceivedUsd(receivableSummary.getTotalReceivedUsd().add(receipt.getAmountUsd()));
        save(receivableSummary);
        return receivableSummary;
    }

    @Override
    @Transactional("transactionManager")
    public ReceivableSummary voidReceipt(Long customerId, Long receiptId) {
        ReceivableSummary receivableSummary = findByCustomerId(customerId);

        return receivableSummary;
    }
}
