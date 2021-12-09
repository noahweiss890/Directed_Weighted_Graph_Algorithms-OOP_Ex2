import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

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
        while (nIter.hasNext()) {
            temp.addNode(((Node) nIter.next()).copy());
        }
        while (eIter.hasNext()) {
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
        return ((Node) nodes.get(src)).getOutEdge(dest);
    }

    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey(), n);
        mc++;
    }

    @Override
    public void connect(int src, int dest, double w) {  // adds a directed edge between src and dest
        Edge e = new Edge(src, dest, w);
        edges.put(src + "" + dest, e);
        ((Node) nodes.get(src)).addOutEdge(e);
        ((Node) nodes.get(dest)).addInEdge(e);
        mc++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        ArrayList<NodeData> nodesTemp = new ArrayList<>(nodes.values());
        return new Iterator<NodeData>() {
            Iterator<NodeData> nIt = nodesTemp.iterator();
            int mc_original = mc;
            @Override
            public boolean hasNext() {
                if (mc_original != mc) {
                    throw new RuntimeException();
                }
                return nIt.hasNext();
            }

            @Override
            public NodeData next() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                return nIt.next();
            }

            @Override
            public void remove() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                mc_original++; //so that iterator will not throw runtime exception
                NodeData n = nIt.next();
                removeNode(n.getKey());
            }

            @Override
            public void forEachRemaining(Consumer<? super NodeData> action) {
                if(mc_original !=mc){
                    throw new RuntimeException();
                }
                nIt.forEachRemaining(action);
            }
        };
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        ArrayList<EdgeData> edgesTemp = new ArrayList<>(edges.values());
        return new Iterator<EdgeData>() {
            Iterator<EdgeData> eIt = edgesTemp.iterator();
            int mc_original = mc;

            @Override
            public boolean hasNext() {
                if (mc_original != mc) {
                    throw new RuntimeException();
                }
                return eIt.hasNext();
            }

            @Override
            public EdgeData next() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                return eIt.next();
            }

            @Override
            public void remove() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                mc_original++;
                EdgeData edge = eIt.next();
                removeEdge(edge.getSrc(), edge.getDest());
            }

            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                if(mc_original !=mc){
                    throw new RuntimeException();
                }
                eIt.forEachRemaining(action);
            }
        };
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {  // returns an iterator of the out edges on the given node id
        ArrayList<EdgeData> outE = new ArrayList<>(((Node) nodes.get(node_id)).getOutEdges().values());
        return new Iterator<EdgeData>() {
            Iterator<EdgeData> oIt = outE.iterator();
            int mc_original = mc;
            @Override
            public boolean hasNext() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                return oIt.hasNext();
            }

            @Override
            public EdgeData next() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                return oIt.next();
            }

            @Override
            public void remove() {
                if(mc_original != mc){
                    throw new RuntimeException();
                }
                mc_original++;
                EdgeData edge = oIt.next();
                removeEdge(edge.getSrc(), edge.getDest());
            }

            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                if(mc_original !=mc){
                    throw new RuntimeException();
                }
                oIt.forEachRemaining(action);
            }
        };
    }

    @Override
    public NodeData removeNode(int key) {  // removes the node and all of the edges that is attached to it including from all of its neighbor's in and out edge lists
        Node gone = (Node) nodes.remove(key);  // removed node
        Iterator<EdgeData> outEdgeIter = gone.getOutEdges().values().iterator();
        Iterator<EdgeData> inEdgeIter = gone.getInEdges().values().iterator();
        while (outEdgeIter.hasNext()) {  // go through all of the removed nodes out edges and delete the edge from the neighbors in edge list
            EdgeData e = outEdgeIter.next();
            EdgeData toDel = ((Node) nodes.get(e.getDest())).getInEdges().remove(key);
            edges.remove(toDel.getSrc() + "" + toDel.getDest());
        }
        while (inEdgeIter.hasNext()) {  // go through all of the removed nodes in edges and delete the edge from the neighbors out edge list
            EdgeData e = inEdgeIter.next();
            EdgeData toDel = ((Node) nodes.get(e.getSrc())).getOutEdges().remove(key);
            edges.remove(toDel.getSrc() + "" + toDel.getDest());
        }
        mc++;  // increment because a change has been made
        return gone;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {  // removes edge from the src's out edge list and from the dest's in edge list
        ((Node) nodes.get(src)).getOutEdges().remove(dest);
        ((Node) nodes.get(dest)).getInEdges().remove(src);
        mc++;  // increment because a change has been made
        return edges.remove(src + "" + dest);
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
