package liquid.accounting.restfulapi;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.accounting.service.InternalCashReceiptsJournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tao Ma on 5/7/15.
 */
@Controller
@RequestMapping("/api/receivable")
public class ReceivableRestController {
    private static final Logger logger = LoggerFactory.getLogger(ReceivableRestController.class);

    @Autowired
    private InternalCashReceiptsJournalService cashReceiptsJournalService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET, params = "orderId")
    @ResponseBody
    public Iterable<CashReceiptsJournal> listReceivableJournalsByOrder(@RequestParam Long orderId) {
        Iterable<CashReceiptsJournal> journals = cashReceiptsJournalService.findByOrderId(orderId);
        return journals;
    }

    @RequestMapping(method = RequestMethod.GET, params = "customerId")
    @ResponseBody
    public Iterable<CashReceiptsJournal> listReceivableJournalsByCustomer(@RequestParam Long customerId) {
        Iterable<CashReceiptsJournal> journals = cashReceiptsJournalService.findByCustomerId(customerId);
        return journals;
    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void addReceivableJournals(@RequestBody CashReceiptsJournal cashReceiptsJournal) {
        logger.debug("CashReceiptsJournal: {}", cashReceiptsJournal);
        cashReceiptsJournalService.save(cashReceiptsJournal);
    }
}
