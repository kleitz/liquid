package liquid.accounting.controller;

import liquid.accounting.domain.*;
import liquid.accounting.service.*;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import liquid.operation.domain.Currency;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.CustomerService;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.order.domain.Order;
import liquid.order.domain.OrderStatus;
import liquid.order.domain.ServiceItem;
import liquid.order.domain.TradeType;
import liquid.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Controller
@RequestMapping("/accounting")
public class AccountingController {
    private static final Logger logger = LoggerFactory.getLogger(AccountingController.class);
    private static final int size = 20;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private InternalReceivableSummaryObsoloteService receivableSummaryService;

    @Autowired
    private ReceivableService receivableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SalesInvoiceService salesInvoiceService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PayableSummaryService payableSummaryService;

    @Autowired
    private PurchaseInvoiceService purchaseInvoiceService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SalesStatementService salesStatementService;

    @Autowired
    private PurchaseStatementService purchaseStatementService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(value = "/gross_profit", method = RequestMethod.GET)
    public String grossProfit(@Valid SearchBarForm searchBarForm,
                              BindingResult bindingResult, Model model, HttpServletRequest request) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate().getValue());

        searchBarForm.prepand(request.getRequestURI());

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummaryObsolete> page = new PageImpl<ReceivableSummaryObsolete>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/gross_profit";
        }

        searchBarForm.setAction("/accounting/gross_profit");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        SumPage<ReceivableSummaryObsolete> page = receivableSummaryService.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);
        return "accounting/gross_profit";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public String summary(@Valid SearchBarForm searchBarForm,
                          BindingResult bindingResult, Model model, HttpServletRequest request) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate().getValue());
        searchBarForm.prepand(request.getRequestURI());
        if (bindingResult.hasErrors()) {
            Page<ReceivableSummaryObsolete> page = new PageImpl<ReceivableSummaryObsolete>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/summary";
        }

        searchBarForm.setAction("/accounting/summary");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        SumPage<ReceivableSummaryObsolete> page = receivableSummaryService.findAll(searchBarForm, pageRequest);

        model.addAttribute("page", page);
        return "accounting/summary";
    }

    @RequestMapping(value = "/receivable", method = RequestMethod.GET)
    public String listReceivables(@Valid SearchBarForm searchBarForm,
                                  BindingResult bindingResult, Model model, HttpServletRequest request) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate().getValue());

        searchBarForm.prepand(request.getRequestURI());

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummaryObsolete> page = new PageImpl<ReceivableSummaryObsolete>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/receivable";
        }

        searchBarForm.setAction("/accounting/receivable");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        SumPage<ReceivableSummaryObsolete> page = receivableSummaryService.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);

        return "accounting/receivable";
    }

    @RequestMapping(value = "/receivable/journal", method = RequestMethod.GET)
    public String listReceivableJournals(Model model) {
        return "accounting/receivable/journal";
    }

    @RequestMapping(value = "/receivable/ledger", method = RequestMethod.GET)
    public String listReceivableLedger(Model model) {
        return "accounting/receivable/ledger";
    }

    @RequestMapping(value = "/payable", method = RequestMethod.GET)
    public String payable(@Valid SearchBarForm searchBarForm,
                          BindingResult bindingResult, Model model, HttpServletRequest request) {
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate().getValue());

        searchBarForm.prepand(request.getRequestURI());

        if (bindingResult.hasErrors()) {
            Page<Charge> page = new PageImpl<Charge>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/payable";
        }

        searchBarForm.setAction("/accounting/payable");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<Charge> page = chargeService.findAll(searchBarForm.getStartDate(), searchBarForm.getEndDate(), null, null, pageRequest);
        model.addAttribute("page", page);
        return "accounting/payable";
    }

    @RequestMapping(value = "/payable/journal", method = RequestMethod.GET)
    public String listPayable(Model model) {
        return "accounting/payable/journal";
    }

    @RequestMapping(value = "/payable/ledger", method = RequestMethod.GET)
    public String listPayableLedger(Model model) {
        return "accounting/payable/ledger";
    }

    @RequestMapping(value = "/ars", method = RequestMethod.GET)
    public String listRevenues(@Valid SearchBarForm searchBarForm, Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivableSummary> page = receivableService.findAll(pageRequest);
        model.addAttribute("page", page);
        searchBarForm.prepand(request.getRequestURI());
        return "accounting/receivable/summary";
    }

    @RequestMapping(value = "/ars/{customerId}", method = RequestMethod.GET)
    public String listRevenues(@PathVariable Long customerId, Model model) {
        List<Order> orderList = orderService.findByCustomerId(customerId);
        List<SalesInvoice> salesInvoiceList = salesInvoiceService.findByCustomerId(customerId);
        List<Receipt> receiptList = receiptService.findByCustomerId(customerId);
        for (int i = salesInvoiceList.size(); i < orderList.size(); i++) {
            SalesInvoice salesInvoice = new SalesInvoice();
            salesInvoice.setAmountCny(BigDecimal.ZERO);
            salesInvoice.setAmountUsd(BigDecimal.ZERO);
            salesInvoiceList.add(salesInvoice);
        }
        for (int i = receiptList.size(); i < orderList.size(); i++) {
            Receipt receipt = new Receipt();
            receipt.setAmountCny(BigDecimal.ZERO);
            receipt.setAmountUsd(BigDecimal.ZERO);
            receiptList.add(receipt);
        }
        ReceivableSummary receivableSummary = receivableService.findByCustomerId(customerId);
        model.addAttribute("orderList", orderList);
        model.addAttribute("salesInvoiceList", salesInvoiceList);
        model.addAttribute("receiptList", receiptList);
        model.addAttribute("invoice", new SalesInvoice());
        model.addAttribute("receipt", new Receipt());
        model.addAttribute("customerId", customerId);
        model.addAttribute("receivableSummary", receivableSummary);
        return "accounting/receivable/details";
    }

    @RequestMapping(value = "/ars/{customerId}/invoices", method = RequestMethod.POST)
    public String addInvoice(@PathVariable Long customerId, SalesInvoice salesInvoice) {
        logger.debug("customerId: {}; salesInvoice: {}", customerId, salesInvoice);
        receivableService.addInvoice(customerId, salesInvoice);
        return "redirect:/accounting/ars/" + customerId;
    }

    @RequestMapping(value = "/ars/{customerId}/receipts", method = RequestMethod.POST)
    public String addReceipt(@PathVariable Long customerId, Receipt receipt) {
        logger.debug("customerId: {}; receipt: {}", customerId, receipt);
        receivableService.addReceipt(customerId, receipt);
        return "redirect:/accounting/ars/" + customerId;
    }

    @RequestMapping(value = "/ars/{customerId}/statements", method = RequestMethod.GET)
    public String listSalesStatements(@PathVariable Long customerId, Model model) {
        logger.debug("customerId: {}", customerId);

        model.addAttribute("customerId", customerId);
        model.addAttribute("statementList", salesStatementService.findSalesStatementByCustomerId(customerId));
        return "accounting/receivable/statements";
    }

    @RequestMapping(value = "/ars/{customerId}/statements/{id}", method = RequestMethod.GET)
    public String getStatement(@PathVariable Long customerId, @PathVariable Long id, Model model) {
        logger.debug("customerId: {}; statementId: {}", customerId, id);

        SalesStatement salesStatement = salesStatementService.find(id);
        List<ServiceSubtype> serviceSubtypeList = serviceSubtypeService.findEnabled();
        List<ServiceSubtype> currencyServiceSubtypeList = new ArrayList<>();
        for (ServiceSubtype serviceSubtype : serviceSubtypeList) {
            ServiceSubtype cnyServiceSubtype = new ServiceSubtype();
            cnyServiceSubtype.setId(serviceSubtype.getId());
            cnyServiceSubtype.setName(serviceSubtype.getName() + "(CNY)");
            currencyServiceSubtypeList.add(cnyServiceSubtype);
            ServiceSubtype usdServiceSubtype = new ServiceSubtype();
            usdServiceSubtype.setId(serviceSubtype.getId() + 1000);
            usdServiceSubtype.setName(serviceSubtype.getName() + "(USD)");
            currencyServiceSubtypeList.add(usdServiceSubtype);
        }

        List<List<ServiceItem>> serviceItemListList = new ArrayList<>();
        Set<Long> serviceSubtypeSet = new HashSet<Long>();
        for (Order order : salesStatement.getOrders()) {
            List<ServiceItem> serviceItemList = new ArrayList<ServiceItem>();
            for (ServiceSubtype serviceSubtype : currencyServiceSubtypeList) {
                boolean exist = false;
                for (ServiceItem serviceItem : order.getServiceItems()) {
                    Long serviceTypeId = serviceItem.getServiceSubtype().getId();
                    if (serviceItem.getCurrency().equals(Currency.USD))
                        serviceTypeId = 1000L + serviceTypeId;
                    if (serviceSubtype.getId().equals(serviceTypeId)) {
                        serviceSubtypeSet.add(serviceSubtype.getId());
                        serviceItemList.add(serviceItem);
                        exist = true;
                    }
                }
                if (!exist) {
                    serviceItemList.add(null);
                }
            }
            serviceItemListList.add(serviceItemList);
        }
        int index = 0;
        Iterator<ServiceSubtype> iterator = currencyServiceSubtypeList.iterator();
        while (iterator.hasNext()) {
            ServiceSubtype serviceSubtype = iterator.next();
            if (!serviceSubtypeSet.contains(serviceSubtype.getId())) {
                iterator.remove();
                for (List serviceItemList : serviceItemListList) {
                    serviceItemList.remove(index);
                }
            } else {
                index++;
            }
        }

        List<ServiceItem> totalServiceItemList = new ArrayList<>(currencyServiceSubtypeList.size());
        BigDecimal cnyTotal = BigDecimal.ZERO;
        BigDecimal usdTotal = BigDecimal.ZERO;
        for (int i = 0; i < currencyServiceSubtypeList.size(); i++) {
            ServiceItem serviceItem = new ServiceItem();
            serviceItem.setQuotation(BigDecimal.ZERO);
            totalServiceItemList.add(serviceItem);
        }
        for (List<ServiceItem> serviceItemList : serviceItemListList) {
            for (int i = 0; i < currencyServiceSubtypeList.size(); i++) {
                ServiceItem totalServiceItem = totalServiceItemList.get(i);
                ServiceItem serviceItem = serviceItemList.get(i);
                // Order could not contain this service item
                if (null != serviceItem) {
                    totalServiceItem.setQuotation(totalServiceItem.getQuotation().add(serviceItem.getQuotation()));
                    if (serviceItemList.get(i).getCurrency().equals(Currency.USD)) {
                        usdTotal = usdTotal.add(serviceItemList.get(i).getQuotation());
                    } else {
                        cnyTotal = cnyTotal.add(serviceItemList.get(i).getQuotation());
                    }
                }
            }
        }

        model.addAttribute("customerId", customerId);
        model.addAttribute("statement", salesStatement);
        model.addAttribute("serviceSubtypeList", currencyServiceSubtypeList);
        model.addAttribute("serviceItemListList", serviceItemListList);
        model.addAttribute("totalServiceItemList", totalServiceItemList);
        model.addAttribute("cnyTotal", cnyTotal);
        model.addAttribute("usdTotal", usdTotal);

        return "accounting/receivable/statement_detail";
    }

    @RequestMapping(value = "/ars/{customerId}/statements/form", method = RequestMethod.GET)
    public String initStatementForm(@PathVariable Long customerId, Model model) {
        logger.debug("customerId: {}", customerId);
        List<Order> orderList = orderService.findByCustomerIdLessThanStatus(customerId, OrderStatus.PAID.getValue());
        model.addAttribute("orderList", orderList);
        return "accounting/receivable/statement_form";
    }

    @RequestMapping(value = "/ars/{customerId}/statements", method = RequestMethod.POST)
    public String addStatement(@PathVariable Long customerId, Long[] orderIds) {
        logger.debug("customerId: {}; orderIds: {}", customerId, orderIds);

        salesStatementService.save(customerId, orderIds);

        return "redirect:/accounting/ars/" + customerId + "/statements";
    }

    @RequestMapping(value = "/aps", method = RequestMethod.GET)
    public String listAps(@Valid SearchBarForm searchBarForm, Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<PayableSummary> page = payableSummaryService.findAll(pageRequest);
        model.addAttribute("page", page);
        searchBarForm.prepand(request.getRequestURI());
        return "accounting/payable/summary";
    }

    @RequestMapping(value = "/aps/{serviceProviderId}", method = RequestMethod.GET)
    public String listAps(@Valid SearchBarForm searchBarForm, @PathVariable Long serviceProviderId,
                          Model model, HttpServletRequest request) {
        List<Purchase> purchaseList = purchaseService.findByServiceProviderId(serviceProviderId, searchBarForm);
        List<PurchaseInvoice> purchaseInvoiceList = purchaseInvoiceService.findByServiceProviderId(serviceProviderId);
        List<Payment> paymentList = paymentService.findByServiceProviderId(serviceProviderId);
        for (int i = purchaseInvoiceList.size(); i < purchaseList.size(); i++) {
            PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
            purchaseInvoice.setAmountCny(BigDecimal.ZERO);
            purchaseInvoice.setAmountUsd(BigDecimal.ZERO);
            purchaseInvoiceList.add(purchaseInvoice);
        }
        for (int i = paymentList.size(); i < purchaseList.size(); i++) {
            Payment payment = new Payment();
            payment.setAmountCny(BigDecimal.ZERO);
            payment.setAmountUsd(BigDecimal.ZERO);
            paymentList.add(payment);
        }

        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("purchaseInvoiceList", purchaseInvoiceList);
        model.addAttribute("paymentList", paymentList);
        model.addAttribute("invoice", new PurchaseInvoice());
        model.addAttribute("payment", new Payment());
        model.addAttribute("serviceProviderId", serviceProviderId);

        PayableSummary payableSummary = payableSummaryService.findByServiceProviderId(serviceProviderId);
        model.addAttribute("payableSummary", payableSummary);

        searchBarForm.prepand(request.getRequestURI());
        return "accounting/payable/details";
    }

    @RequestMapping(value = "/aps/{serviceProviderId}/invoices", method = RequestMethod.POST)
    public String addInvoice(@PathVariable Long serviceProviderId, PurchaseInvoice purchaseInvoice) {
        logger.debug("serviceProviderId: {}; purchaseInvoice: {}", serviceProviderId, purchaseInvoice);
        payableSummaryService.addInvoice(serviceProviderId, purchaseInvoice);
        return "redirect:/accounting/aps/" + serviceProviderId;
    }

    @RequestMapping(value = "/aps/{serviceProviderId}/payments", method = RequestMethod.POST)
    public String addPayment(@PathVariable Long serviceProviderId, Payment payment) {
        logger.debug("serviceProviderId: {}; payment: {}", serviceProviderId, payment);
        payableSummaryService.addPayment(serviceProviderId, payment);
        return "redirect:/accounting/aps/" + serviceProviderId;
    }

    @RequestMapping(value = "/aps/{serviceProviderId}/statements", method = RequestMethod.GET)
    public String listPurchaseStatements(@PathVariable Long serviceProviderId, Model model) {
        logger.debug("serviceProviderId: {}", serviceProviderId);

        model.addAttribute("serviceProviderId", serviceProviderId);
        model.addAttribute("statementList", purchaseStatementService.findStatementByServiceProviderId(serviceProviderId));
        return "accounting/payable/statements";
    }

    @RequestMapping(value = "/aps/{serviceProviderId}/statements", method = RequestMethod.POST)
    public String addPayableStatement(@PathVariable Long serviceProviderId, Long[] purchaseIds) {
        logger.debug("serviceProviderId: {}; purchaseIds: {}", serviceProviderId, purchaseIds);
        purchaseStatementService.save(serviceProviderId, purchaseIds);
        return "redirect:/accounting/aps/" + serviceProviderId + "/statements";
    }

    @RequestMapping(value = "/aps/{serviceProviderId}/statements/{id}", method = RequestMethod.GET)
    public String getPayableStatement(@PathVariable Long serviceProviderId, @PathVariable Long id, Model model) {
        logger.debug("serviceProviderId: {}; statementId: {}", serviceProviderId, id);

        PurchaseStatement purchaseStatement = purchaseStatementService.find(id);

        model.addAttribute("statement", purchaseStatement);
        return "accounting/payable/statement_detail";
    }
}
