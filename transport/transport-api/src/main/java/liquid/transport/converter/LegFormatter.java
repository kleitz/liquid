package liquid.transport.converter;

import liquid.transport.domain.LegEntity;
import liquid.transport.service.LegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 6/7/15.
 */
@Component
public class LegFormatter implements Formatter<LegEntity> {
    @Autowired
    private LegService legService;

    @Override
    public LegEntity parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return legService.find(Long.valueOf(text));
    }

    @Override
    public String print(LegEntity object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
