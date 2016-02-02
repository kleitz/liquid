package liquid.container.restfulapi;

import liquid.container.domain.ContainerSubtype;
import liquid.container.service.ContainerSubtypeService;
import org.hibernate.engine.internal.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * Created by Tao Ma on 5/20/15.
 */
@Controller
@RequestMapping("/api/container-subtype")
public class ContainerSubtypeRestController {
    private static final Logger logger = LoggerFactory.getLogger(ContainerSubtypeRestController.class);

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @RequestMapping
    @ResponseBody
    public Iterable<ContainerSubtype> findAll() {
        return containerSubtypeService.findAll();
    }
}
