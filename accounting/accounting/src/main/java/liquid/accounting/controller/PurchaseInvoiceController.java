package liquid.accounting.controller;

import liquid.accounting.service.InternalPurchaseInvoiceService;
import liquid.core.controller.BaseController;
import liquid.core.model.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
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
    public String list(@ModelAttribute SearchBarForm searchBarForm) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        return "invoice/list";
    }
}
