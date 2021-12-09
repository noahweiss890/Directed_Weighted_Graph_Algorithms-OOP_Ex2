import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MyDirectedWeightedGraphAlgorithmsTest {
    MyDirectedWeightedGraphAlgorithms graph = (MyDirectedWeightedGraphAlgorithms)Ex2.getGrapgAlgo("data/G1.json");
    MyDirectedWeightedGraphAlgorithms graph2 = (MyDirectedWeightedGraphAlgorithms)Ex2.getGrapgAlgo("data/mygraph2.json");
    @Test
    void copy() {
        DirectedWeightedGraph copied_graph = graph.copy();
        assertFalse(copied_graph == graph.getGraph());
    }

    @Test
    void isConnected() {
        assertEquals(graph.isConnected(), true);
        assertEquals(graph2.isConnected(), false);
    }

    @Test
    void shortestPathDist() {
        double ans = (1.7938753352369698 + 1.237565124536135 + 1.3717352984705653);
        assertEquals(ans, graph.shortestPathDist(2,8));
        assertEquals(5, graph2.shortestPathDist(0,4));
    }

    @Test
    void shortestPath() {
        NodeData n2 = graph.getGraph().getNode(2);
        NodeData n6 = graph.getGraph().getNode(6);
        NodeData n7 = graph.getGraph().getNode(7);
        NodeData n8 = graph.getGraph().getNode(8);
        ArrayList<NodeData> path = new ArrayList<>();
        path.add(0, n2);
        path.add(1, n6);
        path.add(2, n7);
        path.add(3, n8);
        NodeData n0 = graph2.getGraph().getNode(0);
        NodeData n3 = graph2.getGraph().getNode(3);
        NodeData n4 = graph2.getGraph().getNode(4);
        ArrayList<NodeData> path2 = new ArrayList<>();
        path2.add(0, n0);
        path2.add(1, n3);
        path2.add(2, n4);
        assertEquals(path, graph.shortestPath(2,8));
        assertEquals(path2, graph2.shortestPath(0,4));


    }

    @Test
    void center() {
        assertEquals(8, graph.center().getKey());
        assertEquals(null,graph2.center());
    }

    @Test
    void tsp() {
        ArrayList<NodeData> path = new ArrayList<>();
        path.add(0, graph.getGraph().getNode(16));
        path.add(1, graph.getGraph().getNode(0));
        path.add(2, graph.getGraph().getNode(1));
        path.add(3, graph.getGraph().getNode(2));
        path.add(4, graph.getGraph().getNode(3));
        path.add(5, graph.getGraph().getNode(4));
        ArrayList<NodeData> cities = new ArrayList<>();
        cities.add(graph.getGraph().getNode(0));
        cities.add(graph.getGraph().getNode(4));
        cities.add(graph.getGraph().getNode(16));
        cities.add(graph.getGraph().getNode(3));
        assertEquals(path, graph.tsp(cities));
    }

    @Test
    void tspDynamic() {
        ArrayList<NodeData> path = new ArrayList<>();
        path.add(0, graph.getGraph().getNode(0));
        path.add(1, graph.getGraph().getNode(16));
        path.add(2, graph.getGraph().getNode(0));
        path.add(3, graph.getGraph().getNode(1));
        path.add(4, graph.getGraph().getNode(2));
        path.add(5, graph.getGraph().getNode(3));
        path.add(6, graph.getGraph().getNode(4));
        ArrayList<NodeData> cities = new ArrayList<>();
        cities.add(graph.getGraph().getNode(0));
        cities.add(graph.getGraph().getNode(4));
        cities.add(graph.getGraph().getNode(16));
        cities.add(graph.getGraph().getNode(3));
        assertEquals(path, graph.tspLong(cities));
    }

    @Test
    void save() {

        assertEquals(graph.save("data/saveSpot.json"), true);
    }

    @Test
    void load() {
        DirectedWeightedGraphAlgorithms alg1 = new MyDirectedWeightedGraphAlgorithms();
        alg1.load("data/G1.json");
        Iterator<NodeData> nit = alg1.getGraph().nodeIter();
        assertEquals(nit.hasNext(), true);

    }
}