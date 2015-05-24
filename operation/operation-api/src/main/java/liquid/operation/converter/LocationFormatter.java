package liquid.operation.converter;

import liquid.operation.domain.Location;
import liquid.operation.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/19/15.
 */
@Component
public class LocationFormatter implements Formatter<Location> {

    @Autowired
    private LocationService locationService;

    @Override
    public Location parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return locationService.find(Long.valueOf(text));
    }

    @Override
    public String print(Location object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
