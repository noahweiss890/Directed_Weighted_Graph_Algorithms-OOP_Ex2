import api.*;

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
    }

    @org.junit.jupiter.api.Test
    void edgeIter() {
    }

    @org.junit.jupiter.api.Test
    void testEdgeIter() {
    }

    @org.junit.jupiter.api.Test
    void removeNode() {
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
    }
}