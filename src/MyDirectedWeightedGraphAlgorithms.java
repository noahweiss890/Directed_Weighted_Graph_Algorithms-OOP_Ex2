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
