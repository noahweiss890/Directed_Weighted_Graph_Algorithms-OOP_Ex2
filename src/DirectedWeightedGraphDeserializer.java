import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class DirectedWeightedGraphDeserializer implements JsonDeserializer<DirectedWeightedGraph> {

    @Override
    public DirectedWeightedGraph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        MyDirectedWeightedGraph graph = new MyDirectedWeightedGraph();

        JsonObject nodesJsonObj = jsonObject.get("Nodes").getAsJsonObject();
        for(Map.Entry<String, JsonElement> n: nodesJsonObj.entrySet()) {
            String pos = n.getValue().getAsJsonObject().get("pos").getAsString();
            int id = n.getValue().getAsJsonObject().get("id").getAsInt();
            Node node = new Node(pos, id);
            graph.addNode(node);
        }

        JsonObject edgesJsonObj = jsonObject.get("Edges").getAsJsonObject();
        for(Map.Entry<String, JsonElement> e: edgesJsonObj.entrySet()) {
            int src = e.getValue().getAsJsonObject().get("src").getAsInt();
            double w = e.getValue().getAsJsonObject().get("w").getAsDouble();
            int dest = e.getValue().getAsJsonObject().get("dest").getAsInt();
            graph.connect(src, dest, w);
        }

        return graph;
    }
}
