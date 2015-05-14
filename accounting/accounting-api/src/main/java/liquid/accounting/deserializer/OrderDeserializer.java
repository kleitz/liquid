package liquid.accounting.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

/**
 * Created by Tao Ma on 5/14/15.
 */
@Component
public class OrderDeserializer extends JsonDeserializer<OrderEntity> {

    @Autowired
    private OrderService orderService;

    public OrderDeserializer() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public OrderEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jsonParser.getText().trim();
            Long orderId = Long.parseLong(text);
            return orderService.find(orderId);
        }
        throw new IllegalArgumentException();
    }
}
