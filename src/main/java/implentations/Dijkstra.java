package implentations;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.lang.instrument.Instrumentation;
import java.util.*;

/**
 * The dijkstra algorithm basec on priority queue
 */
public class Dijkstra{
    private int src,dest;
    private DirectedWeightedGraph graph;
    private List<NodeData> ret;


    HashMap<Integer,Integer> p;
    private HashMap<Integer,Double> d;

    /**
     * Dijkstra algorirhm to use to find a distance betwwen 2 nodes
     * @param src
     * @param dest
     * @param g the graph
     */
    Dijkstra(int src, int dest, DirectedWeightedGraph g){
        this.src = src;
        this.dest =dest;
        this.graph = g;
    }

    /**
     * Dijkstra algorithm to make a tree with all the distances from src
     * @param src
     * @param g the graph
     */
    Dijkstra(int src, DirectedWeightedGraph g){
        this.src = src;
        this.dest =Integer.MAX_VALUE;
        this.graph = g;
    }

    /**
     * run the algorithm
     */
    public void run() {
        HashMap<Integer,Double> dist = new HashMap<>();
        HashMap<Integer,Integer> prev = new HashMap<>();
        HashSet<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> Q = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int)(dist.get(o1) - dist.get(o2));
            }
        });

        Iterator<NodeData> it = graph.nodeIter();
        while(it.hasNext()){
            NodeData n = it.next();
            if(n.getKey() == src){
                dist.put(n.getKey(),0D);
                prev.put(n.getKey(),-1);
                continue;
            }
            dist.put(n.getKey(),Double.POSITIVE_INFINITY);
            prev.put(n.getKey(),-1);
        }

        Q.add(src);
        while (!Q.isEmpty()){
            int n = Q.poll();
            visited.add(n);
            Iterator<EdgeData> edges = graph.edgeIter(n);
            while (edges.hasNext()){
                EdgeData e = edges.next();
                int neighbor = e.getDest();
                if(visited.contains(e)) continue;
                double alt = dist.get(n) + e.getWeight();
                double d = dist.get(neighbor);
                if (alt < d){
                    dist.replace(neighbor,alt);
                    prev.replace(neighbor,n);
                    Q.add(neighbor);
                }
            }
        }

        if(dest != Integer.MAX_VALUE) {
            ret = new ArrayList<>();
            int p = prev.get(dest);
            while (p != -1) {
                if (ret.size() == graph.nodeSize()) {
                    ret = null;
                    return;
                } else {
                    ret.add(graph.getNode(p));
                    p = prev.get(p);
                }
            }
            Collections.reverse(ret);
            ret.add(graph.getNode(dest));
        }


        d = dist;
        p = prev;

    }

    public List<NodeData> getRet() {
        return ret;
    }

    public int getSrc() {
        return src;
    }

    public HashMap<Integer, Double> getDistMap() {
        return d;
    }

    public HashMap<Integer, Integer> getPrevMap() {
        return p;
    }

    public double getMaximumDist(){
        double ret = Double.NEGATIVE_INFINITY;
        for (Double d :
                this.d.values()) {
            if (d > ret) {
                ret  = d;
            }
        }
        return ret;
    }

}
