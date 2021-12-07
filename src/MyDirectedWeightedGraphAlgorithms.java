import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;
import org.w3c.dom.traversal.NodeIterator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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
        u.setTag(2);  //color is black
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
    //O(ElogV)
    public double shortestPathDist(int src, int dest) {
        if(src == dest){
            return 0;
        }
        PriorityQueue<Node> minWeight = new PriorityQueue<Node>(graph.nodeSize(), new NodeComparator());
        Node srcNode = (Node) graph.getNode(src);
        srcNode.setWeight(0); //  O(1)
        minWeight.offer(srcNode); //O(logV)
        Iterator<NodeData> nIterator = graph.nodeIter(); //O(1)
        while (nIterator.hasNext()) { //O(V)
            Node n = (Node) nIterator.next();
            if (n.getKey() != srcNode.getKey()) {
                n.setWeight(Double.MAX_VALUE);
                n.setPrev(null);
                minWeight.offer(n); //O(logV)
            }
        }
        //O(V+E) * O(logV) = O(ElogV)
        while (!minWeight.isEmpty()) {
            if(minWeight.peek() == graph.getNode(dest)){
                return minWeight.peek().getWeight();
            }
            if(minWeight.peek().getWeight() == Double.MAX_VALUE){
                return -1;
            }
            Node curr = minWeight.poll(); //O(logV)
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) {
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight());
                    neighbor.setPrev(curr);
                    minWeight.offer(neighbor); //O(logv)
                }
            }
        }
//        if(graph.getNode(dest).getWeight() == Double.MAX_VALUE){
//            return -1;
//        }
        return graph.getNode(dest).getWeight();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        ArrayList<NodeData> path = new ArrayList<NodeData>();
        if(src == dest){
            path.add(graph.getNode(src));
            return path;
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
            if(minWeight.peek() == graph.getNode(dest)){
                break;
            }
            if(minWeight.peek().getWeight() == Double.MAX_VALUE){
                return null;
            }
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
        HashMap<Integer, NodeData> myCities = new HashMap<Integer, NodeData>();
        for (int i = 0; i < cities.size(); i++) {
            myCities.put(cities.get(i).getKey(), cities.get(i));
        }
        HashMap<String, Double> distances = new HashMap<String, Double>();
        for (int i: myCities.keySet()) {
            for (int j: myCities.keySet()) {
                distances.put(myCities.get(i).getKey() + "->" + myCities.get(j).getKey(), shortestPathDist(myCities.get(i).getKey(), myCities.get(j).getKey()));
            }
        }
        double minWeight = Double.MAX_VALUE, weight;
        ArrayList<NodeData> minPath = new ArrayList<NodeData>();
        for (Map.Entry<Integer, NodeData> n: myCities.entrySet()) {  // who is the starting city?
            HashMap<Integer, NodeData> temp = (HashMap<Integer, NodeData>)myCities.clone();
            temp.remove(n.getKey());
            ArrayList<NodeData> tempPath = new ArrayList<NodeData>();
            tempPath.add(n.getValue());
            weight = tspHelper(tempPath, distances, temp, n.getKey(), n.getKey());
            if(weight < minWeight) {
                minWeight = weight;
                minPath = tempPath;
            }
        }
        System.out.println("DONE!");
        System.out.println("the tsp weight is: " + minWeight);
        ArrayList<NodeData> path = new ArrayList<NodeData>();
        for (int i = 0; i < minPath.size()-1; i++) {
            System.out.println("i: " + i);
            path.addAll(shortestPath(minPath.get(i).getKey(), minPath.get(i+1).getKey()));
        }
        return path;
    }

    private double tspHelper(ArrayList<NodeData> path, HashMap<String, Double> distances, HashMap<Integer, NodeData> cities, int start, int curr) {
        if(cities.size() == 0) {
            return distances.get(curr + "->" + start);
        }
        double minWeight = Double.MAX_VALUE, weight;
        NodeData ans = null;
        for(Map.Entry<Integer, NodeData> n: cities.entrySet()) {
            HashMap<Integer, NodeData> temp = (HashMap<Integer, NodeData>)cities.clone();
            temp.remove(n.getKey());
            weight = distances.get(curr + "->" + n.getKey()) + tspHelper(path, distances, temp, start, n.getKey());
            if(weight < minWeight) {
                minWeight = weight;
                ans = n.getValue();
            }
        }
        path.add(1, ans);
        return minWeight;
    }

    @Override
    public boolean save(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(MyDirectedWeightedGraph.class, new DirectedWeightedGraphSerializer());
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String json = gson.toJson(this.graph);
            FileWriter fw = new FileWriter(file);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        DirectedWeightedGraph newGraph;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(MyDirectedWeightedGraph.class, new DirectedWeightedGraphDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            newGraph = gson.fromJson(reader, MyDirectedWeightedGraph.class);
            this.init(newGraph);
        }
        catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "MyDirectedWeightedGraphAlgorithms{" +
//                "graph=" + graph +
//                '}';
//    }
}
