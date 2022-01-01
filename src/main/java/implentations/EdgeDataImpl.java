package implentations;

import api.EdgeData;

import java.awt.*;

public class EdgeDataImpl implements EdgeData {
    private int src,dest,tag;
    private double weight;
    private String info;

    public EdgeDataImpl(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public EdgeDataImpl(int src, int dest, int weight, int tag, String info) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.tag = tag;
        this.info = info;
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
        return weight;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag = t;
    }

}
