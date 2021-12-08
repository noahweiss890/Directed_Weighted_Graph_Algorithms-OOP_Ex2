import api.NodeData;

import java.util.*;

public class NodeComparator implements Comparator<NodeData> {
    @Override
    public int compare(NodeData o1, NodeData o2) {
        if(o1.getWeight() > o2.getWeight()){
            return 1;
        }
        else if (o1.getWeight() < o2.getWeight()){
            return -1;
        }
        return 0;
    }
}
