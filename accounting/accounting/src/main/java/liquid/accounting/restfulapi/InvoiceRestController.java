package liquid.accounting.restfulapi;

import liquid.accounting.domain.PurchaseInvoice;
import liquid.accounting.service.InternalPurchaseInvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

/**
 * Created by Tao Ma on 9/9/15.
 */
@Controller
@RequestMapping("/api/invoices")
public class InvoiceRestController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceRestController.class);

    @Autowired
    private InternalPurchaseInvoiceService purchaseInvoiceService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterator<PurchaseInvoice> findAll() {
        PageRequest pageRequest = new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "id"));
        Iterable<PurchaseInvoice> journals = purchaseInvoiceService.find(pageRequest);
        return journals.iterator();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public PurchaseInvoice addPurchaseInvoice(@RequestBody PurchaseInvoice purchaseInvoice) {
        logger.debug("Purchase Invoice: {}", purchaseInvoice);
        return purchaseInvoiceService.save(purchaseInvoice);
    }
}
