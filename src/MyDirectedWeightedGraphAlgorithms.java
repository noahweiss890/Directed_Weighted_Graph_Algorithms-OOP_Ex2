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
    public double shortestPathDist(int src, int dest) {
        PriorityQueue<Node> minWeight = new PriorityQueue<Node>(graph.nodeSize(), new NodeComparator());
        Node srcNode = (Node) graph.getNode(src);
        srcNode.setWeight(0);
        minWeight.add(srcNode);
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
            return -1;
        }
        return graph.getNode(dest).getWeight();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        PriorityQueue<Node> minWeight = new PriorityQueue<Node>(graph.nodeSize(), new NodeComparator());
        Node srcNode = (Node) graph.getNode(src);
        srcNode.setWeight(0);
        minWeight.add(srcNode);
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
    public NodeData center() {
        return null;
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
