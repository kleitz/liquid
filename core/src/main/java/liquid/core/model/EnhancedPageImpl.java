package liquid.core.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Tao Ma on 1/14/15.
 */
public class EnhancedPageImpl<T> extends PageImpl<T> {
    private T sum;

    public EnhancedPageImpl(List<T> content, Pageable pageable, long total, T sum) {
        super(content, pageable, total);
        this.sum = sum;
    }

    public T getSum() {
        return sum;
    }

    public void setSum(T sum) {
        this.sum = sum;
    }
}
