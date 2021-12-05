import java.util.*;

public class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        if(o1.getWeight() > o2.getWeight()){
            return 1;
        }
        else if (o1.getWeight() < o2.getWeight()){
            return -1;
        }
        return 0;
    }
}
