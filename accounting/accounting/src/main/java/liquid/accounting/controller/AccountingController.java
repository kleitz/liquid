package liquid.accounting.controller;

import liquid.accounting.domain.*;
import liquid.accounting.service.*;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import liquid.operation.domain.Customer;
import liquid.operation.service.CustomerService;
import liquid.order.domain.Order;
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
    private InternalReceivableSummaryService receivableSummaryService;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ReceiptService receiptService;

    @RequestMapping(value = "/gross_profit", method = RequestMethod.GET)
    public String grossProfit(@Valid SearchBarForm searchBarForm,
                              BindingResult bindingResult, Model model, HttpServletRequest request) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate().getValue());

        searchBarForm.prepand(request.getRequestURI());

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/gross_profit";
        }

        searchBarForm.setAction("/accounting/gross_profit");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        SumPage<ReceivableSummary> page = receivableSummaryService.findAll(searchBarForm, pageRequest);
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
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/summary";
        }

        searchBarForm.setAction("/accounting/summary");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        SumPage<ReceivableSummary> page = receivableSummaryService.findAll(searchBarForm, pageRequest);

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
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/receivable";
        }

        searchBarForm.setAction("/accounting/receivable");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        SumPage<ReceivableSummary> page = receivableSummaryService.findAll(searchBarForm, pageRequest);
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

    @RequestMapping(value = "/revenues", method = RequestMethod.GET)
    public String listRevenues(@Valid SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<Revenue> page = revenueService.findAll(pageRequest);
        model.addAttribute("page", page);
        return "accounting/revenue";
    }

    @RequestMapping(value = "/revenues/{customerId}", method = RequestMethod.GET)
    public String listRevenues(@PathVariable Long customerId, Model model) {
        List<Order> orderList = orderService.findByCustomerId(customerId);
        List<Invoice> invoiceList = invoiceService.findByCustomerId(customerId);
        List<Receipt> receiptList = receiptService.findByCustomerId(customerId);
        for(int i = invoiceList.size(); i < orderList.size(); i++){
            invoiceList.add(new Invoice());
        }
        for(int i = receiptList.size(); i < orderList.size(); i++){
            receiptList.add(new Receipt());
        }
        Revenue revenue = revenueService.findByCustomerId(customerId);
        model.addAttribute("orderList", orderList);
        model.addAttribute("invoiceList", invoiceList);
        model.addAttribute("receiptList", receiptList);
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("receipt", new Receipt());
        model.addAttribute("customerId", customerId);
        model.addAttribute("revenue", revenue);
        return "accounting/receivable/details";
    }

    @RequestMapping(value = "/revenues/{customerId}/invoices", method = RequestMethod.POST)
    public String addInvoice(@PathVariable Long customerId, Invoice invoice) {
        logger.debug("customerId: {}; invoice: {}", customerId, invoice);
        revenueService.addInvoice(customerId, invoice);
        return "redirect:/accounting/revenues/" + customerId;
    }

    @RequestMapping(value = "/revenues/{customerId}/receipts", method = RequestMethod.POST)
    public String addReceipt(@PathVariable Long customerId, Receipt receipt) {
        logger.debug("customerId: {}; receipt: {}", customerId, receipt);
        revenueService.addReceipt(customerId, receipt);
        return "redirect:/accounting/revenues/" + customerId;
    }
}
