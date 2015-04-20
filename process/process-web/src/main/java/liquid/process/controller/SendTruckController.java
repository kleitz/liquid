package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.operation.service.ServiceProviderService;
import liquid.process.domain.Task;
import liquid.process.handler.SendTruckHandler;
import liquid.process.model.SendingTruckForm;
import liquid.process.service.TaskService;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.TruckEntity;
import liquid.transport.model.TruckForm;
import liquid.transport.service.TruckService;
import liquid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/16/15.
 */
@Controller
public class SendTruckController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(SendTruckController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TruckService truckService;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + SendTruckHandler.TASK_DEFINITION_KEY)
    public String fillIn(@PathVariable String taskId,
                         @Valid @ModelAttribute SendingTruckForm sendingTruckForm, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        logger.debug("task: {}", task);

        if (bindingResult.hasErrors()) {
            model.addAttribute("sendingTruckForm", sendingTruckForm);
            model.addAttribute("sps", serviceProviderService.findByType(4L));

            model.addAttribute("task", task);
            return "task/sendTruck/init";
        }

        List<TruckEntity> truckEntityList = new ArrayList<>();
        for (TruckForm truck : sendingTruckForm.getTruckList()) {
            TruckEntity truckEntity = convert(truck);
            truckEntityList.add(truckEntity);
        }
        truckService.save(truckEntityList);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/task/" + taskId;
    }

    // FIXME - use Fomatter instead.
    private TruckEntity convert(TruckForm truck) {
        TruckEntity entity = new TruckEntity();
        entity.setId(truck.getId());
        entity.setShipment(ShipmentEntity.newInstance(ShipmentEntity.class, truck.getShipmentId()));
        entity.setPickingAt(DateUtil.dateOf(truck.getPickingAt()));
        entity.setServiceProviderId(truck.getServiceProviderId());
        entity.setLicensePlate(truck.getLicensePlate());
        entity.setDriver(truck.getDriver());
        return entity;
    }
}
