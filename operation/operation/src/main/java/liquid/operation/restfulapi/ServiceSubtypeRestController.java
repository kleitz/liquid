package liquid.operation.restfulapi;

import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tao Ma on 6/9/15.
 */
@Controller
@RequestMapping("/api/service-subtype")
public class ServiceSubtypeRestController {

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<ServiceSubtype> findAll() {
        return serviceSubtypeService.findEnabled();
    }
}
