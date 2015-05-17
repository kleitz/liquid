package liquid.order.converter;

import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/8/15.
 */
@Component
public class OrderFormatter implements Formatter<OrderEntity> {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderEntity parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return orderService.find(Long.valueOf(text));
    }

    @Override
    public String print(OrderEntity object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
