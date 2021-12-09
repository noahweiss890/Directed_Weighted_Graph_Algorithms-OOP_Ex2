import api.*;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MyDirectedWeightedGraphTest {

    MyDirectedWeightedGraphAlgorithms graph = (MyDirectedWeightedGraphAlgorithms)Ex2.getGrapgAlgo("data/G1.json");

    @org.junit.jupiter.api.Test
    void copy() {
        MyDirectedWeightedGraph other = (MyDirectedWeightedGraph)graph.copy();
        assertNotEquals(other.getEdge(0,1), graph.getGraph().getEdge(0,1));
        assertNotEquals(other.getNode(0), graph.getGraph().getNode(0));
    }

    @org.junit.jupiter.api.Test
    void addNode() {
        Node n = new Node("0,1,2",0);
        graph.getGraph().addNode(n);
        assertEquals(graph.getGraph().getNode(0), n);
    }

    @org.junit.jupiter.api.Test
    void connect() {
        graph.getGraph().connect(0,3,2.2);
        assertEquals(graph.getGraph().getEdge(0,3).getWeight(), 2.2);
    }

    @org.junit.jupiter.api.Test
    void nodeIter() {
       DirectedWeightedGraph g1 = new MyDirectedWeightedGraph();
        ArrayList<NodeData> g1Nodes = new ArrayList<>();
       Node n1 = new Node("0,1,0",0);
       Node n2 = new Node("3,1,0",1);
       Node n3 = new Node("5,1,0",2);
       Node n4 = new Node("4,1,0",3);
       Node n5 = new Node("2,1,0",4);
       g1.addNode(n1);
       g1Nodes.add(n1);
       g1.addNode(n2);
       g1Nodes.add(n2);
       g1.addNode(n3);
       g1Nodes.add(n3);
       g1.addNode(n4);
       g1Nodes.add(n4);
       g1.addNode(n5);
       g1Nodes.add(n5);
       Iterator<NodeData> nIt = g1.nodeIter();
       while(nIt.hasNext()){
           NodeData n = nIt.next();
           assertTrue(g1Nodes.contains(n));
       }
        nIt.forEachRemaining((node) -> node.setInfo("done"));
        while(nIt.hasNext()){
            NodeData n = nIt.next();
            assertEquals("done", n.getInfo());
        }
       while(nIt.hasNext()){
           NodeData n = nIt.next();
           nIt.remove();
           assertFalse(g1Nodes.size() == g1.nodeSize());
       }
        g1.removeNode(0);
       RuntimeException thrown = assertThrows(RuntimeException.class, () -> {nIt.next();});
    }

    @org.junit.jupiter.api.Test
    void edgeIter() {
        DirectedWeightedGraph g1 = new MyDirectedWeightedGraph();
        ArrayList<EdgeData> g1Edges = new ArrayList<>();
        Node n1 = new Node("0,1,0",0);
        Node n2 = new Node("3,1,0",1);
        Node n3 = new Node("5,1,0",2);
        Node n4 = new Node("4,1,0",3);
        Edge e1 = new Edge(0,1,.5);
        Edge e2 = new Edge(3,1,1);
        Edge e3 = new Edge(2,3,1.5);
        Edge e4 = new Edge(0,3,2.5);
        g1.addNode(n1);
        g1.addNode(n2);
        g1.addNode(n3);
        g1.addNode(n4);
        g1.connect(0,1,.5);
        g1.connect(3,1,1);
        g1.connect(2,3,1.5);
        g1.connect(0,3,2.5);
        g1Edges.add(e1);
        g1Edges.add(e2);
        g1Edges.add(e3);
        g1Edges.add(e4);
        Iterator<EdgeData> eIt = g1.edgeIter();
        eIt.forEachRemaining((edge) -> edge.setInfo("done"));
        while(eIt.hasNext()){
            EdgeData e = eIt.next();
            assertEquals("done", e.getInfo());
        }
        while(eIt.hasNext()){
            EdgeData e = eIt.next();
            eIt.remove();
            assertFalse(g1Edges.size() == g1.edgeSize());
        }
        g1.removeEdge(0,1);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {eIt.next();});
    }

    @org.junit.jupiter.api.Test
    void testEdgeIter() {
        DirectedWeightedGraph g1 = new MyDirectedWeightedGraph();
        ArrayList<EdgeData> g1Edges = new ArrayList<>();
        NodeData n1 = new Node("0,1,0", 0);
        NodeData n2 = new Node("1,2,0", 1);
        NodeData n3 = new Node("1,3,0", 2);
        g1.addNode(n1);
        g1.addNode(n2);
        g1.addNode(n3);
        g1.connect(0, 2,5);
        g1.connect(0,1,1);
        Iterator<EdgeData> eIt = g1.edgeIter(n1.getKey());
        eIt.forEachRemaining((edge) -> edge.setInfo("done"));
        while(eIt.hasNext()){
            EdgeData e = eIt.next();
            assertEquals("done", e.getInfo());
        }
        while(eIt.hasNext()){
            EdgeData e = eIt.next();
            eIt.remove();
            assertFalse(g1Edges.size() == g1.edgeSize());
        }
        g1.removeEdge(0,1);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {eIt.next();});



    }

    @org.junit.jupiter.api.Test
    void removeNode() {
       int nodeSize = graph.getGraph().nodeSize();
       NodeData n0 = graph.getGraph().getNode(0);
       graph.getGraph().removeNode(0);
       assertFalse(nodeSize == graph.getGraph().nodeSize());
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        int edgeSize = graph.getGraph().edgeSize();
        EdgeData e = graph.getGraph().getEdge(0,16);
        graph.getGraph().removeEdge(0,16);
        assertFalse(edgeSize == graph.getGraph().edgeSize());

    }
}