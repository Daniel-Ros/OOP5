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
    private boolean taken;

    private int takenAt;

    Pokemon(double val,int type,GeoLocation pos)
    {
        this.val = val;
        this.type = type;
        this.pos = pos;
        this.taken = false;
        this.takenAt = -1;
    }

    public int whenTaken(){
        return takenAt;
    }

    public boolean isTaken(){
        return taken;
    }

    public void setTaken(int time){
        takenAt = time;
        taken = true;
    }

    /**
     * calculate the edge that the pokemon is on using linear algebera ans some vector math
     *
     * @param ga
     */
    public void calculateEdge(DirectedWeightedGraphAlgorithms ga){
        Iterator<EdgeData> edges = ga.getGraph().edgeIter();
        EdgeData minEdge = null;
        double min =  Float.POSITIVE_INFINITY;
        while(edges.hasNext())
        {
            EdgeData e = edges.next();
            int t = e.getSrc() - e.getDest();
            if(t >= 0) {
                t = -1;
            }
            else{
                t = 1;
            }

            if(type !=t)
                continue;


            Vector3 p1 = (Vector3) ga.getGraph().getNode(e.getSrc()).getLocation();
            Vector3 p2 = (Vector3) ga.getGraph().getNode(e.getDest()).getLocation();

            if(!pos.inRect(p1,p2))
                continue;

            Vector3 v1 = p1.sub(p2);
            Vector3 v2 = p1.sub(pos);

            v1.normilze();
            v2.normilze();
            if(v1.distance(v2) < min){
                minEdge = e;
                min = v1.distance(v2);
            }
        }
        edge = minEdge;

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
