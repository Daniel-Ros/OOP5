package Game;

import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.GeoLocation;
import implentations.Vector3;

import java.util.Iterator;

public class Pokemon {
    private double val;
    private int type;
    private GeoLocation pos;
    private EdgeData edge;

    Pokemon(double val,int type,GeoLocation pos)
    {
        this.val = val;
        this.type = type;
        this.pos = pos;

        System.out.println("Pokemon " + val);
    }

    public void calculateEdge(DirectedWeightedGraphAlgorithms ga){
        Iterator<EdgeData> edges = ga.getGraph().edgeIter();

        while(edges.hasNext())
        {
            EdgeData e = edges.next();
            int t = e.getSrc() - e.getDest();
            if(t >= 0) {
                t = 1;
            }
            else{
                t = -1;
            }

            if(type !=t)
                continue;


            Vector3 p1 = (Vector3) ga.getGraph().getNode(e.getSrc()).getLocation();
            Vector3 p2 = (Vector3) ga.getGraph().getNode(e.getDest()).getLocation();

            Vector3 v1 = p1.sub(p2);
            Vector3 v2 = p1.sub(pos);

            v1.normilze();
            v2.normilze();

            if(v1.distance(v2) < 0.002){
                edge = e;
                System.out.println("is on Edge " + e.getSrc() + "->" + e.getDest());
            }
        }

    }

    public double getVal() {
        return val;
    }

    public int getType() {
        return type;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public EdgeData getEdge() {
        return edge;
    }

}
