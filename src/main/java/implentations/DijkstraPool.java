package implentations;

import api.DirectedWeightedGraph;
import api.NodeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Threadablle object that can be used to combine n Dijksta objects and run them one by one
 */
public class DijkstraPool extends Thread{

    List<NodeData> nodes;
    DirectedWeightedGraph graph;
    int ret;
    Double max;
    DijkstraPool( DirectedWeightedGraph g ){
        nodes = new ArrayList<>();
        graph = g;
    }

    public void add(NodeData n){
        nodes.add(n);
    }
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        ret = 0;
        max = Double.POSITIVE_INFINITY;

        for(NodeData n: nodes){
            Dijkstra d = new Dijkstra(n.getKey(),graph);
            d.run();
            double tmp = d.getMaximumDist();
            if(tmp < max){
                ret = d.getSrc();
                max = tmp;
            }
        }
    }

    public double getMinMaxWeight(){
        return max;
    }

    public int getMinMaxNodeKey(){
        return ret;
    }
}
