import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyDirectedWeightedGraph implements DirectedWeightedGraph {

    private HashMap<Integer, Node> nodes;

    public MyDirectedWeightedGraph() {
        nodes = new HashMap<Integer, Node>();
    }

    @Override
    public NodeData getNode(int key) {
            return nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return nodes.get(src).getOutEdges(dest);
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), (Node) n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        Edge e = new Edge(src, dest, w);
        nodes.get(src).addOutEdges(e);
        nodes.get(dest).addInEdges(e);

    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }

}
