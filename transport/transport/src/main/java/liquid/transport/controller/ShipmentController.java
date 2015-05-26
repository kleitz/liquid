package liquid.transport.controller;

import liquid.core.model.Pagination;
import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.handler.DefinitionKey;
import liquid.process.service.BusinessKey;
import liquid.process.service.TaskService;
import liquid.transport.domain.*;
import liquid.transport.model.Leg;
import liquid.transport.model.RailTransport;
import liquid.transport.model.Shipment;
import liquid.transport.model.ShipmentSet;
import liquid.transport.repository.RailContainerRepository;
import liquid.transport.service.InternalShipmentService;
import liquid.transport.service.LegService;
import liquid.transport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by redbrick9 on 9/11/14.
 */
@Controller
@RequestMapping("/shipment")
public class ShipmentController {
    private static final int size = 20;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InternalShipmentService shipmentService;

    @Autowired
    private LegService legService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private RailContainerRepository railContainerRepository;

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pagination pagination, Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(pagination.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ShipmentEntity> page = shipmentService.findAll(pageRequest);
        pagination.prepand(request.getRequestURI());
        model.addAttribute("page", page);
        return "shipment/list";
    }

    @RequestMapping(method = RequestMethod.GET, params = "o")
    public String findByOrder(@RequestParam(value = "o") Long orderId, Model model) {
        Order order = orderService.find(orderId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        model.addAttribute("tab", "shipment");
        model.addAttribute("order", order);
        model.addAttribute("shipmentSet", shipmentSet);
        return "shipment/detail";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=edit")
    public String initEditForm(@RequestParam(value = "o") Long orderId, Model model) {
        Order order = orderService.find(orderId);
        Iterable<ShipmentEntity> shipmentEntities = shipmentService.findByOrderId(orderId);

        ShipmentSet shipmentSet = new ShipmentSet();
        shipmentSet.setOrderId(orderId);
        shipmentSet.setShipments(Shipment.valueOf(shipmentEntities));

        model.addAttribute("tab", "shipment");
        model.addAttribute("order", order);
        model.addAttribute("shipmentSet", shipmentSet);
        model.addAttribute("fleets", serviceProviderService.findByType(4L));
        return "shipment/edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "save_to_route")
    public String saveToRoute(@PathVariable Long id) {
        ShipmentEntity shipment = shipmentService.find(id);
        List<LegEntity> legs = shipment.getLegs();

        RouteEntity route = new RouteEntity();
        route.setName("Copy form shipment " + shipment.getId());
        List<PathEntity> paths = new ArrayList<>(legs.size());
        PathEntity prev = null;
        for (LegEntity leg : legs) {
            PathEntity path = new PathEntity();
            path.setRoute(route);
            path.setTransportMode(leg.getTransMode());
            path.setFrom(leg.getSrcLoc());
            path.setTo(leg.getDstLoc());
            paths.add(path);

            if (null == prev) {
                route.setHead(path);
                route.setFrom(path.getFrom());
            } else {
                prev.setNext(path);
                route.setTo(path.getTo());
            }
            prev = path;
        }
        route.setPaths(paths);
        RouteEntity newRoute = routeService.save(route);

        return "redirect:/route/" + newRoute.getId();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String edit(@RequestParam(value = "o") Long orderId, @ModelAttribute ShipmentSet shipmentSet) {
        RailTransport[] railTransportSet = null;

        Collection<RailContainer> entities = RailTransport.toEntities(railTransportSet);
        railContainerRepository.save(entities);

        return "redirect:/shipment?o=" + orderId;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "delete")
    public String deleteShipment(@PathVariable Long id) {
        ShipmentEntity shipmentEntity = shipmentService.find(id);
        shipmentService.delete(id);
        return "redirect:/task/" + shipmentEntity.getTaskId();
    }

    @RequestMapping(value = "/{id}/leg", method = RequestMethod.GET)
    public String get(@PathVariable Long id, Model model) {
        Leg leg = new Leg();
        leg.setShipmentId(id);

        model.addAttribute("transportModeOptions", TransMode.values());
        model.addAttribute("leg", leg);
        return "leg/form";
    }

    @RequestMapping(value = "/{id}/leg", method = RequestMethod.POST)
    public String post(@PathVariable Long id, Leg leg, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Long serviceSubtype = TransMode.toServiceType(leg.getTransMode());
            Iterable<ServiceProvider> sps = Collections.emptyList();
            if (serviceSubtype != null) {
                sps = serviceProviderService.findByType(serviceSubtype);
            }

            model.addAttribute("transportModeOptions", TransMode.values());
            model.addAttribute("sps", sps);
            model.addAttribute("leg", leg);
            return "leg/form";
        }

        ShipmentEntity shipmentEntity = shipmentService.find(id);

        LegEntity legEntity = null;
        if (null == leg.getId()) {
            legEntity = new LegEntity();
            legEntity.setTransMode(leg.getTransMode());
        } else {
            legEntity = legService.find(leg.getId());
        }

        if (leg.getServiceProviderId() != null)
            legEntity.setSp(ServiceProvider.newInstance(ServiceProvider.class, leg.getServiceProviderId()));
        legEntity.setSrcLoc(Location.newInstance(Location.class, leg.getSourceId()));
        legEntity.setDstLoc(Location.newInstance(Location.class, leg.getDestinationId()));
        legEntity.setShipment(shipmentEntity);
        legService.save(legEntity);

        String taskId = shipmentEntity.getTaskId();
        if (null == taskId) {
            Order order = shipmentEntity.getOrder();
            Task task = taskService.getTask(DefinitionKey.planShipment, BusinessKey.encode(order.getId(), order.getOrderNo()));
            if (null == task) {
                // FIXME - Will delete it after GA.
                task = taskService.getTask(DefinitionKey.planRoute, BusinessKey.encode(order.getId(), order.getOrderNo()));
            }
            if (null != task) {
                taskId = task.getId();
            }
        }

        return "redirect:/task/" + taskId;
    }

    private Long computeDefaultDstLocId(List<Location> locationEntities) {
        int size = locationEntities.size();
        long id = 0;
        if (size < 2) {
            id = locationEntities.get(0).getId();
        } else {
            id = locationEntities.get(1).getId();
        }
        return id;
    }
}