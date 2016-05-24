package liquid.accounting.service;

import liquid.accounting.domain.Receipt;
import liquid.accounting.domain.Revenue;
import liquid.accounting.domain.SalesInvoice;
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
    private SalesInvoiceService salesInvoiceService;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public void doSaveBefore(Revenue entity) {}

    @Override
    @Transactional("transactionManager")
    public Revenue addInvoice(Long customerId, SalesInvoice salesInvoice) {
        Customer customer = customerService.find(customerId);
        salesInvoice.setCustomer(customer);
        salesInvoiceService.save(salesInvoice);

        Revenue revenue = findByCustomerId(customerId);
        revenue.setTotalInvoicedCny(revenue.getTotalInvoicedCny().add(salesInvoice.getAmountCny()));
        revenue.setTotalInvoicedUsd(revenue.getTotalInvoicedUsd().add(salesInvoice.getAmountUsd()));
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
        revenue.setTotalReceivedCny(revenue.getTotalReceivedUsd().add(receipt.getAmountCny()));
        revenue.setTotalReceivedUsd(revenue.getTotalReceivedUsd().add(receipt.getAmountUsd()));
        save(revenue);
        return revenue;
    }

    @Override
    @Transactional("transactionManager")
    public Revenue voidReceipt(Long customerId, Long receiptId) {
        Revenue revenue = findByCustomerId(customerId);

        return revenue;
    }
}
