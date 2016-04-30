package liquid.order.converter;

import liquid.order.domain.OrderContainer;
import liquid.order.service.OrderContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by mat on 4/29/16.
 */
@Component
public class OrderContainerFormatter implements Formatter<OrderContainer> {

    @Autowired
    private OrderContainerService orderContainerService;

    @Override
    public OrderContainer parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return orderContainerService.find(Long.valueOf(text));
    }

    @Override
    public String print(OrderContainer object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
