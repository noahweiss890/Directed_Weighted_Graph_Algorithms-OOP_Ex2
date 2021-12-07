import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;

public class DirectedWeightedGraphSerializer implements JsonSerializer<DirectedWeightedGraph> {
    @Override
    public JsonElement serialize(DirectedWeightedGraph directedWeightedGraph, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();

        JsonArray edgeArray = new JsonArray();
        Iterator<EdgeData> eIter = directedWeightedGraph.edgeIter();
        while(eIter.hasNext()) {
            EdgeData curr = eIter.next();
            JsonObject edge = new JsonObject();
            edge.addProperty("src", curr.getSrc());
            edge.addProperty("w", curr.getWeight());
            edge.addProperty("dest", curr.getDest());
            edgeArray.add(edge);
        }
        json.add("Edges", edgeArray);

        JsonArray nodeArray = new JsonArray();
        Iterator<NodeData> nIter = directedWeightedGraph.nodeIter();
        while(nIter.hasNext()) {
            NodeData curr = nIter.next();
            JsonObject node = new JsonObject();
            node.addProperty("pos", curr.getLocation().toString());
            node.addProperty("id", curr.getKey());
            nodeArray.add(node);
        }
        json.add("Nodes", nodeArray);

        return json;
    }
}
