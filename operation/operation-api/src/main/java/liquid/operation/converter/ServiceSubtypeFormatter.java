package liquid.operation.converter;

import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/24/15.
 */
@Component
public class ServiceSubtypeFormatter implements Formatter<ServiceSubtype> {
    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Override
    public ServiceSubtype parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return serviceSubtypeService.find(Long.valueOf(text));
    }

    @Override
    public String print(ServiceSubtype object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
