package liquid.accounting.model;

import liquid.accounting.domain.TaxRate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
public class TaxRates {
    private Collection<TaxRate> list = new ArrayList<>();

    public Collection<TaxRate> getList() {
        return list;
    }

    public void setList(Collection<TaxRate> list) {
        this.list = list;
    }
}
