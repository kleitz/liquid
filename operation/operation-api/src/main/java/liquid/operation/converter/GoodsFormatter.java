package liquid.operation.converter;

import liquid.operation.domain.Goods;
import liquid.operation.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 4/30/15.
 */
@Component
public class GoodsFormatter implements Formatter<Goods> {

    @Autowired
    private GoodsService goodsService;

    @Override
    public Goods parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return goodsService.find(Long.valueOf(text));
    }

    @Override
    public String print(Goods object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
