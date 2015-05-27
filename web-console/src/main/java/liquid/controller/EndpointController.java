package liquid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Tao Ma on 5/27/15.
 */
@Controller
public class EndpointController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @RequestMapping(value = "/endpoint", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("handlerMethods", requestMappingHandlerMapping.getHandlerMethods());
        return "endpoint";
    }
}
