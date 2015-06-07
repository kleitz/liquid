package liquid.accounting.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import liquid.accounting.domain.Charge;
import liquid.accounting.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

/**
 * Created by Tao Ma on 6/7/15.
 */
@Component
public class ChargeDeserializer extends JsonDeserializer<Charge> {
    @Autowired
    private ChargeService chargeService;

    public ChargeDeserializer() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public Charge deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jsonParser.getText().trim();
            Long chargeId = Long.parseLong(text);
            return chargeService.find(chargeId);
        }
        throw new IllegalArgumentException();
    }
}
