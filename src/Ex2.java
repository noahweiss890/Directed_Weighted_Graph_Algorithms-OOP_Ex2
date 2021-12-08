import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import netscape.javascript.JSObject;
import com.google.gson.*;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = null;
        // ****** Add your code here ******

        ans = new MyDirectedWeightedGraph();
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(MyDirectedWeightedGraph.class, new DirectedWeightedGraphDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(json_file);
            ans = gson.fromJson(reader, MyDirectedWeightedGraph.class);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // ********************************
        return ans;
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = null;
        // ****** Add your code here ******

        ans = new MyDirectedWeightedGraphAlgorithms();
        if(!ans.load(json_file)) {
            System.out.println("the graph was not loaded successfully");
        }

        // ********************************
        return ans;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        // ****** Add your code here ******


        // ********************************
    }

    public static void main(String[] args) {

 //       DirectedWeightedGraph graph2 = getGrapg("data/G2.json");
//        DirectedWeightedGraph graph3 = getGrapg("data/G3.json");
//        DirectedWeightedGraphAlgorithms alg1 = new MyDirectedWeightedGraphAlgorithms();
  //      DirectedWeightedGraphAlgorithms alg2 = new MyDirectedWeightedGraphAlgorithms();
  //      alg2.init(graph2);
     //   System.out.println(alg2.shortestPathDist(4, 29));
    //    System.out.println(alg2.shortestPath(4, 29));
//        DirectedWeightedGraphAlgorithms alg3 = new MyDirectedWeightedGraphAlgorithms();
//        alg1.init(graph1);
//        alg2.init(graph2);
//        alg3.init(graph3);
//        System.out.println(alg1.shortestPathDist(2,16));
//        System.out.println(alg1.shortestPath(2,16));
//        System.out.println(alg1.isConnected());
//        System.out.println(alg1.center());
//         DirectedWeightedGraph graph1 = getGrapg("data/mygraph2.json");

//       System.out.println(alg1.shortestPathDist(2,7));
//       System.out.println(alg1.shortestPath(2,7));
      //  System.out.println(alg1.shortestPath(7,2));
      //  System.out.println(alg1.isConnected());
      //  System.out.println(alg1.center());

//        DirectedWeightedGraph dwg = getGrapg("data/G1.json");
//        DirectedWeightedGraph dwg2 = getGrapg("data/G2.json");
//        DirectedWeightedGraphAlgorithms dawg2 = new MyDirectedWeightedGraphAlgorithms();
//        dawg2.init(dwg);
//        System.out.println(dawg2.isConnected());
//        DirectedWeightedGraphAlgorithms whatever = new MyDirectedWeightedGraphAlgorithms();
//        whatever.init(dwg);
        //System.out.println(whatever.isConnected());
//        System.out.println(whatever.shortestPathDist(14,0));
//        System.out.println(whatever.shortestPath(14,0));
//        System.out.println(dwg);
//        dwg.removeNode(5);
//        System.out.println(dwg);
//        System.out.println(((Node)dwg.getNode(2)).inDegree());
//        EdgeData e = dwg.removeEdge(1,2);
//        System.out.println(((Node)dwg.getNode(2)).inDegree());

        DirectedWeightedGraphAlgorithms dwga = getGrapgAlgo("data/G1.json");
//        System.out.println(dwga + "\n");
//        dwga.load("data/G2.json");
//        System.out.println(dwga + "\n");
//        dwga.save("src/testing.json");
//        dwga.load("data/G2.json");
//        dwga.save("src/testing.json");

        List<NodeData> somPath = new ArrayList<>();
        somPath.add(dwga.getGraph().getNode(0));
        somPath.add(dwga.getGraph().getNode(1));
        somPath.add(dwga.getGraph().getNode(2));
        somPath.add(dwga.getGraph().getNode(3));
//        somPath.add(dwga.getGraph().getNode(4));
//        somPath.add(dwga.getGraph().getNode(5));
//        somPath.add(dwga.getGraph().getNode(6));
//        somPath.add(dwga.getGraph().getNode(7));
//        somPath.add(dwga.getGraph().getNode(8));
//        somPath.add(dwga.getGraph().getNode(9));
//        somPath.add(dwga.getGraph().getNode(10));
//        somPath.add(dwga.getGraph().getNode(11));
//        somPath.add(dwga.getGraph().getNode(12));
//        somPath.add(dwga.getGraph().getNode(13));
//        somPath.add(dwga.getGraph().getNode(14));
//        somPath.add(dwga.getGraph().getNode(15));
//        somPath.add(dwga.getGraph().getNode(16));

        System.out.println("CENTER: " + dwga.center());

        System.out.println(dwga.tsp(somPath));

//        System.out.println(dwga.shortestPath(1,2));

        System.out.println(dwga.center());
        DirectedWeightedGraph graph2 = getGrapg("data/mygraph2.json");
        DirectedWeightedGraphAlgorithms alg2 = new MyDirectedWeightedGraphAlgorithms();
        alg2.init(graph2);
        System.out.println(alg2.shortestPathDist(2,7));
        System.out.println(alg2.shortestPath(2,7));
        System.out.println(alg2.isConnected());
        System.out.println(alg2.center());
//        PriorityQueue<Node> pq = new PriorityQueue<>(graph1.nodeSize(), new NodeComparator());
//        Iterator nodeIt = graph1.nodeIter();
//        while(nodeIt.hasNext()){
//            Node n = (Node) nodeIt.next();
//            n.setWeight(Double.MAX_VALUE);
//            pq.offer(n);
//
//        }
//         System.out.println(pq);
//         Node r = (Node) graph1.getNode(10);
//         r.setWeight(3);
//         pq.remove(r);
//         pq.offer(r);
//        System.out.println(pq);
       // System.out.println(pq);

    }
}