import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        dwga.save("src/testing.json");

        dwga.load("data/G2.json");

        dwga.save("src/testing.json");


    }
}