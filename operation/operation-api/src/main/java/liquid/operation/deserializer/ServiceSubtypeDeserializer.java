package liquid.operation.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

/**
 * Created by Tao Ma on 6/10/15.
 */
@Component
public class ServiceSubtypeDeserializer extends JsonDeserializer<ServiceSubtype> {

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    public ServiceSubtypeDeserializer() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public ServiceSubtype deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jsonParser.getText().trim();
            Long id = Long.parseLong(text);
            return serviceSubtypeService.find(id);
        }
        throw new IllegalArgumentException();
    }
}
