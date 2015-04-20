package liquid.process.model;

import liquid.transport.domain.BargeContainerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/20/15.
 */
public class BargeContainerListForm {
    private List<BargeContainerEntity> list;

    public BargeContainerListForm() { }

    public BargeContainerListForm(Iterable<BargeContainerEntity> iterable) {
        this.list = new ArrayList<>();
        for (BargeContainerEntity container : iterable) {
            this.list.add(container);
        }
    }

    public List<BargeContainerEntity> getList() {
        return list;
    }

    public void setList(List<BargeContainerEntity> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=BargeContainerListForm");
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
