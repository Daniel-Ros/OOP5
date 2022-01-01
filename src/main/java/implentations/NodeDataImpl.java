package implentations;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

public class NodeDataImpl implements NodeData {
    private int id,tag;
    private double weight;
    private GeoLocation geoLocation;
    private String info;

    private HashMap<Integer, EdgeData> toEdges;
    private HashMap<Integer, EdgeData> fromEdges;

    public NodeDataImpl(int id, GeoLocation geoLocation) {
        this.id = id;
        this.tag = 0;
        this.geoLocation = geoLocation;
        this.info = "";
        this.toEdges = new HashMap<>();
        this.fromEdges = new HashMap<>();
        this.weight = 0;
    }

    public NodeDataImpl(int id, int tag, GeoLocation geoLocation, String info) {
        this.id = id;
        this.tag = tag;
        this.geoLocation = geoLocation;
        this.info = info;
        this.toEdges = new HashMap<>();
        this.fromEdges = new HashMap<>();
        this.weight = 0;
    }

    @Override
    public int getKey() {
        return id;
    }

    @Override
    public GeoLocation getLocation() {
        return geoLocation;
    }

    @Override
    public void setLocation(GeoLocation p) {
        geoLocation = p;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight =w;
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

    public void addEdgeTo(EdgeData e){
        toEdges.put(e.getDest(),e);
    }

    public void addEdgeFrom(EdgeData e){
        fromEdges.put(e.getSrc(),e);
    }

    public void removeEdgeTo(EdgeData e){
        toEdges.remove(e.getDest());
    }

    public void removeEdgeFrom(EdgeData e){
        fromEdges.remove(e.getSrc());
    }

    public EdgeData getEdgeByDest(int dest){
        return toEdges.get(dest);
    }

    public Iterator<EdgeData> getEdgeIter(){
        return toEdges.values().iterator();
    }

    public Iterator<EdgeData> getTransposedEdgeIter(){
        return fromEdges.values().iterator();
    }

}
