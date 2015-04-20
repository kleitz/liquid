package liquid.process.model;

import liquid.transport.domain.VesselContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/20/15.
 */
public class VesselContainerListForm {
    private List<VesselContainer> list;

    public VesselContainerListForm() { }

    public VesselContainerListForm(Iterable<VesselContainer> iterable) {
        this.list = new ArrayList<>();
        for (VesselContainer container : iterable) {
            list.add(container);
        }
    }

    public List<VesselContainer> getList() {
        return list;
    }

    public void setList(List<VesselContainer> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=VesselContainerListForm");
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
