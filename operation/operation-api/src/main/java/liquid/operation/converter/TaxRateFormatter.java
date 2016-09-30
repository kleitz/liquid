package liquid.operation.converter;

import liquid.operation.domain.TaxRate;
import liquid.operation.service.TaxRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by mat on 9/30/16.
 */
@Component
public class TaxRateFormatter implements Formatter<TaxRate> {
    private static final Logger logger = LoggerFactory.getLogger(TaxRateFormatter.class);

    @Autowired
    private TaxRateService taxRateService;

    @Override
    public TaxRate parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            logger.warn("Container subtype id '{}' is illegal.", text);
            return null;
        }
        return taxRateService.find(Long.valueOf(text));
    }

    @Override
    public String print(TaxRate object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
