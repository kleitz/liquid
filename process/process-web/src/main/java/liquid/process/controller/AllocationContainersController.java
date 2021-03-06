package liquid.process.controller;

import liquid.accounting.domain.ChargeWay;
import liquid.accounting.domain.Purchase;
import liquid.accounting.service.PurchaseService;
import liquid.container.domain.ContainerEntity;
import liquid.container.domain.ContainerSubtype;
import liquid.container.domain.ContainerType;
import liquid.container.model.Container;
import liquid.container.model.EnterContainerAllocForm;
import liquid.container.service.ContainerService;
import liquid.container.service.ContainerSubtypeService;
import liquid.core.model.Alert;
import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.LocationService;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.order.service.ServiceItemService;
import liquid.transport.domain.RailContainer;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.TransMode;
import liquid.transport.domain.Truck;
import liquid.transport.facade.ContainerAllocationFacade;
import liquid.transport.model.*;
import liquid.transport.service.RailContainerService;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import liquid.transport.service.TruckService;
import liquid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Container allocation controller.
 * FIXME - This is multiple operations task, we need a more better solution for this kind of tasks.
 * User: tao
 * Date: 10/3/13
 * Time: 2:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/allocation")
public class AllocationContainersController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationContainersController.class);

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Autowired
    private ContainerAllocationFacade containerAllocationFacade;

    @Autowired
    private TruckService truckService;

    @Autowired
    private RailContainerService railContainerService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);
        model.addAttribute("order",order);

        model.addAttribute("chargeWays", ChargeWay.values());
        List<Purchase> purchases = purchaseService.findByOrderId(order.getId());
        model.addAttribute("purchases", purchases);

        Purchase purchase = new Purchase();
        purchase.setOrder(order);
        purchase.setTaskId(taskId);
        purchase.setTransportMode(TransMode.VESSEL.getType());
        model.addAttribute("purchase", purchase);

        model.addAttribute("containerQuantity", order.getContainerQty());

        model.addAttribute("transportModeOptions", TransMode.values());
        model.addAttribute("transModes", TransMode.toMap());

        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);

        Iterable<ServiceProvider> sps = serviceProviderService.findAll();
        model.addAttribute("sps", sps);

        model.addAttribute("chargeWays", ChargeWay.values());

        return "task/allocateContainers/init";
    }

    @Deprecated
    public String init0(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);
        List<ShipmentContainerAllocation> shipmentContainerAllocations = containerAllocationFacade.computeContainerAllocation(order);

        model.addAttribute("shipmentContainerAllocations", shipmentContainerAllocations);

        if (ContainerType.RAIL.getType() == order.getContainerType()) {
            return "task/allocateContainers/rail_container";
        } else {
            List<Truck> trucks = new ArrayList<>();

            Iterable<ShipmentEntity> shipmentEntities = shipmentService.findByOrderId(orderId);
            for (ShipmentEntity shipmentEntity : shipmentEntities) {
                Iterable<Truck> truckEntityIterable = truckService.findByShipmentId(shipmentEntity.getId());
                List<Truck> truckFormsForShipment = new ArrayList<>();
                for (Truck truck : truckEntityIterable) {
                    truckFormsForShipment.add(truck);
                }

                trucks.addAll(truckFormsForShipment);
            }

            model.addAttribute("shipmentContainerAllocation", new ShipmentContainerAllocation());
            model.addAttribute("truckForms", trucks);
            return "task/allocateContainers/self_container";
        }
    }

    @RequestMapping(value = "/rail", method = RequestMethod.GET, params = "shipmentId")
    public String initRailAlloc(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        model.addAttribute("taskId", taskId);

        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        ShipmentContainerAllocation shipmentContainerAllocation = containerAllocationFacade.getShipmentContainerAllocation(
                ContainerType.RAIL.getType(), shipmentEntity.getOrder().getContainerSubtype().getName(), shipmentEntity);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        Iterable<ContainerSubtype> subtypes = containerSubtypeService.findByContainerType(ContainerType.RAIL);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setShipmentId(shipmentId);

        // For the allocation form
        model.addAttribute("containerSubtypes", subtypes);
        return "task/allocateContainers/rail_enter";
    }

    /**
     *
     * @param taskId
     * @param order is used for wrapper of containers here.
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId,
                           Order order,
                           RedirectAttributes redirectAttributes) {
        logger.debug("TaskId: {}; Containers: {}", taskId, order);
        Order originalOrder = orderService.find(order.getId());
        originalOrder.setContainers(order.getContainers());
        orderService.save(originalOrder);
        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/task/" + taskId + "/allocation";
    }

    @Deprecated
    public String allocate0(@PathVariable String taskId,
                           ShipmentContainerAllocation shipmentContainerAllocation, Model model,
                           RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentContainerAllocations: {}", shipmentContainerAllocation);

        containerAllocationFacade.allocate(shipmentContainerAllocation);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("shipmentContainerAllocations", containerAllocationFacade.computeContainerAllocation(orderId));

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = "shipmentId")
    public String initContainerList(@PathVariable String taskId, @RequestParam Long shipmentId,
                                    @RequestParam(required = false, defaultValue = "0") Long ownerId,
                                    @RequestParam(required = false, defaultValue = "0") Long yardId,
                                    @RequestParam(required = false, defaultValue = "0") Integer number,
                                    Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        shipmentContainerAllocation.setShipment(shipmentEntity);
        shipmentContainerAllocation.setShipmentId(shipmentId);
        shipmentContainerAllocation.setType(shipmentEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(shipmentEntity.getOrder().getContainerSubtype().getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        model.addAttribute("taskId", taskId);

        int size = 5;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findAll(shipmentEntity.getOrder().getContainerSubtype().getId(),
                ownerId, yardId, pageRequest);
        // Owner list
        List<ServiceProvider> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<Location> yards = locationService.findYards();
        // For showing containers in stock.
        model.addAttribute("owners", owners);
        model.addAttribute("yards", yards);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("yardId", yardId);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/task/" + taskId + "/allocation?shipmentId=" + shipmentId + "&ownerId=" + ownerId + "&yardId=" + yardId + "&");

        // For the allocation form
        List<Truck> truckForms = new ArrayList<>();
        Iterable<Truck> truckEntityIterable = truckService.findByShipmentId(shipmentEntity.getId());
        List<Truck> truckFormsForShipment = new ArrayList<>();
        for (Truck truck : truckEntityIterable) {
            truckFormsForShipment.add(truck);
        }

        truckForms.addAll(truckFormsForShipment);


        model.addAttribute("truckForms", truckForms);

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setShipmentId(shipmentId);
        model.addAttribute("selfContainerAllocation", selfContainerAllocation);
        return "task/allocateContainers/list";
    }

    @RequestMapping(method = RequestMethod.POST, params = "self")
    public String allocateInList(@PathVariable String taskId, SelfContainerAllocation selfContainerAllocation,
                                 @RequestHeader(value = "referer", required = false) final String referer) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", selfContainerAllocation);
        containerAllocationFacade.allocate(selfContainerAllocation);
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/release", method = RequestMethod.POST, params = "release")
    public String release(@PathVariable String taskId, ShipmentContainerAllocation shipmentContainerAllocation,
                          @RequestHeader(value = "referer", required = false) final String referer) {
        List<ContainerAllocation> containerAllocations = shipmentContainerAllocation.getContainerAllocations();
        Collection<RailContainer> railContainers = railContainerService.findByShipmentId(shipmentContainerAllocation.getShipmentId());
        for (RailContainer railContainer : railContainers) {
            for (ContainerAllocation containerAllocation : containerAllocations) {
                if (railContainer.getSc().getId().equals(containerAllocation.getAllocationId())) {
                    Truck truck = truckService.find(containerAllocation.getTruckId());
                    railContainer.setTruck(truck);
                    railContainer.setFleet(truck.getSp());
                    railContainer.setPlateNo(truck.getLicensePlate());
                    railContainer.setTrucker(truck.getDriver());
                    railContainer.setReleasedAt(new Date());
                    railContainerService.save(railContainer);
                }
            }
        }

        return "redirect:" + referer;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "shipmentId")
    public String initContainerSearch(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        shipmentContainerAllocation.setShipment(shipmentEntity);
        shipmentContainerAllocation.setType(shipmentEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(shipmentEntity.getOrder().getContainerSubtype().getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        model.addAttribute("taskId", taskId);

        // For the allocation form
        SearchContainerAllocForm searchContainerAllocForm = new SearchContainerAllocForm();
        searchContainerAllocForm.setShipmentId(shipmentId);
        model.addAttribute("searchContainerAllocForm", searchContainerAllocForm);
        return "task/allocateContainers/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String allocateInSearch(@PathVariable String taskId, SearchContainerAllocForm searchContainerAllocForm) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", searchContainerAllocForm);

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setShipmentId(searchContainerAllocForm.getShipmentId());
        selfContainerAllocation.setContainerIds(new Long[]{searchContainerAllocForm.getContainerId()});
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/search?shipmentId=" + searchContainerAllocForm.getShipmentId();
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET, params = "shipmentId")
    public String initContainerEnter(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        model.addAttribute("taskId", taskId);

        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        shipmentContainerAllocation.setShipment(shipmentEntity);
        shipmentContainerAllocation.setType(shipmentEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(shipmentEntity.getOrder().getContainerSubtype().getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        // Owner list
        List<ServiceProvider> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<Location> yards = locationService.findYards();
        // container subtypes
        Iterable<ContainerSubtype> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setShipmentId(shipmentId);
        for (int i = 0; i < 5; i++) {
            containers.getList().add(new Container());
        }
        // For the allocation form
        model.addAttribute("owners", owners);
        model.addAttribute("locations", yards);
        model.addAttribute("containerSubtypes", subtypes);
        model.addAttribute("containers", containers);
        return "task/allocateContainers/enter";
    }

    @RequestMapping(value = "/enter", method = RequestMethod.POST)
    public String allocateInEnter(@PathVariable String taskId, @ModelAttribute(value = "containers") EnterContainerAllocForm containers) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", containers);

        List<ContainerEntity> entities = new ArrayList<>();
        for (int i = 0; i < containers.getList().size(); i++) {
            Container container = containers.getList().get(i);
            ContainerEntity containerEntity = new ContainerEntity();
            if (null == container.getBicCode() || container.getBicCode().trim().isEmpty())
                continue;
            containerEntity.setId(container.getId());
            containerEntity.setBicCode(container.getBicCode());
            containerEntity.setOwner(ServiceProvider.newInstance(ServiceProvider.class, container.getOwnerId()));
            containerEntity.setYard(Location.newInstance(Location.class, container.getYardId()));
            containerEntity.setSubtype(ContainerSubtype.newInstance(ContainerSubtype.class, container.getSubtypeId()));
            containerEntity.setStatus(0);
            entities.add(containerEntity);
        }
        Iterable<ContainerEntity> containerEntities = containerService.save(entities);

        List<Long> containerIds = new ArrayList<>();
        for (ContainerEntity containerEntity : containerEntities) {
            containerIds.add(containerEntity.getId());
        }

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setShipmentId(containers.getShipmentId());

        selfContainerAllocation.setContainerIds(containerIds.toArray(new Long[0]));
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/enter?shipmentId=" + containers.getShipmentId();
    }

    @RequestMapping(method = RequestMethod.POST, params = "undo")
    public String undo(@PathVariable String taskId, @RequestParam Long allocationId,
                       @RequestHeader(value = "referer", required = false) final String referer) {
        logger.debug("taskId: {}", taskId);
        logger.debug("allocationId: {}", allocationId);

        containerAllocationFacade.undo(allocationId);
        return "redirect:" + referer;
    }
}
