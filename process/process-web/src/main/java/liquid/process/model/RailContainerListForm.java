package liquid.process.model;

import liquid.transport.domain.RailContainerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/18/15.
 */
public class RailContainerListForm {
    List<RailContainerEntity> list;

    public RailContainerListForm() { }

    public RailContainerListForm(List<RailContainerEntity> list) {
        this.list = list;
    }

    public RailContainerListForm(Iterable<RailContainerEntity> iterable) {
        this.list = new ArrayList<>();
        for (RailContainerEntity railContainerEntity : iterable) {
            this.list.add(railContainerEntity);
        }
    }

    public List<RailContainerEntity> getList() {
        return list;
    }

    public void setList(List<RailContainerEntity> list) {
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
