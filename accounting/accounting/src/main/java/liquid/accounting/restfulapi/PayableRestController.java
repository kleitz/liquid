package liquid.accounting.restfulapi;

import liquid.accounting.domain.Payable;
import liquid.accounting.service.PayableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tao Ma on 6/7/15.
 */
@Controller
@RequestMapping("/api/payable")
public class PayableRestController {
    private static final Logger logger = LoggerFactory.getLogger(PayableRestController.class);

    @Autowired
    private PayableService payableService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET, params = "chargeId")
    @ResponseBody
    public Iterable<Payable> findByChargeId(@RequestParam Long chargeId) {
        Iterable<Payable> journals = payableService.findByChargeId(chargeId);
        return journals;
    }

    @RequestMapping(method = RequestMethod.GET, params = "spId")
    @ResponseBody
    public Iterable<Payable> listReceivableJournalsByCustomer(@RequestParam Long spId) {
        Iterable<Payable> journals = payableService.findByChargeSpId(spId);
        return journals;
    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void addPayable(@RequestBody Payable payable) {
        logger.debug("Payable: {}", payable);
        payableService.save(payable);
    }
}
