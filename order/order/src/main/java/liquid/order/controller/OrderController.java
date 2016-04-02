package liquid.order.controller;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.model.Invoice;
import liquid.accounting.model.Receipt;
import liquid.accounting.model.Settlement;
import liquid.accounting.model.Statement;
import liquid.accounting.service.ChargeService;
import liquid.accounting.service.InvoiceService;
import liquid.accounting.service.ReceiptService;
import liquid.accounting.service.SettlementService;
import liquid.container.domain.ContainerCap;
import liquid.container.domain.ContainerSubtype;
import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.core.controller.BaseController;
import liquid.core.security.SecurityContext;
import liquid.operation.domain.*;
import liquid.operation.service.*;
import liquid.order.domain.*;
import liquid.order.model.OrderSearchBar;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.service.BusinessKey;
import liquid.process.service.ProcessService;
import liquid.process.service.TaskService;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * User: tao
 * Date: 9/28/13
 * Time: 2:38 PM
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private Environment env;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @Autowired
    private InvoiceService invoiceFacade;

    @Autowired
    private ReceiptService receiptFacade;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private ProcessService processService;

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceType> populateServiceTypes() {
        return serviceTypeService.findAll();
    }

    @ModelAttribute("cargos")
    public Iterable<Goods> populateCargos() {
        return goodsService.findAll();
    }

    @ModelAttribute("tradeTypes")
    public TradeType[] populateTradeTypes() {
        return TradeType.values();
    }

    @ModelAttribute("tradeTypeMap")
    public Map<Integer, String> populateTradeTypeMap() {
        return TradeType.toMap();
    }

    @ModelAttribute("loadingTypes")
    public LoadingType[] populateLoadings() {
        return LoadingType.values();
    }

    @ModelAttribute("loadingTypeMap")
    public Map<Integer, String> populateLoadingTypeMap() {
        return LoadingType.toMap();
    }

    @ModelAttribute("containerTypeMap")
    public Map<Integer, String> populateContainerTypes() {
        return ContainerType.toMap();
    }

    @Deprecated
    @ModelAttribute("containerCaps")
    public ContainerCap[] populateContainerCaps() {
        return ContainerCap.values();
    }

    @Deprecated
    @ModelAttribute("railContainerSubtypes")
    public Iterable<ContainerSubtype> populateRailContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.RAIL);
    }

    @Deprecated
    @ModelAttribute("selfContainerSubtypes")
    public Iterable<ContainerSubtype> populateOwnContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.SELF);
    }

    @ModelAttribute("containerSubtypes")
    public Iterable<ContainerSubtype> populateontainerSubtypes() {
        return containerSubtypeService.findAll();
    }

    @ModelAttribute("serviceSubtypes")
    public Iterable<ServiceSubtype> populateServiceSubtyes() {
        return serviceSubtypeService.findEnabled();
    }

    @ModelAttribute("railwayPlanTypes")
    public Iterable<RailPlanType> populateRailwayPlanTypes() {
        return railwayPlanTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@ModelAttribute(value = "searchBarForm") OrderSearchBar orderSearchBar,
                       Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(orderSearchBar.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Long id = null;
        Long customerId = null;
        String username = null;
        Boolean isDiscarded = false;
        switch (SecurityContext.getInstance().getRole()) {
            case "ROLE_SALES":
            case "ROLE_MARKETING":
                username = SecurityContext.getInstance().getUsername();
                break;
            default:
                break;
        }
        if ("customer".equals(orderSearchBar.getType())) {
            customerId = orderSearchBar.getId();
        } else if ("order".equals(orderSearchBar.getType())) {
            id = orderSearchBar.getId();
        }
        if(orderSearchBar.getStatus() != null && orderSearchBar.getStatus() == 3) {
            isDiscarded = true;
        }

        Page<Order> page = orderService.findAll(id, customerId, username, isDiscarded, pageRequest);

        orderSearchBar.prepand(request.getRequestURI());
        model.addAttribute("page", page);
        if(isDiscarded) {
            return "order/shipment/discarded_list";
        } else {
            return "order/shipment/list";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String initCreationForm(Model model) {
        Order order = new Order();
        order.setSource(locationService.find(Long.valueOf(env.getProperty("default.origin.id"))));
        order.setDestination(locationService.find(Long.valueOf(env.getProperty("default.destination.id"))));
        order.setLoadingEt(new Date());

        OrderRail orderRail = new OrderRail();
        orderRail.setSource(order.getSource());
        orderRail.setDestination(order.getDestination());
        orderRail.setPlanReportTime(new Date());
        order.setRailway(orderRail);

        List<ServiceItem> serviceItemList = new ArrayList<>();
        List<ServiceSubtype> serviceSubtypeList = serviceSubtypeService.findEnabled();
        for (int i = 0; i < 5; i++) {
            ServiceItem serviceItem = new ServiceItem();
            serviceItem.setServiceSubtype(serviceSubtypeList.get(i));
            serviceItemList.add(serviceItem);
        }
        order.setServiceItems(serviceItemList);

        List<OrderContainer> receivingContainerList = new ArrayList<>();

        model.addAttribute("order", order);
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        model.addAttribute("railSourceName", orderRail.getSource().getName());
        model.addAttribute("railDestinationName", orderRail.getDestination().getName());
        return "order/shipment/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "addServiceItem")
    public String addServiceItem(@ModelAttribute(value = "order") Order order, Model model) {
        logger.debug("order: {}", order);
        order.getServiceItems().add(new ServiceItem());
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        model.addAttribute("railSourceName", order.getRailway().getSource().getName());
        model.addAttribute("railDestinationName", order.getRailway().getDestination().getName());
        return "order/shipment/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "removeServiceItem")
    public String removeRow(@ModelAttribute(value = "order") Order order, Model model, HttpServletRequest request) {
        final Integer rowId = Integer.valueOf(request.getParameter("removeServiceItem"));
        order.getServiceItems().remove(rowId.intValue());
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        model.addAttribute("railSourceName", order.getRailway().getSource().getName());
        model.addAttribute("railDestinationName", order.getRailway().getDestination().getName());
        return "order/shipment/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute(value = "order") Order order, BindingResult bindingResult, Model model, HttpServletRequest request) {
        logger.debug("order: {}", order);
        String sourceName = request.getParameter("sourceName");
        logger.debug("sourceName: {}", sourceName);
        String destinationName = request.getParameter("destinationName");
        logger.debug("destinationName: {}", destinationName);
        String railSourceName = request.getParameter("railSourceName");
        logger.debug("railSourceName: {}", railSourceName);
        String railDestinationName = request.getParameter("railDestinationName");
        logger.debug("railDestinationName: {}", railDestinationName);

        if (null == order.getContainerSubtype()) {
            bindingResult.rejectValue("containerSubtype", "order.container.subtype.illegal");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("sourceName", order.getSource().getName());
            model.addAttribute("destinationName", order.getDestination().getName());
            model.addAttribute("railSourceName", order.getRailway().getSource().getName());
            model.addAttribute("railDestinationName", order.getRailway().getDestination().getName());
            return "order/shipment/form";
        }

        order.setStatus(OrderStatus.SAVED.getValue());
        orderService.saveOrder(order);
        return "redirect:/order?number=0";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute(value = "order") Order order,
                         BindingResult bindingResult, Model model, HttpServletRequest request) {
        logger.debug("order: {}", order);

        String sourceName = request.getParameter("sourceName");
        String destinationName = request.getParameter("destinationName");
        String railSourceName = request.getParameter("railSourceName");
        String railDestinationName = request.getParameter("railDestinationName");
        logger.debug("sourceName: {}; destinationName: {}", sourceName, destinationName);
        logger.debug("railSourceName: {}; railDestinationName: {}", railSourceName, railDestinationName);

        if (null == order.getContainerSubtype()) {
            bindingResult.rejectValue("containerSubtype", "order.container.subtype.illegal");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("sourceName", order.getSource().getName());
            model.addAttribute("destinationName", order.getDestination().getName());
            model.addAttribute("railSourceName", order.getRailway().getSource());
            model.addAttribute("railDestinationName", order.getRailway().getDestination());
            return "order/shipment/form";
        }

        order.setStatus(OrderStatus.SUBMITTED.getValue());
        order = orderService.submitOrder(order);

        boolean hasDelivery = false;
        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            if (serviceItem.getServiceSubtype().getId() == Long.valueOf(env.getProperty("service.subtype.delivery.id"))) {
                hasDelivery = true;
                break;
            }
        }
        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("serviceCode", order.getServiceType().getCode());
        variableMap.put("loadingType", order.getLoadingType());
        variableMap.put("hasDelivery", hasDelivery);
        variableMap.put("orderOwner", order.getUpdatedBy());
        variableMap.put("tradeType", order.getTradeType());
        processService.startProcess(order.getUpdatedBy(), BusinessKey.encode(order.getId(), order.getOrderNo()), variableMap);

        return String.format("redirect:/task?key=%s", BusinessKey.encode(order.getId(), order.getOrderNo()).getText());
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderService.find(id);
        logger.debug("order: {}", order);

        List<ServiceItem> serviceItemList = order.getServiceItems();
        if (null == serviceItemList) serviceItemList = new ArrayList<>();
        if (serviceItemList.size() < 5) {
            for (int i = serviceItemList.size(); i < 5; i++) {
                serviceItemList.add(new ServiceItem());
            }
        }
        order.setServiceItems(serviceItemList);

        model.addAttribute("order", order);
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        model.addAttribute("railSourceName", order.getRailway().getSource().getName());
        model.addAttribute("railDestinationName", order.getRailway().getDestination().getName());

        return "order/shipment/form";
    }

    @RequestMapping(value = "/{id}/duplicate", method = RequestMethod.GET)
    public String initDuplicate(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderService.find(id);
        order.setId(null);
        if (null != order.getRailway())
            order.getRailway().setId(null);
        else {
            OrderRail orderRail = new OrderRail();
            orderRail.setSource(order.getSource());
            orderRail.setDestination(order.getDestination());
            orderRail.setPlanReportTime(new Date());
            order.setRailway(orderRail);
        }
        order.setOrderNo(null);

        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            serviceItem.setId(null);
        }
        logger.debug("order: {}", order);

        List<OrderContainer> containers = new ArrayList<>(order.getContainerQty());
        for(int i = 0; i < order.getContainerQty();i++) {
            containers.add(new OrderContainer());
        }
        order.setContainers(containers);

        model.addAttribute("order", order);
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        model.addAttribute("railSourceName", order.getRailway().getSource().getName());
        model.addAttribute("railDestinationName", order.getRailway().getDestination().getName());
        return "order/shipment/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderService.find(id);
        List<Location> locationEntities = locationService.findByTypeId(LocationType.CITY);
        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");
        return "order/shipment/detail";
    }

    @RequestMapping(value = "/{id}/{tab}", method = RequestMethod.GET)
    public String charge(@PathVariable long id, @PathVariable String tab, Model model) {
        logger.debug("id: {}", id);
        logger.debug("tab: {}", tab);

        Order order = orderService.find(id);

        switch (tab) {
            case "task":
                List<Task> completedTasks = taskService.listCompltedTasks(id + ":" + order.getOrderNo());
                model.addAttribute("completedTasks", completedTasks);
                break;
            case "railway":
                break;
            case "container":
                Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(id);
                model.addAttribute("shipmentSet", shipmentSet);
                break;
            case "charge":
                Iterable<Charge> charges = chargeService.getChargesByOrderId(id);

                Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
                model.addAttribute("serviceSubtypes", serviceSubtypes);
                model.addAttribute("chargeWays", ChargeWay.values());
                model.addAttribute("charges", charges);
                break;
            case "amount":
                tab = "amount";
                break;
            default:
                tab = "detail";
                break;
        }

        model.addAttribute("order", order);
        model.addAttribute("tab", tab);
        return "order/shipment/" + tab;
    }

    @RequestMapping(value = "/{orderId}/receivable", method = RequestMethod.GET)
    public String initPanel(@PathVariable Long orderId, Model model) {
        Order order = orderService.find(orderId);

        Statement<Invoice> statement = invoiceFacade.findByOrderId(orderId);
        model.addAttribute("statement", statement);

        Statement<Receipt> receiptStatement = receiptFacade.findByOrderId(orderId);
        model.addAttribute("receiptStatement", receiptStatement);

        Statement<Settlement> settlementStatement = settlementService.findByOrderId(orderId);
        model.addAttribute("settlementStatement", settlementStatement);

        return "receivable/panel";
    }

    @RequestMapping(value = "/{orderId}/invoice", method = RequestMethod.GET)
    public String initInvoice(@PathVariable Long orderId, Model model) {
        Order order = orderService.find(orderId);
        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setIssuedAt(DateUtil.dayStrOf());
        invoice.setBuyerId(order.getCustomer().getId());
        invoice.setBuyerName(order.getCustomer().getName());
        invoice.setExpectedPaymentAt(DateUtil.dayStrOf());
        model.addAttribute("invoice", invoice);
        return "invoice/form";
    }

    @RequestMapping(value = "/{orderId}/invoice", method = RequestMethod.POST)
    public String addInvoice(@PathVariable Long orderId, @Valid @ModelAttribute Invoice invoice,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "invoice/form";
        }
        invoiceFacade.update(invoice);
        return "redirect:/order/" + orderId + "/receivable";
    }

    @RequestMapping(value = "/{orderId}/receipt", method = RequestMethod.GET)
    public String initReceipt(@PathVariable Long orderId, Model model) {
        Receipt receipt = new Receipt();
        receipt.setOrderId(orderId);
        receipt.setIssuedAt(DateUtil.dayStrOf());
        model.addAttribute("receipt", receipt);
        return "receipt/form";
    }

    @RequestMapping(value = "/{orderId}/receipt", method = RequestMethod.POST)
    public String addReceipt(@PathVariable Long orderId, @Valid @ModelAttribute Receipt receipt,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "receipt/form";
        }
        receiptFacade.update(receipt);
        return "redirect:/order/" + orderId + "/receivable";
    }

    @RequestMapping(value = "/{orderId}/settlement", method = RequestMethod.GET)
    public String initSettlement(@PathVariable Long orderId, Model model) {
        Settlement settlement = new Settlement();
        settlement.setOrderId(orderId);
        settlement.setSettledAt(DateUtil.dayStrOf());
        model.addAttribute("settlement", settlement);
        return "settlement/form";
    }

    @RequestMapping(value = "/{orderId}/settlement", method = RequestMethod.POST)
    public String addSettlement(@PathVariable Long orderId, @Valid @ModelAttribute Settlement settlement,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "settlement/form";
        }
        settlementService.save(settlement);
        return "redirect:/order/" + orderId + "/receivable";
    }
}
