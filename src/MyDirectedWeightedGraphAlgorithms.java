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


    private void DFS_visit(DirectedWeightedGraph g, NodeData u) {
        Stack<NodeData> DFS_stack = new Stack<>();
        DFS_stack.push(u);
        u.setTag(1); //color is grey
        while (!DFS_stack.isEmpty()) {
            u = DFS_stack.peek();
            DFS_stack.pop();
            u.setTag(2); //color is blck
            Iterator<EdgeData> eIterator = g.edgeIter(u.getKey());
            while (eIterator.hasNext()) {
                EdgeData vEdge = eIterator.next();
                int vKey = vEdge.getDest();
                NodeData v = g.getNode(vKey);
                if (v.getTag() == 0) { //haven't touched this node yet
                    DFS_stack.push(v);
                    v.setTag(1); //now grey
                }
//        Iterator<EdgeData> eIterator = g.edgeIter(u.getKey());
//        while(eIterator.hasNext()){
//            EdgeData vEdge = eIterator.next();
//            int vKey = vEdge.getDest();
//            Node v = (Node) g.getNode(vKey);
//            if(v.getTag() == 0){
//                DFS_visit(g, v);
//            }
//        }
//        u.setTag(2);  //color is black
            }
        }
    }

    //receives a graph and returns the transpose of that graph
    private DirectedWeightedGraph Transpose(MyDirectedWeightedGraph g) {
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
        if (src == dest) {
            return 0;
        }
        PriorityQueue<NodeData> minWeight = new PriorityQueue<NodeData>(graph.nodeSize(), new NodeComparator());
        NodeData srcNode = graph.getNode(src);
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
            if (minWeight.peek().getWeight() == Double.MAX_VALUE) {
                return -1;
            }
            if (minWeight.peek() == graph.getNode(dest)) {
                return minWeight.peek().getWeight();
            }
            NodeData curr = minWeight.poll(); //O(logV)
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) {
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight());
                    neighbor.setPrev((Node)curr);
                    minWeight.remove(neighbor);
                    minWeight.offer(neighbor); //O(logv)
                }
            }
        }
        return graph.getNode(dest).getWeight();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        ArrayList<NodeData> path = new ArrayList<NodeData>();
        if (src == dest) {
            path.add(graph.getNode(src));
            return path;
        }
        PriorityQueue<NodeData> minWeight = new PriorityQueue<NodeData>(graph.nodeSize(), new NodeComparator());
        NodeData srcNode = graph.getNode(src);
        srcNode.setWeight(0);
        srcNode.setPrev(null);
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
            if (minWeight.peek().getWeight() == Double.MAX_VALUE) {
                return null;
            }
            if (minWeight.peek() == graph.getNode(dest)) {
                break;
            }
            NodeData curr = minWeight.poll();
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) {
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight());
                    neighbor.setPrev((Node)curr);
                    minWeight.remove(neighbor);
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
    //O()
    public NodeData center() {
        DirectedWeightedGraph copied_graph = this.copy();
        if (!isConnected()) {
            return null;
        }
        //ArrayList<Double> eccentricity_list = new ArrayList<Double>();
        HashMap<Integer, Double> eccentricity_map = new HashMap<>();
        //Iterator<NodeData> nodeIt = this.graph.nodeIter();
        Iterator<NodeData> nodeIt = copied_graph.nodeIter();
        while (nodeIt.hasNext()) { //O(n)
            Node n = (Node) nodeIt.next();
            int key_n = n.getKey();
            Node graph_n = (Node) this.getGraph().getNode(key_n);
            double ecc = 0;
            //Iterator<NodeData> nodeIt2 = this.graph.nodeIter();
            Iterator<NodeData> nodeIt2 = copied_graph.nodeIter();
            while (nodeIt2.hasNext()) {
                Node u = (Node) nodeIt2.next();
                int key_u = u.getKey();
                Node graph_u = (Node) this.getGraph().getNode(key_u);
                double shortest_dist = shortestPathDist(graph_n.getKey(), graph_u.getKey()); //O(ElogV)
                if (shortest_dist > ecc) {
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
        ArrayList<NodeData> path = new ArrayList<>();
        ArrayList<NodeData> myCities = new ArrayList<>(cities);
        NodeData curr = myCities.get(0);
        do {
            path.add(curr);
            myCities.remove(curr);
            curr = closestNodeFinder(curr, myCities);
        }
        while(myCities.size() > 1);
        path.add(myCities.get(0));
        return path;
    }

    private NodeData closestNodeFinder(NodeData src, ArrayList<NodeData> cities) {
        return null;
    }


//    @Override
//    public List<NodeData> tsp(List<NodeData> cities) {
//        ArrayList<NodeData> myCities = new ArrayList<>(cities);
//        ArrayList<NodeData> path = new ArrayList<>();
//        EdgeData minEdge = null;
//        double minWeight = Double.MAX_VALUE;
//        Iterator<EdgeData> eIter = graph.edgeIter();
//        while (eIter.hasNext()) {
//            EdgeData e = eIter.next();
//            if (e.getWeight() < minWeight) {
//                minWeight = e.getWeight();
//                minEdge = e;
//            }
//        }
//        path.add(graph.getNode(minEdge.getSrc()));
//        path.add(graph.getNode(minEdge.getDest()));
//        myCities.remove(graph.getNode(minEdge.getSrc()));
//        myCities.remove(graph.getNode(minEdge.getDest()));
//        while(!myCities.isEmpty()) {
//            NodeData closest = findClosestNodeToPath(path, myCities);
//        }
//
//        return null;
//    }

//    private NodeData findClosestNodeToPath(ArrayList<NodeData> path, ArrayList<NodeData> myCities) {
//        double minWeight = Double.MAX_VALUE;
//        NodeData ans = null;
//        return null;
//    }

//    @Override
    public List<NodeData> tspBAD(List<NodeData> cities) {
        HashMap<Integer, NodeData> myCities = new HashMap<Integer, NodeData>();
        for (NodeData city : cities) {
            myCities.put(city.getKey(), city);
        }
        HashMap<String, Double> distances = new HashMap<String, Double>();
        for (int i : myCities.keySet()) {
            for (int j : myCities.keySet()) {
                if (i != j) {
                    distances.put(myCities.get(i).getKey() + "->" + myCities.get(j).getKey(), shortestPathDist(myCities.get(i).getKey(), myCities.get(j).getKey()));
                }
            }
        }
        System.out.println("DISTANCES: " + distances);
        double minWeight = Double.MAX_VALUE, weight;
        ArrayList<NodeData> minPath = new ArrayList<NodeData>();
        NodeData ans = null;
        NodeData closestToCenter = closestToCenter(center(), cities);
//        NodeData closestToCenter = cities.get(0);
//        System.out.println(closestToCenter);
//        for (Map.Entry<Integer, NodeData> n: myCities.entrySet()) {  // who is the starting city?
        HashMap<Integer, NodeData> temp = new HashMap<Integer, NodeData>(myCities);
//            temp.remove(n.getKey());
        temp.remove(closestToCenter.getKey());
        ArrayList<NodeData> tempPath = new ArrayList<NodeData>();
        weight = tspHelper(tempPath, distances, temp, closestToCenter.getKey());
        if (weight < minWeight) {
            minWeight = weight;
            minPath = tempPath;
            ans = closestToCenter;
        }
//        }
        minPath.add(ans);
        Collections.reverse(minPath);
        ArrayList<NodeData> path = new ArrayList<NodeData>();
        System.out.println(minWeight);
        System.out.println(minPath);

        for (int i = 0; i < minPath.size() - 1; i++) {
            ArrayList<NodeData> addToPath = (ArrayList<NodeData>) shortestPath(minPath.get(i).getKey(), minPath.get(i + 1).getKey());
            if (i > 0) {
                addToPath.remove(0);
            }
            path.addAll(addToPath);
        }
        return path;
    }

    private NodeData closestNodeFinder(NodeData src, ArrayList<NodeData> cities){
        PriorityQueue<NodeData> minWeight = new PriorityQueue<NodeData>(graph.nodeSize(), new NodeComparator());
        src.setWeight(0); //  O(1)
        ((Node)src).setPrev(null);
        minWeight.offer(src); //O(logV)
        Iterator<NodeData> nIterator = graph.nodeIter(); //O(1)
        while (nIterator.hasNext()) { //O(V)
            Node n = (Node) nIterator.next();
            if (n.getKey() != src.getKey()) {
                n.setWeight(Double.MAX_VALUE);
                n.setPrev(null);
                minWeight.offer(n); //O(logV)
            }
        }
        while (!minWeight.isEmpty()) {
            if (cities.contains(minWeight.peek())) {
                return minWeight.peek();
            }
            NodeData curr = minWeight.poll(); //O(logV)
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) {
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight());
                    neighbor.setPrev((Node) curr);
                    minWeight.remove(neighbor);
                    minWeight.offer(neighbor); //O(logv)
                }
            }
        }
        return null;
    }

    private double tspHelper(ArrayList<NodeData> path, HashMap<String, Double> distances, HashMap<Integer, NodeData> cities, int curr) {
        if (cities.size() == 0) {
            return 0;
        }
        double minWeight = Double.MAX_VALUE, weight;
        NodeData ans = null;
        ArrayList<NodeData> minPath = new ArrayList<NodeData>();
        for (Map.Entry<Integer, NodeData> n : cities.entrySet()) {
            HashMap<Integer, NodeData> temp = new HashMap<Integer, NodeData>(cities);
            temp.remove(n.getKey());
            ArrayList<NodeData> tempPath = new ArrayList<NodeData>(path);
            double dist = distances.get(curr + "->" + n.getKey());
            if (dist != -1) {
                weight = dist + tspHelper(tempPath, distances, temp, n.getKey());
                if (weight < minWeight) {
                    minWeight = weight;
                    ans = n.getValue();
                    path.clear();
                    minPath = tempPath;
                }
            }
        }
        path.addAll(minPath);
        path.add(ans);
        return minWeight;
    }

    private NodeData closestToCenter(NodeData center, List<NodeData> cities) {
        double minWeight = 0, weight;
        NodeData ans = null;
        for (NodeData n : cities) {
            weight = shortestPathDist(center.getKey(), n.getKey());
            if (weight > minWeight) {
                minWeight = weight;
                ans = n;
            }
        }
        return ans;
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
        } catch (FileNotFoundException e) {
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
