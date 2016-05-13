package liquid.order.restfulapi;

import liquid.order.service.ReceivingOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mat on 2/21/16.
 */
@Deprecated
@Controller
@RequestMapping("/api/pickup_order")
public class PickupOrderRestController {

    @Autowired
    private ReceivingOrderServiceImpl recvOrderService;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        recvOrderService.delete(id);
    }
}
