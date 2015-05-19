package liquid.order.controller;

import liquid.accounting.domain.ChargeEntity;
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
import liquid.container.domain.ContainerSubtypeEntity;
import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.core.controller.BaseController;
import liquid.core.model.SearchBarForm;
import liquid.core.security.SecurityContext;
import liquid.core.validation.FormValidationResult;
import liquid.operation.domain.*;
import liquid.operation.service.*;
import liquid.order.domain.LoadingType;
import liquid.order.domain.OrderEntity;
import liquid.order.domain.OrderStatus;
import liquid.order.domain.TradeType;
import liquid.order.facade.InternalOrderFacade;
import liquid.order.model.Order;
import liquid.order.model.ServiceItem;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.service.TaskService;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private InternalOrderFacade orderFacade;

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @Autowired
    private InvoiceService invoiceFacade;

    @Autowired
    private ReceiptService receiptFacade;

    @Autowired
    private SettlementService settlementService;

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceTypeEntity> populateServiceTypes() {
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

    @ModelAttribute("loadingTypes")
    public LoadingType[] populateLoadings() {
        return LoadingType.values();
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

    @ModelAttribute("railContainerSubtypes")
    public Iterable<ContainerSubtypeEntity> populateRailContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.RAIL);
    }

    @ModelAttribute("selfContainerSubtypes")
    public Iterable<ContainerSubtypeEntity> populateOwnContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.SELF);
    }

    @ModelAttribute("status")
    public OrderStatus[] populateStatus() {
        return OrderStatus.values();
    }

    @ModelAttribute("serviceSubtypes")
    public Iterable<ServiceSubtype> populateServiceSubtyes() {return serviceSubtypeService.findEnabled(); }

    @ModelAttribute("railwayPlanTypes")
    public Iterable<RailPlanTypeEntity> populateRailwayPlanTypes() {
        return railwayPlanTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initFindPaging(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        String username = SecurityContext.getInstance().getUsername();

        Page<Order> page;

        switch (SecurityContext.getInstance().getRole()) {
            case "ROLE_SALES":
            case "ROLE_MARKETING":
                page = orderFacade.findByCreateUser(username, pageRequest);
                break;
            default:
                page = orderFacade.findAll(pageRequest);
                break;
        }

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/order?");

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction("/order");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        return "order/page";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        Page<Order> page = new PageImpl<Order>(new ArrayList<>());
        if ("customer".equals(searchBarForm.getType())) {
            PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
            page = orderFacade.findByCustomerId(searchBarForm.getId(), pageRequest);
        } else if ("order".equals(searchBarForm.getType())) {
            Order order = orderFacade.find(searchBarForm.getId());
            List<Order> orders = new ArrayList<>();
            orders.add(order);
            page = new PageImpl<Order>(orders);
        }
        model.addAttribute("page", page);
        model.addAttribute("searchBarForm", searchBarForm);
        return "order/page";
    }

    @Deprecated
    @RequestMapping(method = RequestMethod.GET, params = {"type", "text"})
    public String search0(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        String orderNo = "orderNo".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;
        String customerName = "customerName".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;
        String username = SecurityContext.getInstance().getUsername();

        Page<Order> page;

        switch (SecurityContext.getInstance().getRole()) {
            case "ROLE_SALES":
            case "ROLE_MARKETING":
                page = orderFacade.findAll(orderNo, customerName, username, pageRequest);
                break;
            default:
                page = orderFacade.findAll(orderNo, customerName, null, pageRequest);
                break;
        }

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/order?type=" + searchBarForm.getType() + "&text=" + searchBarForm.getText() + "&");

        searchBarForm.setAction("/order");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        return "order/page";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String initCreationForm(Model model) {
        Order order = orderFacade.initOrder();

        model.addAttribute("order", order);
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        return "order/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "addServiceItem")
    public String addServiceItem(@ModelAttribute Order order) {
        logger.debug("order: {}", order);
        order.getServiceItems().add(new ServiceItem());

        return "order/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "removeServiceItem")
    public String removeRow(@ModelAttribute Order order, HttpServletRequest request) {
        final Integer rowId = Integer.valueOf(request.getParameter("removeServiceItem"));
        order.getServiceItems().remove(rowId.intValue());
        return "order/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute Order order, BindingResult bindingResult, Model model, HttpServletRequest request) {
        logger.debug("order: {}", order);
        String sourceName = request.getParameter("sourceName");
        logger.debug("sourceName: {}", sourceName);
        String destinationName = request.getParameter("destinationName");
        logger.debug("destinationName: {}", destinationName);
        if (bindingResult.hasErrors()) {
            model.addAttribute("sourceName", order.getSource().getName());
            model.addAttribute("destinationName", order.getDestination().getName());
            return "order/form";
        }

        // FIXME - Need a new way to validate customer.
//        FormValidationResult result = orderFacade.validateCustomer(order.getCustomerId(), order.getCustomerName());
//        if (!result.isSuccessful()) {
//            addFieldError(bindingResult, "order", "customerName", order.getCustomerName(), order.getCustomerName());
//        } else {
//            order.setCustomerId(result.getId());
//            order.setCustomerName(result.getName());
//        }

        FormValidationResult result = orderFacade.validateLocation(order.getSource().getId(), sourceName);
        if (!result.isSuccessful()) {
            order.getSource().setName(sourceName);
            addFieldError(bindingResult, "order", "source", order.getSource(), sourceName);
        }

        result = orderFacade.validateLocation(order.getDestination().getId(), destinationName);
        if (!result.isSuccessful()) {
            order.getDestination().setName(destinationName);
            addFieldError(bindingResult, "order", "destination", order.getDestination(), destinationName);
        }

        result = orderFacade.validateLocation(order.getRailSourceId(), order.getRailSource(), LocationType.STATION);
        if (!result.isSuccessful()) {
            addFieldError(bindingResult, "order", "railSource", order.getRailSource(), order.getRailSource());
        } else {
            order.setRailSourceId(result.getId());
            order.setRailSource(result.getName());
        }

        result = orderFacade.validateLocation(order.getRailDestinationId(), order.getRailDestination(), LocationType.STATION);
        if (!result.isSuccessful()) {
            addFieldError(bindingResult, "order", "railDestination", order.getRailDestination(), order.getRailDestination());
        } else {
            order.setRailDestinationId(result.getId());
            order.setRailDestination(result.getName());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("sourceName", order.getSource().getName());
            return "order/form";
        }

        order.setStatus(OrderStatus.SAVED.getValue());
        orderFacade.save(order);
        return "redirect:/order?number=0";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute Order order,
                         BindingResult bindingResult, Model model, HttpServletRequest request) {
        logger.debug("order: {}", order);
        String sourceName = request.getParameter("sourceName");
        logger.debug("sourceName: {}", sourceName);
        String destinationName = request.getParameter("destinationName");
        logger.debug("destinationName: {}", destinationName);
        if (bindingResult.hasErrors()) {
            model.addAttribute("sourceName", order.getSource().getName());
            model.addAttribute("destinationName", order.getDestination().getName());
            return "order/form";
        }

        // FIXME - Need a new way to validate customer.
//        FormValidationResult result = orderFacade.validateCustomer(order.getCustomerId(), order.getCustomerName());
//        if (!result.isSuccessful()) {
//            addFieldError(bindingResult, "order", "customerName", order.getCustomerName(), order.getCustomerName());
//        } else {
//            order.setCustomerId(result.getId());
//            order.setCustomerName(result.getName());
//        }

        FormValidationResult result = orderFacade.validateLocation(order.getSource().getId(), sourceName);
        if (!result.isSuccessful()) {
            addFieldError(bindingResult, "order", "source", order.getSource(), sourceName);
        }

        result = orderFacade.validateLocation(order.getDestination().getId(), destinationName);
        if (!result.isSuccessful()) {
            addFieldError(bindingResult, "order", "destination", order.getDestination(), destinationName);
        }

        result = orderFacade.validateLocation(order.getRailSourceId(), order.getRailSource(), LocationType.STATION);
        if (!result.isSuccessful()) {
            addFieldError(bindingResult, "order", "railSource", order.getRailSource(), order.getRailSource());
        } else {
            order.setRailSourceId(result.getId());
            order.setRailSource(result.getName());
        }

        result = orderFacade.validateLocation(order.getRailDestinationId(), order.getRailDestination(), LocationType.STATION);
        if (!result.isSuccessful()) {
            addFieldError(bindingResult, "order", "railDestination", order.getRailDestination(), order.getRailDestination());
        } else {
            order.setRailDestinationId(result.getId());
            order.setRailDestination(result.getName());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("sourceName", order.getSource().getName());
            model.addAttribute("destinationName", order.getDestination().getName());
            return "order/form";
        }

        order.setStatus(OrderStatus.SUBMITTED.getValue());
        orderFacade.submit(order);

        return "redirect:/order?number=0";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String initEdit(@PathVariable long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderFacade.find(id);
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
        return "order/form";
    }

    @RequestMapping(value = "/{id}/duplicate", method = RequestMethod.GET)
    public String initDuplicate(@PathVariable long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderFacade.duplicate(id);
        logger.debug("order: {}", order);

        model.addAttribute("order", order);
        model.addAttribute("sourceName", order.getSource().getName());
        model.addAttribute("destinationName", order.getDestination().getName());
        return "order/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderFacade.find(id);
        List<Location> locationEntities = locationService.findByTypeId(LocationType.CITY);
        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");
        return "order/detail";
    }

    @RequestMapping(value = "/{id}/{tab}", method = RequestMethod.GET)
    public String charge(@PathVariable long id, @PathVariable String tab, Model model) {
        logger.debug("id: {}", id);
        logger.debug("tab: {}", tab);

        Order order = orderFacade.find(id);

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
                Iterable<ChargeEntity> charges = chargeService.getChargesByOrderId(id);

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
        return "order/" + tab;
    }

    @RequestMapping(value = "/{orderId}/receivable", method = RequestMethod.GET)
    public String initPanel(@PathVariable Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);

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
        Order order = orderFacade.find(orderId);
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
