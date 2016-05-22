package liquid.accounting.service;

import liquid.accounting.domain.Invoice;
import liquid.accounting.domain.Receipt;
import liquid.accounting.domain.Revenue;
import liquid.accounting.repository.RevenueRepository;
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
public class RevenueServiceImpl extends AbstractService<Revenue, RevenueRepository> implements RevenueService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ReceiptService receiptService;

    @Override
    public Revenue findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Page<Revenue> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Revenue> findAll(SearchBarForm searchBarForm, Pageable pageable) {
        return null;
    }

    @Override
    public void doSaveBefore(Revenue entity) {}

    @Override
    @Transactional("transactionManager")
    public Revenue addInvoice(Long customerId, Invoice invoice) {
        Customer customer = customerService.find(customerId);
        invoice.setCustomer(customer);
        invoiceService.save(invoice);

        Revenue revenue = findByCustomerId(customerId);
        revenue.setTotalInvoicedCny(revenue.getTotalInvoicedCny().add(invoice.getAmountCny()));
        revenue.setTotalInvoicedUsd(revenue.getTotalInvoicedUsd().add(invoice.getAmountUsd()));
        save(revenue);
        return revenue;
    }

    @Override
    @Transactional("transactionManager")
    public Revenue voidInvoice(Long customerId, Long invoiceId) {
        Revenue revenue = findByCustomerId(customerId);

        return revenue;
    }

    @Override
    @Transactional("transactionManager")
    public Revenue addReceipt(Long customerId, Receipt receipt) {
        Customer customer = customerService.find(customerId);
        receipt.setCustomer(customer);
        receiptService.save(receipt);

        Revenue revenue = findByCustomerId(customerId);
        revenue.setTotalReceivedCny(revenue.getTotalInvoicedCny().add(receipt.getAmountCny()));
        revenue.setTotalReceivedUsd(revenue.getTotalInvoicedUsd().add(receipt.getAmountUsd()));
        save(revenue);
        return revenue;
    }

    @Override
    @Transactional("transactionManager")
    public Revenue voidReceipt(Long customerId, long receiptId) {
        Revenue revenue = findByCustomerId(customerId);

        return revenue;
    }
}
