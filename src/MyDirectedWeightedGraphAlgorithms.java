import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MyDirectedWeightedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph graph;

    public MyDirectedWeightedGraphAlgorithms() {  // constructor
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
    public DirectedWeightedGraph copy() {  // deep copy
        return ((MyDirectedWeightedGraph) graph).copy();
    }

    //The following function iteratively performs a DFS visit on node u from graph g
    private void DFS_visit(DirectedWeightedGraph g, NodeData u) {
        Stack<NodeData> DFS_stack = new Stack<>();
        DFS_stack.push(u);
        u.setTag(1); //color is grey
        while (!DFS_stack.isEmpty()) {
            u = DFS_stack.peek();
            DFS_stack.pop();
            u.setTag(2); //color is black
            Iterator<EdgeData> eIterator = g.edgeIter(u.getKey()); //iterate over neighbor nodes
            while (eIterator.hasNext()) {
                EdgeData vEdge = eIterator.next();
                int vKey = vEdge.getDest();
                NodeData v = g.getNode(vKey);
                if (v.getTag() == 0) { //haven't touched this node yet
                    DFS_stack.push(v);
                    v.setTag(1); //now grey
                }
            }
        }
    }

    //receives a graph and returns the transpose of that graph
    private DirectedWeightedGraph transpose(MyDirectedWeightedGraph g) {
        DirectedWeightedGraph gt = g.copy();
        Iterator<EdgeData> edgeIt = g.edgeIter();
        while (edgeIt.hasNext()) {
            EdgeData eIt = edgeIt.next();
            gt.connect(eIt.getDest(), eIt.getSrc(), eIt.getWeight());
        }
        return gt;
    }

    @Override
    //The function calls DFS_visit on first node in graph. The function also calls DFS_visit on the transpose of the graph starting from the same node.
    //If after both rounds of DFS_visit, every node in the graph has been touched, the function returns true.
    public boolean isConnected() {
        NodeData n = graph.getNode(0); //any node to start
        DFS_visit(graph, n); //DFS on graph
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
        DirectedWeightedGraph gt = transpose((MyDirectedWeightedGraph) graph); //tranposes the graph
        DFS_visit(gt, n); //DFS again
        Iterator<NodeData> gtIterator = gt.nodeIter(); //goes through all nodes in graph
        while (gtIterator.hasNext()) {
            NodeData gtNode = gtIterator.next();
            if (gtNode.getTag() == 0) //color of node is white, so hasn't been touched
            {
                return false; //not connected
            }
        }
        return true; //connected
    }

    @Override
    //implements Dijkstra's algorithm
    public double shortestPathDist(int src, int dest) {
        if (src == dest) {
            return 0;
        }
        PriorityQueue<NodeData> minWeight = new PriorityQueue<NodeData>(graph.nodeSize(), new NodeComparator());
        NodeData srcNode = graph.getNode(src);
        srcNode.setWeight(0); // so that this node will be the starting node
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
            if (minWeight.peek().getWeight() == Double.MAX_VALUE) { //no such path exists
                return -1;
            }
            if (minWeight.peek() == graph.getNode(dest)) { //node to be polled is at smallest weight it can be from the src node
                return minWeight.peek().getWeight();
            }
            NodeData curr = minWeight.poll();
            Iterator<EdgeData> eIterator = graph.edgeIter(curr.getKey());
            while (eIterator.hasNext()) {
                EdgeData neighborEdge = eIterator.next();
                int neighborKey = neighborEdge.getDest();
                Node neighbor = (Node) graph.getNode(neighborKey);
                if (neighbor.getWeight() > (curr.getWeight() + neighborEdge.getWeight())) {
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight()); //relaxing
                    neighbor.setPrev((Node)curr);
                    minWeight.remove(neighbor);
                    minWeight.offer(neighbor);
                }
            }
        }
        return graph.getNode(dest).getWeight(); //the destination node's weight will be the shortest path dist.
    }

    @Override
    //returns the shortest path from src to dest
    public List<NodeData> shortestPath(int src, int dest) {
        ArrayList<NodeData> path = new ArrayList<NodeData>();
        if (src == dest) { //if the values are the same, the list just holds the one value.
            path.add(graph.getNode(src));
            return path;
        }
        PriorityQueue<NodeData> minWeight = new PriorityQueue<NodeData>(graph.nodeSize(), new NodeComparator());
        NodeData srcNode = graph.getNode(src);
        srcNode.setWeight(0); // so that this node will be the starting node
        ((Node)srcNode).setPrev(null);
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
            if (minWeight.peek().getWeight() == Double.MAX_VALUE) { //no such path exists
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
                    neighbor.setWeight(curr.getWeight() + neighborEdge.getWeight()); //relaxing
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
    //returns the center node of the graph
    public NodeData center() {
        DirectedWeightedGraph copied_graph = this.copy();
        if (!isConnected()) { //if the graph is not connected, return null
            return null;
        }
        HashMap<Integer, Double> eccentricity_map = new HashMap<>(); //to hold the eccentricity values
        Iterator<NodeData> nodeIt = copied_graph.nodeIter();
        while (nodeIt.hasNext()) {
            Node n = (Node) nodeIt.next();
            int key_n = n.getKey();
            Node graph_n = (Node) this.getGraph().getNode(key_n);
            double ecc = 0; //ecc is the highest of the shortest paths
            Iterator<NodeData> nodeIt2 = copied_graph.nodeIter();
            while (nodeIt2.hasNext()) {
                Node u = (Node) nodeIt2.next();
                int key_u = u.getKey();
                Node graph_u = (Node) this.getGraph().getNode(key_u);
                double shortest_dist = shortestPathDist(graph_n.getKey(), graph_u.getKey()); //Dijkstra's algo on n, u
                if (shortest_dist > ecc) {
                    ecc = shortest_dist; //update eccentricity
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
    public List<NodeData> tsp(List<NodeData> cities) {  // finds a path that visits all of the nodes in the given list. this function is an implementation of a greedy algorithm. this implementation will be quicker than the dynamic algo but will be farther from the true answer
        double minWeight = Double.MAX_VALUE;
        ArrayList<NodeData> minPath = new ArrayList<>(), myPath, myCities;
        for (int i = 0; i < cities.size(); i++) {
            myPath = new ArrayList<>();
            myCities = new ArrayList<>(cities);
            NodeData curr = myCities.get(i);  // starts from the i'th node in the list
            do {
                myPath.add(curr);  // adds the curr to the path
                myCities.remove(curr);  // removes the path from the nodes remaining
                curr = closestNodeFinder(curr, myCities);  // finds the closest node to the curr node, and then make that the curr the result
            }
            while (myCities.size() > 1);  // until there is only one node left in myCities
            myPath.add(myCities.get(0));  // add the last city to the end
            ArrayList<NodeData> path = new ArrayList<>();
            double weight = 0;  // to count how much this path will cost in weight
            for (int j = 0; j < myPath.size() - 1; j++) {  // checks which nodes we need to pass on the way to get from any two adjacent nodes in myPath and adds them to path
                ArrayList<NodeData> addToPath = (ArrayList<NodeData>) shortestPath(myPath.get(j).getKey(), myPath.get(j + 1).getKey());
                if (j > 0) {
                    addToPath.remove(0);
                }
                path.addAll(addToPath);
                weight += shortestPathDist(myPath.get(j).getKey(), myPath.get(j + 1).getKey());
            }
            System.out.println("i: " + i + ", weight: " + weight);
            if(weight < minWeight) {  // is this path shorter than the current minimum path?
                minWeight = weight;
                minPath = new ArrayList<>(path);
            }
        }
        return minPath;
    }

    private NodeData closestNodeFinder(NodeData src, ArrayList<NodeData> cities) {
        PriorityQueue<NodeData> minWeight = new PriorityQueue<>(graph.nodeSize(), new NodeComparator());
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

    public List<NodeData> tspLong(List<NodeData> cities) {  // finds a path that visits all of the nodes in the given list. this function is an implementation of a dynamic algorithm. this implementation will be more exact to the true answer but it will take longer to compute than the previous greedy function
        HashMap<Integer, NodeData> myCities = new HashMap<>();  // this will hold all of the given nodes
        for (NodeData city : cities) {
            myCities.put(city.getKey(), city);
        }
        HashMap<String, Double> distances = new HashMap<>();  // this will hold all of the shortest distances from any two nodes
        for (int i : myCities.keySet()) {
            for (int j : myCities.keySet()) {
                if (i != j) {
                    distances.put(myCities.get(i).getKey() + "->" + myCities.get(j).getKey(), shortestPathDist(myCities.get(i).getKey(), myCities.get(j).getKey()));
                }
            }
        }
        NodeData start = cities.get(0);  // start from the first node in cities
        HashMap<Integer, NodeData> temp = new HashMap<>(myCities);
        temp.remove(start.getKey());
        ArrayList<NodeData> tempPath = new ArrayList<>();
        tspHelper(tempPath, distances, temp, start.getKey());
        tempPath.add(start);
        Collections.reverse(tempPath);
        ArrayList<NodeData> path = new ArrayList<>();
        for (int i = 0; i < tempPath.size() - 1; i++) {  // checks which nodes we need to pass on the way to get from any two adjacent nodes in myPath and adds them to path
            ArrayList<NodeData> addToPath = (ArrayList<NodeData>) shortestPath(tempPath.get(i).getKey(), tempPath.get(i + 1).getKey());
            if (i > 0) {
                addToPath.remove(0);
            }
            path.addAll(addToPath);
        }
        return path;
    }

    private double tspHelper(ArrayList<NodeData> path, HashMap<String, Double> distances, HashMap<Integer, NodeData> cities, int curr) {  // recursive function that goes through the options of the next possible node and returns the weight of the minimum option
        if (cities.size() == 0) {  // base case, all nodes have been visited
            return 0;
        }
        double minWeight = Double.MAX_VALUE, weight;
        NodeData ans = null;
        ArrayList<NodeData> minPath = new ArrayList<>();
        for (Map.Entry<Integer, NodeData> n : cities.entrySet()) {  // check all of the possibilities that can be the next node to visit
            HashMap<Integer, NodeData> temp = new HashMap<>(cities);
            temp.remove(n.getKey());
            ArrayList<NodeData> tempPath = new ArrayList<>(path);
            double dist = distances.get(curr + "->" + n.getKey());
            if (dist != -1) {  // if there's a path between curr n.getKey()
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

    @Override
    public boolean save(String file) {  // saves the graph into a json file
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(MyDirectedWeightedGraph.class, new DirectedWeightedGraphSerializer());  // uses my custom serializer
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
    public boolean load(String file) { // loads a graph from a json file
        try {
            DirectedWeightedGraph newGraph;
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

    @Override
    public String toString() {
        return "MyDirectedWeightedGraphAlgorithms{" +
                "graph=" + graph +
                '}';
    }
}
