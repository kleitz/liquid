package liquid.order.restfulapi;

import liquid.core.model.SearchBarForm;
import liquid.operation.domain.Customer;
import liquid.operation.service.CustomerService;
import liquid.order.domain.Order;
import liquid.order.domain.ServiceItem;
import liquid.order.service.OrderService;
import liquid.process.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 12/9/14.
 */
@Controller
@RequestMapping("/api/order")
public class OrderRestController {
    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET, params = "text")
    @ResponseBody
    public Iterable<SearchBarForm> listByName(@RequestParam String text) {
        logger.debug("text: {}", text);
        Iterable<Customer> customers = customerService.findByQueryNameLike(text);
        List<SearchBarForm> searchBarForms = new ArrayList<>();
        for (Customer customer : customers) {
            SearchBarForm searchBarForm = new SearchBarForm();
            searchBarForm.setType("customer");
            searchBarForm.setId(customer.getId());
            searchBarForm.setText(customer.getName());
            searchBarForms.add(searchBarForm);
        }

        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "id"));
        Page<Order> page = orderService.findByOrderNoLike(text, pageRequest);
        for (Order order : page.getContent()) {
            SearchBarForm searchBarForm = new SearchBarForm();
            searchBarForm.setId(order.getId());
            searchBarForm.setType("order");
            searchBarForm.setText(order.getOrderNo());
            searchBarForms.add(searchBarForm);
        }

        return searchBarForms;
    }

    @RequestMapping(value = "/{id}/service-item", method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceItem> listServiceItems(@PathVariable Long id) {
        Order order = orderService.find(id);
        return order.getServiceItems();
    }

    @RequestMapping(value = "/{id}/service-item", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void saveServiceItem(@PathVariable Long id, @RequestBody ServiceItem serviceItem) {
        logger.debug("ServiceItem: {}", serviceItem);
        Order order = orderService.find(id);
        order.getServiceItems().remove(serviceItem);
        order.getServiceItems().add(serviceItem);
        orderService.save(order);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Long id){
        orderService.delete(id);
    }
}
