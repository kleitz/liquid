package liquid.accounting.restfulapi;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.accounting.service.InternalCashReceiptsJournalService;
import liquid.core.model.SearchBarForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setType("order");
        searchBarForm.setText(String.valueOf(orderId));
        Iterable<CashReceiptsJournal> journals = cashReceiptsJournalService.findAll(searchBarForm);

        journals = cashReceiptsJournalService.findByOrderId(orderId);
        return journals;
    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    @ResponseBody
    public void addReceivableJournals(CashReceiptsJournal cashReceiptsJournal) {
        logger.debug("CashReceiptsJournal: {}", cashReceiptsJournal);
        cashReceiptsJournalService.save(cashReceiptsJournal);
    }
}
