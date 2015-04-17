package liquid.process.controller;

import liquid.container.domain.ContainerEntity;
import liquid.container.domain.ContainerSubtypeEntity;
import liquid.container.domain.ContainerType;
import liquid.container.model.Container;
import liquid.container.model.EnterContainerAllocForm;
import liquid.container.service.ContainerService;
import liquid.container.service.ContainerSubtypeService;
import liquid.core.model.Alert;
import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.LocationService;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.order.service.ServiceItemService;
import liquid.transport.domain.RailContainerEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.TruckEntity;
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

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        List<ShipmentContainerAllocation> shipmentContainerAllocations = containerAllocationFacade.computeContainerAllocation(order);

        model.addAttribute("shipmentContainerAllocations", shipmentContainerAllocations);

        if (ContainerType.RAIL.getType() == order.getContainerType()) {
            return "task/allocateContainers/rail_container";
        } else {
            List<TruckForm> truckForms = new ArrayList<>();

            Iterable<ShipmentEntity> shipmentEntities = shipmentService.findByOrderId(orderId);
            for (ShipmentEntity shipmentEntity : shipmentEntities) {
                Iterable<TruckEntity> truckEntityIterable = truckService.findByShipmentId(shipmentEntity.getId());
                List<TruckForm> truckFormsForShipment = new ArrayList<>();
                for (TruckEntity truckEntity : truckEntityIterable) {
                    TruckForm truck = convert(truckEntity);
                    truckFormsForShipment.add(truck);
                }

                truckForms.addAll(truckFormsForShipment);
            }

            model.addAttribute("shipmentContainerAllocation", new ShipmentContainerAllocation());
            model.addAttribute("truckForms", truckForms);
            return "task/allocateContainers/self_container";
        }
    }

    private TruckForm convert(TruckEntity entity) {
        TruckForm truck = new TruckForm();
        truck.setId(entity.getId());
        truck.setShipmentId(entity.getShipment().getId());
        truck.setPickingAt(DateUtil.stringOf(entity.getPickingAt()));
        truck.setServiceProviderId(entity.getServiceProviderId());
        truck.setLicensePlate(entity.getLicensePlate());
        truck.setDriver(entity.getDriver());
        return truck;
    }

    @RequestMapping(value = "/rail", method = RequestMethod.GET, params = "shipmentId")
    public String initRailAlloc(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        model.addAttribute("taskId", taskId);

        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        ShipmentContainerAllocation shipmentContainerAllocation = containerAllocationFacade.getShipmentContainerAllocation(
                ContainerType.RAIL.getType(), containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.RAIL);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setShipmentId(shipmentId);

        // For the allocation form
        model.addAttribute("containerSubtypes", subtypes);
        return "task/allocateContainers/rail_enter";
    }

    /**
     * Rail Container Allocation.
     *
     * @param taskId
     * @param shipmentContainerAllocation
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId,
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
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        model.addAttribute("taskId", taskId);

        int size = 5;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findAll(shipmentEntity.getOrder().getContainerSubtypeId(),
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
        List<TruckForm> truckForms = new ArrayList<>();
        Iterable<TruckEntity> truckEntityIterable = truckService.findByShipmentId(shipmentEntity.getId());
        List<TruckForm> truckFormsForShipment = new ArrayList<>();
        for (TruckEntity truckEntity : truckEntityIterable) {
            TruckForm truck = convert(truckEntity);
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
        Collection<RailContainerEntity> railContainerEntities = railContainerService.findByShipmentId(shipmentContainerAllocation.getShipmentId());
        for (RailContainerEntity railContainerEntity : railContainerEntities) {
            for (ContainerAllocation containerAllocation : containerAllocations) {
                if (railContainerEntity.getSc().getId().equals(containerAllocation.getAllocationId())) {
                    TruckEntity truckEntity = truckService.find(containerAllocation.getTruckId());
                    railContainerEntity.setTruck(truckEntity);
                    railContainerEntity.setFleet(ServiceProvider.newInstance(liquid.operation.domain.ServiceProvider.class, truckEntity.getServiceProviderId()));
                    railContainerEntity.setPlateNo(truckEntity.getLicensePlate());
                    railContainerEntity.setTrucker(truckEntity.getDriver());
                    railContainerEntity.setReleasedAt(new Date());
                    railContainerService.save(railContainerEntity);
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
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
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
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        // Owner list
        List<ServiceProvider> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<Location> yards = locationService.findYards();
        // container subtypes
        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);
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
            containerEntity.setSubtype(ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, container.getSubtypeId()));
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
