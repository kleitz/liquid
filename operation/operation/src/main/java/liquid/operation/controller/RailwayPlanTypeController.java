package liquid.operation.controller;

import liquid.operation.domain.RailPlanType;
import liquid.operation.service.RailwayPlanTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Controller
@RequestMapping("/railway_plan_type")
public class RailwayPlanTypeController {
    private final static String ROOT_DIR = "operation/railway_plan_type/";

    private static final Logger logger = LoggerFactory.getLogger(RailwayPlanTypeController.class);

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Iterable<RailPlanType> railwayPlanTypes = railwayPlanTypeService.findAll();

        model.addAttribute("railwayPlanTypes", railwayPlanTypes);
        return ROOT_DIR + "list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("railwayPlanType", new RailPlanType());
        return ROOT_DIR + "form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("railwayPlanType", railwayPlanTypeService.find(id));
        return ROOT_DIR + "form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute("railwayPlanType") RailPlanType railwayPlanType) {
        logger.debug("RailwayPlanTypeEntity: {}", railwayPlanType);

        railwayPlanTypeService.save(railwayPlanType);
        return "redirect:/railway_plan_type";
    }
}
