package liquid.transport.converter;

import liquid.transport.domain.Truck;
import liquid.transport.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by mat on 4/10/16.
 */
@Component
public class TruckFormatter implements Formatter<Truck> {
    @Autowired
    private TruckService truckService;

    @Override
    public Truck parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return truckService.find(Long.valueOf(text));
    }

    @Override
    public String print(Truck truck, Locale locale) {
        if (null == truck) {
            return "";
        }
        return String.valueOf(truck.getId());
    }
}
