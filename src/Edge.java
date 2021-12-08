import api.EdgeData;
import com.google.gson.annotations.Expose;

public class Edge implements EdgeData {

    private int src;
    private double w;
    private int dest;
    private String info;
    private int tag;

    public Edge(int src, int dest, double weight){
        this.src = src;
        this.w = weight;
        this.dest = dest;
        info = "";
        tag = 0; // 0 = white for now, 1 is grey, 2 is black
    }

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return w;
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

    @Override
    public String toString() {
        return "Edge{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + w +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                '}';
    }
}
