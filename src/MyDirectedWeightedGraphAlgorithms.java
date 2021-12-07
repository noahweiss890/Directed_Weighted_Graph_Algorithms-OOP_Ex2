import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import org.w3c.dom.traversal.NodeIterator;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class MyDirectedWeightedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph graph;

    public MyDirectedWeightedGraphAlgorithms() {
        graph = null;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        graph = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return ((MyDirectedWeightedGraph) graph).copy();
    }


    public void DFS_visit(DirectedWeightedGraph g, NodeData u){
        u.setTag(1); //color is grey
        Iterator<EdgeData> eIterator = g.edgeIter(u.getKey());
        while(eIterator.hasNext()){
            EdgeData vEdge = eIterator.next();
            int vKey = vEdge.getDest();
            Node v = (Node) g.getNode(vKey);
            if(v.getTag() == 0){
                DFS_visit(g, v);
            }
        }
        u.setTag(2);//color is black
    }

    //receives a graph and returns the transpose of that graph
    public DirectedWeightedGraph Transpose(MyDirectedWeightedGraph g) {
        DirectedWeightedGraph gt = g.copy();
        Iterator<EdgeData> edgeIt = g.edgeIter();
        while (edgeIt.hasNext()) {
            EdgeData eIt = edgeIt.next();
          //  gt.removeEdge(eIt.getSrc(), eIt.getDest());
            gt.connect(eIt.getDest(), eIt.getSrc(), eIt.getWeight());
        }
        return gt;
    }

    @Override
    public boolean isConnected() {
        NodeData n = graph.getNode(0); //any node to start
        DFS_visit(graph, n);
        Iterator<NodeData> nIterator = graph.nodeIter(); //goes through all nodes in graph
        while (nIterator.hasNext()) {
            NodeData gNode = nIterator.next();
            if (gNode.getTag() == 0) //color of node is white, so hasn't been touched
            {
                return false; //not connected
            }
        }
            Iterator<NodeData> nIterator2 = graph.nodeIter(); //goes through all nodes in graph
            while (nIterator2.hasNext()) {
                NodeData gNode2 = nIterator2.next();
                gNode2.setTag(0); //resets to zero
            }
            DirectedWeightedGraph gt = Transpose((MyDirectedWeightedGraph) graph); //tranposes the graph
            DFS_visit(gt, n); //dfs again
            Iterator<NodeData> gtIterator = gt.nodeIter(); //goes through all nodes in graph
            while (gtIterator.hasNext()) {
                NodeData gtNode = gtIterator.next();
                if (gtNode.getTag() == 0) //color of node is white, so hasn't been touched
                {
                    return false; //not connected
                }
            }
            return true;
        }

    @Override
    //O(n^2logn)
    public double shortestPathDist(int src, int dest) {
        if(src == dest){
            return 0;
        }
        PriorityQueue<Node> minWeight = new PriorityQueue<Node>(graph.nodeSize(), new NodeComparator());
        Node srcNode = (Node) graph.getNode(src);
        srcNode.setWeight(0); //  O(1)
        minWeight.offer(srcNode); //O(logn)
        Iterator<NodeData> nIterator = graph.nodeIter(); //O(1)
        while (nIterator.hasNext()) { //O(n)
            Node n = (Node) nIterator.next();
            if (n.getKey() != srcNode.getKey()) {
                n.setWeight(Double.MAX_VALUE); //O(1)
                n.setPrev(null); //O(1)
                minWeight.offer(n); //O(logn)
            }
        }

        while (!minWeight.isEmpty()) { //O(n)
            Node curr = minWeight.poll(); //O(logn)
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) { //O(n)
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight());
                    neighbor.setPrev(curr);
                    minWeight.offer(neighbor); //O(logn)
                }
            }
        }
        if(graph.getNode(dest).getWeight() == Double.MAX_VALUE){
            return -1;
        }
        return graph.getNode(dest).getWeight();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if(src == dest){
            return null;
        }
        PriorityQueue<Node> minWeight = new PriorityQueue<Node>(graph.nodeSize(), new NodeComparator());
        Node srcNode = (Node) graph.getNode(src);
        srcNode.setWeight(0);
        minWeight.offer(srcNode);
        Iterator<NodeData> nIterator = graph.nodeIter();
        while (nIterator.hasNext()) {
            Node n = (Node) nIterator.next();
            if (n.getKey() != srcNode.getKey()) {
                n.setWeight(Double.MAX_VALUE);
                n.setPrev(null);
                minWeight.offer(n);
            }
        }

        while (!minWeight.isEmpty()) {
            Node curr = minWeight.poll();
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) {
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight());
                    neighbor.setPrev(curr);
                    minWeight.offer(neighbor);
                }
            }
        }
        if(graph.getNode(dest).getWeight() == Double.MAX_VALUE){
            return null;
        }
        ArrayList<NodeData> path = new ArrayList<NodeData>();
        Node curNode = (Node) graph.getNode(dest);
        path.add(0, curNode);
        while (curNode.getPrev() != null) {
            path.add(0, curNode.getPrev());
            curNode = curNode.getPrev();
        }
        return path;
    }


    @Override
    //O(n^4logn)
    public NodeData center() {
        DirectedWeightedGraph copied_graph = this.copy();
        if(isConnected() == false) {
            return null;
        }
        //ArrayList<Double> eccentricity_list = new ArrayList<Double>();
        HashMap<Integer, Double> eccentricity_map = new HashMap<>();
        //Iterator<NodeData> nodeIt = this.graph.nodeIter();
        Iterator<NodeData> nodeIt = copied_graph.nodeIter();
        while(nodeIt.hasNext()){ //O(n)
            Node n = (Node) nodeIt.next();
            int key_n = n.getKey();
            Node graph_n = (Node) this.getGraph().getNode(key_n);
            double ecc = 0;
            //Iterator<NodeData> nodeIt2 = this.graph.nodeIter();
            Iterator<NodeData> nodeIt2 = copied_graph.nodeIter();
            while(nodeIt2.hasNext()){
                Node u = (Node) nodeIt2.next();
                int key_u = u.getKey();
                Node graph_u = (Node) this.getGraph().getNode(key_u);
                double shortest_dist = shortestPathDist(graph_n.getKey(),graph_u.getKey()); //O(n^2logn)
                if(shortest_dist > ecc){
                    ecc = shortest_dist;
                }
            }
            eccentricity_map.put(n.getKey(), ecc); //O(1)
        }
        Map.Entry<Integer, Double> min = null;
        for (Map.Entry<Integer, Double> entry : eccentricity_map.entrySet()) { //O(n)
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return copied_graph.getNode(min.getKey());
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
