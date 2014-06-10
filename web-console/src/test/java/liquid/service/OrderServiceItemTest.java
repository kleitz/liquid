package liquid.service;

import liquid.config.*;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceTypeEntity;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by redbrick9 on 4/26/14.
 */
public class OrderServiceItemTest {

    @Test
    public void computeOrderNo() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BpmConfig.class);
        context.register(LdapConfig.class);
        context.register(JpaConfig.class);
        context.register(RootConfig.class);
        context.register(SecurityConfig.class);
        context.register(MailConfig.class);
        context.refresh();

        OrderEntity order = new OrderEntity();
        order.setCreateRole("ROLE_SALES");
        ServiceTypeEntity serviceType = new ServiceTypeEntity();
        serviceType.setCode("1");
        order.setServiceType(serviceType);
        OrderService orderService = context.getBean(OrderService.class);
        System.out.println(orderService.computeOrderNo(order.getCreateRole(), order.getServiceType().getCode()));
    }
}