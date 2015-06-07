package liquid.accounting.restfulapi;

import liquid.accounting.domain.Payable;
import liquid.accounting.service.PayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tao Ma on 6/7/15.
 */
@Controller
@RequestMapping("/api/payable")
public class PayableRestController {

    @Autowired
    private PayableService payableService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET, params = "chargeId")
    @ResponseBody
    public Iterable<Payable> findByChargeId(@RequestParam Long chargeId) {
        Iterable<Payable> journals = payableService.findByChargeId(chargeId);
        return journals;
    }
}
