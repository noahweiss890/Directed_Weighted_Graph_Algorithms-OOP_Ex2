import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        } catch (FileNotFoundException e) {
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
        if (!ans.load(json_file)) {
            System.out.println("the graph was not loaded successfully");
        } else {
            System.out.println("the graph was loaded successfully!");
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
        // ****** Add your code here ******

        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        Window window = new Window(alg);
        window.setSize(1000, 700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        window.setTitle("Ex2 GUI");
        window.getContentPane().setBackground(Color.white);
        window.setVisible(true);

        // ********************************
    }

    public static void main(String[] args) {
           runGUI(args[0]);
    }
}