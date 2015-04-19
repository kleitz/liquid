package liquid.process.controller;

import liquid.process.handler.DefinitionKey;
import liquid.transport.model.Shipment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Tao Ma on 4/16/15.
 * FIXME -
 */
@Controller
public class PlanRouteController extends PlanShipmentController {
    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.planRoute)
    public String addShipment(@PathVariable String taskId, @Valid @ModelAttribute(value = "shipment") Shipment shipment,
                              BindingResult result, Model model) {
        return super.addShipment(taskId, shipment, result, model);
    }
}
