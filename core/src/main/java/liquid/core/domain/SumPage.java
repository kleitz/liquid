package liquid.core.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 5/1/15.
 */
public class SumPage<T> extends PageImpl<T> {
    private T sum;

    public SumPage(Page<T> page, T sum, Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());
        this.sum = sum;
    }

    public T getSum() {
        return sum;
    }

    public void setSum(T sum) {
        this.sum = sum;
    }
}

