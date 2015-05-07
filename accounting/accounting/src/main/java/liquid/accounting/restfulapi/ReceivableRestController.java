package liquid.accounting.restfulapi;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.accounting.service.InternalCashReceiptsJournalService;
import liquid.core.model.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Tao Ma on 5/7/15.
 */
@Controller
@RequestMapping("/api/receivable")
public class ReceivableRestController {
    @Autowired
    private InternalCashReceiptsJournalService cashReceiptsJournalService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET, params = "orderId")
    public Iterable<CashReceiptsJournal> listReceivableJournalsByOrder(@RequestParam Long orderId, Model model) {
        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setType("order");
        searchBarForm.setText(String.valueOf(orderId));
        Iterable<CashReceiptsJournal> journals = cashReceiptsJournalService.findAll(searchBarForm);
        return journals;
    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    public void addReceivableJournals(CashReceiptsJournal cashReceiptsJournal) {
        cashReceiptsJournalService.save(cashReceiptsJournal);
    }
}
