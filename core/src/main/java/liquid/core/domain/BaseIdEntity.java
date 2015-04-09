package liquid.core.domain;

import javax.persistence.*;

/**
 * Created by redbrick9 on 6/10/14.
 */
@MappedSuperclass
public class BaseIdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static <E extends BaseIdEntity> E newInstance(Class<E> clazz, Long id) {
        try {
            E entity = clazz.newInstance();
            entity.setId(id);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new EntityInstantiationException(e);
        }
    }
}
