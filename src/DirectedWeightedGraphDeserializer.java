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

        JsonArray nodesJsonArray = jsonObject.get("Nodes").getAsJsonArray();
        for(int i = 0; i < nodesJsonArray.size(); i++) {
            String pos = nodesJsonArray.get(i).getAsJsonObject().get("pos").getAsString();
            int id = nodesJsonArray.get(i).getAsJsonObject().get("id").getAsInt();
            graph.addNode(new Node(pos, id));
        }

        JsonArray edgesJsonArray = jsonObject.get("Edges").getAsJsonArray();
        for(int i = 0; i < edgesJsonArray.size(); i++) {
            int src = edgesJsonArray.get(i).getAsJsonObject().get("src").getAsInt();
            double w = edgesJsonArray.get(i).getAsJsonObject().get("w").getAsDouble();
            int dest = edgesJsonArray.get(i).getAsJsonObject().get("dest").getAsInt();
            graph.connect(src, dest, w);
        }

        return graph;
    }
}
