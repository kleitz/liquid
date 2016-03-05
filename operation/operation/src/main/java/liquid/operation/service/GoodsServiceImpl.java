package liquid.operation.service;

import liquid.operation.domain.Goods;
import liquid.operation.domain.Goods_;
import liquid.operation.repository.GoodsRepository;
import liquid.core.service.AbstractCachedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by redbrick9 on 7/1/14.
 */
@Service
public class GoodsServiceImpl extends AbstractCachedService<Goods, GoodsRepository>
        implements InternalGoodsService {
    @Override
    public void doSaveBefore(Goods entity) { }

    @Override
    public Page<Goods> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Goods> findAll(String name, Pageable pageable) {
        List<Specification<Goods>> specList = new ArrayList<>();

        if (null != name) {
            Specification<Goods> orderNoSpec = new Specification<Goods>() {
                @Override
                public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(Goods_.name), "%" + name + "%");
                }
            };
            specList.add(orderNoSpec);
        }

        if(specList.size() == 0) {
            return repository.findAll(pageable);
        } else {
            Specifications<Goods> specs = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specs.and(specList.get(i));
            }
            return repository.findAll(specs, pageable);
        }
    }
}
