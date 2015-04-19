package liquid.process.model;

import liquid.transport.domain.RailContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/18/15.
 */
public class RailContainerListForm {
    List<RailContainer> list;

    public RailContainerListForm() { }

    public RailContainerListForm(List<RailContainer> list) {
        this.list = list;
    }

    public RailContainerListForm(Iterable<RailContainer> iterable) {
        this.list = new ArrayList<>();
        for (RailContainer railContainer : iterable) {
            this.list.add(railContainer);
        }
    }

    public List<RailContainer> getList() {
        return list;
    }

    public void setList(List<RailContainer> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=RailContainerListForm");
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
