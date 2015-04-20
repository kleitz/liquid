package liquid.process.model;

import liquid.transport.domain.BargeContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/20/15.
 */
public class BargeContainerListForm {
    private List<BargeContainer> list;

    public BargeContainerListForm() { }

    public BargeContainerListForm(Iterable<BargeContainer> iterable) {
        this.list = new ArrayList<>();
        for (BargeContainer container : iterable) {
            this.list.add(container);
        }
    }

    public List<BargeContainer> getList() {
        return list;
    }

    public void setList(List<BargeContainer> list) {
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
