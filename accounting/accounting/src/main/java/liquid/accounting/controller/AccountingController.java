package liquid.accounting.controller;

import liquid.accounting.domain.*;
import liquid.accounting.service.*;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import liquid.operation.service.CustomerService;
import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.Order;
import liquid.order.domain.OrderStatus;
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
import java.util.ArrayList;
import java.util.List;

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
    private AccountingService accountingService;


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
    public String listRevenues(@Valid SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivableSummary> page = receivableService.findAll(pageRequest);
        model.addAttribute("page", page);
        return "accounting/receivable/summary";
    }

    @RequestMapping(value = "/ars/{customerId}", method = RequestMethod.GET)
    public String listRevenues(@PathVariable Long customerId, Model model) {
        List<Order> orderList = orderService.findByCustomerId(customerId);
        List<SalesInvoice> salesInvoiceList = salesInvoiceService.findByCustomerId(customerId);
        List<Receipt> receiptList = receiptService.findByCustomerId(customerId);
        for (int i = salesInvoiceList.size(); i < orderList.size(); i++) {
            salesInvoiceList.add(new SalesInvoice());
        }
        for (int i = receiptList.size(); i < orderList.size(); i++) {
            receiptList.add(new Receipt());
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
    public String listStatements(@PathVariable Long customerId, Model model) {
        logger.debug("customerId: {}", customerId);

        model.addAttribute("customerId", customerId);
        model.addAttribute("statementList", accountingService.findSalesStatementByCustomerId(customerId));
        return "accounting/receivable/statements";
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

        accountingService.save(customerId, orderIds);

        return "redirect:/accounting/ars/" + customerId + "/statements";
    }

    @RequestMapping(value = "/aps", method = RequestMethod.GET)
    public String listAps(@Valid SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<PayableSummary> page = payableSummaryService.findAll(pageRequest);
        model.addAttribute("page", page);
        return "accounting/payable/summary";
    }

    @RequestMapping(value = "/aps/{serviceProviderId}", method = RequestMethod.GET)
    public String listAps(@PathVariable Long serviceProviderId, Model model) {
        List<Purchase> purchaseList = purchaseService.findByServiceProviderId(serviceProviderId);
        List<PurchaseInvoice> purchaseInvoiceList = purchaseInvoiceService.findByServiceProviderId(serviceProviderId);
        List<Payment> paymentList = paymentService.findByServiceProviderId(serviceProviderId);
        for (int i = purchaseInvoiceList.size(); i < purchaseList.size(); i++) {
            purchaseInvoiceList.add(new PurchaseInvoice());
        }
        for (int i = paymentList.size(); i < purchaseList.size(); i++) {
            paymentList.add(new Payment());
        }
        PayableSummary payableSummary = payableSummaryService.findByServiceProviderId(serviceProviderId);
        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("purchaseInvoiceList", purchaseInvoiceList);
        model.addAttribute("paymentList", paymentList);
        model.addAttribute("invoice", new PurchaseInvoice());
        model.addAttribute("payment", new Payment());
        model.addAttribute("serviceProviderId", serviceProviderId);
        model.addAttribute("payableSummary", payableSummary);
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
}
