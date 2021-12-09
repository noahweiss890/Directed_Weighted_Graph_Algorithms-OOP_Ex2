import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyDirectedWeightedGraph implements DirectedWeightedGraph {

    private HashMap<String, EdgeData> edges;
    private HashMap<Integer, NodeData> nodes;
    private int mc;

    public MyDirectedWeightedGraph() {  // constructor
        edges = new HashMap<>();
        nodes = new HashMap<>();
        mc = 0;
    }

    public DirectedWeightedGraph copy() {  // deep copy
        MyDirectedWeightedGraph temp = new MyDirectedWeightedGraph();
        Iterator<NodeData> nIter = this.nodeIter();
        Iterator<EdgeData> eIter = this.edgeIter();
        while(nIter.hasNext()) {
            temp.addNode(((Node)nIter.next()).copy());
        }
        while(eIter.hasNext()) {
            EdgeData e = eIter.next();
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
        nodes.put(n.getKey(), n);
        mc++;
    }

    @Override
    public void connect(int src, int dest, double w) {  // adds a directed edge between src and dest
        Edge e = new Edge(src, dest, w);
        edges.put(src+""+dest, e);
        ((Node)nodes.get(src)).addOutEdge(e);
        ((Node)nodes.get(dest)).addInEdge(e);
        mc++;
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
    public Iterator<EdgeData> edgeIter(int node_id) {  // returns an iterator of the out edges on the given node id
        return ((Node)nodes.get(node_id)).getOutEdges().values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {  // removes the node and all of the edges that is attached to it including from all of its neighbor's in and out edge lists
        Node gone = (Node)nodes.remove(key);  // removed node
        Iterator<EdgeData> outEdgeIter = gone.getOutEdges().values().iterator();
        Iterator<EdgeData> inEdgeIter = gone.getInEdges().values().iterator();
        while(outEdgeIter.hasNext()) {  // go through all of the removed nodes out edges and delete the edge from the neighbors in edge list
            EdgeData e = outEdgeIter.next();
            EdgeData toDel = ((Node)nodes.get(e.getDest())).getInEdges().remove(key);
            edges.remove(toDel.getSrc() + "" + toDel.getDest());
        }
        while(inEdgeIter.hasNext()) {  // go through all of the removed nodes in edges and delete the edge from the neighbors out edge list
            EdgeData e = inEdgeIter.next();
            EdgeData toDel = ((Node)nodes.get(e.getSrc())).getOutEdges().remove(key);
            edges.remove(toDel.getSrc() + "" + toDel.getDest());
        }
        mc++;  // increment because a change has been made
        return gone;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {  // removes edge from the src's out edge list and from the dest's in edge list
        ((Node)nodes.get(src)).getOutEdges().remove(dest);
        ((Node)nodes.get(dest)).getInEdges().remove(src);
        mc++;  // increment because a change has been made
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

    public void setMC(int newMC) {
        this.mc = newMC;
    }

    public ArrayList<EdgeData> getEdgesArray() {
        return new ArrayList<>(edges.values());
    }

    @Override
    public String toString() {
        return "MyDirectedWeightedGraph{" +
                "nodes=" + nodes +
                '}';
    }
}
