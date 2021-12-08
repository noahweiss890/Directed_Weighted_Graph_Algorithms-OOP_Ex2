import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class Node implements NodeData {

    private GeoLocation location;
    private int key;
    private double weight;
    private String info;
    private int tag;
    private HashMap<Integer, EdgeData> inEdges, outEdges;
    private Node prev;



    public Node(String pos, int id) {
        String[] locArray = pos.split(",");
        this.location = new Point3D(Double.parseDouble(locArray[0]), Double.parseDouble(locArray[1]), Double.parseDouble(locArray[2]));
        this.key = id;
        this.weight = 0;
        this.info = "";
        this.tag = 0;
        inEdges = new HashMap<Integer, EdgeData>();
        outEdges = new HashMap<Integer, EdgeData>();
        prev = null;
    }


    public NodeData copy() {
        Node temp = new Node(this.location.x() + "," + this.location.y() + "," + this.location.z(), this.key);
        temp.inEdges = (HashMap<Integer, EdgeData>) this.inEdges.clone();
        temp.outEdges = (HashMap<Integer, EdgeData>) this.outEdges.clone();
        return temp;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public GeoLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = new Point3D(p.x(), p.y(), p.z());
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public Node getPrev(){
        return this.prev;
    }

    public void setPrev(Node n){
        this.prev = n;
    }

    public EdgeData getInEdge(int key) {
        return inEdges.get(key);
    }

    public void addInEdge(EdgeData e) {
        inEdges.put(e.getSrc(), e);
    }

    public EdgeData getOutEdge(int key) {
        return outEdges.get(key);
    }

    public void addOutEdge(EdgeData e) {
        outEdges.put(e.getDest(), e);
    }

    public int inDegree() {
        return inEdges.size();
    }

    public int outDegree() {
        return outEdges.size();
    }

    public HashMap<Integer, EdgeData> getInEdges() {
        return inEdges;
    }

    public HashMap<Integer, EdgeData> getOutEdges() {
        return outEdges;
    }

    @Override
    public String toString() {
        return "Node{" +
                "location=" + location +
                ", key=" + key +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                ", inEdges=" + inEdges +
                ", outEdges=" + outEdges +
                ", prev node=" + prev +
                '}' + "\n";
    }
}