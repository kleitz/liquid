package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.container.domain.Container;
import liquid.container.domain.ContainerType;
import liquid.container.domain.Containers;
import liquid.container.facade.ContainerFacade;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.service.ContainerService;
import liquid.container.service.ContainerSubtypeService;
import liquid.domain.*;
import liquid.facade.ContainerAllocationFacade;
import liquid.metadata.ChargeWay;
import liquid.metadata.ContainerCap;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.order.service.ServiceItemService;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ChargeService;
import liquid.service.LocationService;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.ShippingContainerEntity;
import liquid.shipping.service.RouteService;
import liquid.shipping.service.ShippingContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Container allocation controller.
 * User: tao
 * Date: 10/3/13
 * Time: 2:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/allocation")
public class AllocationController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContainerFacade containerFacade;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ContainerAllocationFacade containerAllocationFacade;

    public String init(@PathVariable String taskId, Model model) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        scService.initialize(orderId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("containerTypeMap", ContainerCap.toMap());
        model.addAttribute("routes", routes);

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "allocation/main";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @RequestParam(required = false, defaultValue = "0") long routeId,
                       @RequestParam(required = false, defaultValue = "0") long ownerId,
                       @RequestParam(required = false, defaultValue = "0") long yardId,
                       Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        List<RouteContainerAllocation> routeContainerAllocations = containerAllocationFacade.computeContainerAllocation(order);
        model.addAttribute("routeContainerAllocations", routeContainerAllocations);

        if (ContainerType.RAIL.getType() == order.getContainerType())
            return "allocation/rail_container";
        else {
            // Owner list
            List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();

            // Yard list
            List<LocationEntity> yards = locationService.findYards();

            model.addAttribute("ownerId", ownerId);
            model.addAttribute("owners", owners);

            model.addAttribute("yardId", yardId);
            model.addAttribute("yards", yards);

            model.addAttribute("routeId", routeId);
            return "allocation/self_container";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId, RouteContainerAllocation routeContainerAllocation, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", routeContainerAllocation);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        containerAllocationFacade.allocate(routeContainerAllocation);
        if (ContainerType.RAIL.getType() == routeContainerAllocation.getType()) {
            model.addAttribute("containerAllocation", containerAllocationFacade.computeContainerAllocation(orderId));
            model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
            return "allocation/rail_container";
        } else {
            return "redirect:/task/" + taskId + "/allocation";
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = "routeId")
    public String initContainerList(@PathVariable String taskId,
                                    @RequestParam Long routeId,
                                    @RequestParam(required = false, defaultValue = "0") Long ownerId,
                                    @RequestParam(required = false, defaultValue = "0") Long yardId,
                                    @RequestParam(required = false, defaultValue = "0") Integer number,
                                    Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();

        RouteEntity routeEntity = routeService.find(routeId);
        routeContainerAllocation.setRoute(routeEntity);
        routeContainerAllocation.setType(routeEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(routeEntity.getOrder().getContainerSubtype().getName(), routeEntity);
        routeContainerAllocation.setContainerAllocations(containerAllocations);

        int size = 10;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findAll(routeEntity.getOrder().getContainerSubtype().getId(),
                ownerId, yardId, pageRequest);

        // Owner list
        List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();

        // Yard list
        List<LocationEntity> yards = locationService.findYards();

        model.addAttribute("taskId", taskId);
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        model.addAttribute("owners", owners);
        model.addAttribute("yards", yards);

        model.addAttribute("ownerId", ownerId);
        model.addAttribute("yardId", yardId);

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/task/" + taskId + "/allocation?" + "routeId=" + routeId + "&ownerId=" + ownerId + "&yardId=" + yardId + "&");

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setRouteId(routeId);
        model.addAttribute("selfContainerAllocation", selfContainerAllocation);
        return "allocation/list";
    }

    @RequestMapping(method = RequestMethod.POST, params = "self")
    public String allocateInList(@PathVariable String taskId, SelfContainerAllocation selfContainerAllocation, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", selfContainerAllocation);
        containerAllocationFacade.allocate(selfContainerAllocation);
        return "redirect:/task/" + taskId + "/allocation/list?routeId=" + selfContainerAllocation.getRouteId();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "routeId")
    public String initContainerSearch(@PathVariable String taskId,
                                      @RequestParam Long routeId,
                                      Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
        RouteEntity routeEntity = routeService.find(routeId);
        routeContainerAllocation.setRoute(routeEntity);
        routeContainerAllocation.setType(routeEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(routeEntity.getOrder().getContainerSubtype().getName(), routeEntity);
        routeContainerAllocation.setContainerAllocations(containerAllocations);

        model.addAttribute("taskId", taskId);
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        SearchContainerAllocForm searchContainerAllocForm = new SearchContainerAllocForm();
        searchContainerAllocForm.setRouteId(routeId);
        model.addAttribute("searchContainerAllocForm", searchContainerAllocForm);
        return "allocation/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String allocateInSearch(@PathVariable String taskId, SearchContainerAllocForm searchContainerAllocForm) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", searchContainerAllocForm);
        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setRouteId(searchContainerAllocForm.getRouteId());
        selfContainerAllocation.setContainerIds(new Long[]{searchContainerAllocForm.getContainerId()});
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/search?routeId=" + searchContainerAllocForm.getRouteId();
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET, params = "routeId")
    public String initContainerEnter(@PathVariable String taskId,
                                     @RequestParam Long routeId,
                                     Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();

        RouteEntity routeEntity = routeService.find(routeId);
        routeContainerAllocation.setRoute(routeEntity);
        routeContainerAllocation.setType(routeEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(routeEntity.getOrder().getContainerSubtype().getName(), routeEntity);
        routeContainerAllocation.setContainerAllocations(containerAllocations);

        model.addAttribute("taskId", taskId);
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        // Owner list
        List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<LocationEntity> yards = locationService.findYards();
        // container subtypes
        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);

        model.addAttribute("owners", owners);
        model.addAttribute("locations", yards);
        model.addAttribute("containerSubtypes", subtypes);

        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        for (int i = 0; i < 5; i++) {
            containers.getList().add(new Container());
        }
        containers.setRouteId(routeId);
        model.addAttribute("containers", containers);
        return "allocation/enter";
    }

    @RequestMapping(value = "/enter", method = RequestMethod.POST)
    public String allocateInEnter(@PathVariable String taskId, @ModelAttribute(value = "containers") EnterContainerAllocForm containers) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", containers);

        Iterable<ContainerEntity> containerEntities = containerFacade.enter(containers);
        List<Long> containerIds = new ArrayList<>();
        for (ContainerEntity containerEntity : containerEntities) {
            containerIds.add(containerEntity.getId());
        }

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setRouteId(containers.getRouteId());

        selfContainerAllocation.setContainerIds(containerIds.toArray(new Long[0]));
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/enter?routeId=" + containers.getRouteId();
    }

    @RequestMapping(method = RequestMethod.POST, params = "undo")
    public String undo(@PathVariable String taskId,
                       @RequestParam long allocationId) {
        logger.debug("taskId: {}", taskId);
        logger.debug("allocationId: {}", allocationId);
        containerAllocationFacade.undo(allocationId);
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(value = "/route/{routeId}/sc/{scId}", method = RequestMethod.GET)
    public String initAllocation(@PathVariable String taskId,
                                 @PathVariable long routeId,
                                 @PathVariable long scId,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        ShippingContainerEntity sc = scService.find(scId);

        // Set up pickup contact and his phone by default
        RouteEntity route = routeService.find(routeId);
        Collection<ShippingContainerEntity> scs = route.getContainers();
        if (scs.iterator().hasNext()) {
            ShippingContainerEntity firstOne = scs.iterator().next();
            sc.setPickupContact(firstOne.getPickupContact());
            sc.setContactPhone(firstOne.getContactPhone());
        }

        model.addAttribute("sc", sc);
        model.addAttribute("routeId", routeId);
        model.addAttribute("containers", containerService.findAllInStock(route.getOrder().getContainerType()));
        return "allocation/allocating";
    }

    @RequestMapping(value = "/route/{routeId}/sc", method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId,
                           @PathVariable long routeId,
                           ShippingContainerEntity sc,
                           BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        if (bindingResult.hasErrors()) {
            return "allocation/allocating";
        }

        scService.allocate(routeId, sc);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("routes", routes);
        return "redirect:/task/" + taskId + "/allocation";
    }

    // TODO: change to form submit.
//    @RequestMapping(value = "/route/{routeId}/sc/{scId}", method = RequestMethod.GET)
//    public String remove(@PathVariable String taskId,
//                         @PathVariable long routeId,
//                         @PathVariable long scId,
//                         Model model, Principal principal) {
//        logger.debug("taskId: {}", taskId);
//        logger.debug("routeId: {}", routeId);
//
//        scService.remove(scId);
//
//        Collection<Route> routes = routeService.findByTaskId(taskId);
//        model.addAttribute("routes", routes);
//        return "redirect:/task/" + taskId + "/allocation";
//    }
}
