import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;

public class Node implements NodeData {

    private GeoLocation location;
    private int key;
    private double weight;
    private String info;
    private int tag;
    private ArrayList<EdgeData> inEdges, outEdges;


    public Node(String pos, int id) {
        String[] locArray = pos.split(",");
        this.location = new Point3D(Double.parseDouble(locArray[0]), Double.parseDouble(locArray[1]), Double.parseDouble(locArray[2]));
        this.key = id;
        this.weight = 0;
        this.info = pos + ", id: " + id;
        this.tag = 0;
        inEdges = new ArrayList<EdgeData>();
        outEdges = new ArrayList<EdgeData>();
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

    public EdgeData getInEdges(int i) {
        return inEdges.get(i);
    }

    public void addInEdges(EdgeData e) {
        inEdges.add(e);
    }

    public EdgeData getOutEdges(int i) {
        return outEdges.get(i);
    }

    public void addOutEdges(EdgeData e) {
        outEdges.add(e);
    }

    public int inDegree() {
        return inEdges.size();
    }

    public int outDegree() {
        return outEdges.size();
    }
}
