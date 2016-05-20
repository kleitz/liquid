package liquid.accounting.controller;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.domain.Revenue;
import liquid.accounting.service.ChargeService;
import liquid.accounting.service.ExchangeRateService;
import liquid.accounting.service.InternalReceivableSummaryService;
import liquid.accounting.service.RevenueService;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import liquid.order.domain.TradeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Controller
@RequestMapping("/accounting")
public class AccountingController {
    private static final int size = 20;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private InternalReceivableSummaryService receivableSummaryService;

    @Autowired
    private RevenueService revenueService;

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
}
