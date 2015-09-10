package liquid.accounting.controller;

import liquid.accounting.domain.PurchaseInvoice;
import liquid.accounting.service.InternalPurchaseInvoiceService;
import liquid.core.controller.BaseController;
import liquid.core.model.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Tao Ma on 9/7/15.
 */
@Controller
@RequestMapping("/invoices")
public class PurchaseInvoiceController extends BaseController {

    @Autowired
    private InternalPurchaseInvoiceService purchaseInvoiceService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@ModelAttribute SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<PurchaseInvoice> page = purchaseInvoiceService.find(pageRequest);
        model.addAttribute("page", page);
        return "invoice/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String enter(Model model) {

        return "invoice/panel";
    }
}
