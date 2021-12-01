import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyDirectedWeightedGraph implements DirectedWeightedGraph {

    private HashMap<Integer, NodeData> nodes;
    private HashMap<String, EdgeData> edges;
    private int mc;

    public MyDirectedWeightedGraph() {
        nodes = new HashMap<Integer, NodeData>();
        edges = new HashMap<String, EdgeData>();
        mc = 0;
    }

    public DirectedWeightedGraph copy() {
        MyDirectedWeightedGraph temp = new MyDirectedWeightedGraph();
        Iterator<NodeData> nIter = this.nodeIter();
        Iterator<EdgeData> eIter = this.edgeIter();
        while(nIter.hasNext()) {
            temp.addNode(((Node)nIter.next()).copy());
        }
        while(eIter.hasNext()) {
            EdgeData e = (Edge)eIter.next();
            temp.connect(e.getSrc(), e.getDest(), e.getWeight());
        }
        temp.mc = 0;
        return temp;
    }

    @Override
    public NodeData getNode(int key) {
            return nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return ((Node)nodes.get(src)).getOutEdge(dest);
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), (Node) n);
        mc++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        Edge e = new Edge(src, dest, w);
        edges.put(src+""+dest, e);
        ((Node)nodes.get(src)).addOutEdge(e);
        ((Node)nodes.get(dest)).addInEdge(e);
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return nodes.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return edges.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return ((Node)nodes.get(node_id)).getOutEdges().values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        Node gone = (Node)nodes.remove(key);
        Iterator<EdgeData> outEdgeIter = gone.getOutEdges().values().iterator();
        Iterator<EdgeData> inEdgeIter = gone.getInEdges().values().iterator();
        while(outEdgeIter.hasNext()) {
            EdgeData e = outEdgeIter.next();
            ((Node)nodes.get(e.getDest())).getInEdges().remove(key);
        }
        while(inEdgeIter.hasNext()) {
            EdgeData e = inEdgeIter.next();
            ((Node)nodes.get(e.getSrc())).getOutEdges().remove(key);
        }
        mc++;
        return gone;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        ((Node)nodes.get(src)).getOutEdges().remove(dest);
        ((Node)nodes.get(dest)).getInEdges().remove(src);
        mc++;
        return edges.remove(src+""+dest);
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public int getMC() {
        return mc;
    }

    public void setMC(int newMC){
        this.mc = newMC;
    }

    @Override
    public String toString() {
        return "MyDirectedWeightedGraph{" +
                "nodes=" + nodes +
                '}';
    }
}
