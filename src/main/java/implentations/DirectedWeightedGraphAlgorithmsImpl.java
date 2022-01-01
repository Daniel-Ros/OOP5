package implentations;

import api.*;

import com.google.gson.*;

import java.util.*;
import java.util.List;


public class DirectedWeightedGraphAlgorithmsImpl implements DirectedWeightedGraphAlgorithms {

    DirectedWeightedGraph graph;
    GeoLocation min,max;


    /**
     * Inits the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        graph = g;
    }

    /**
     * Returns the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    /**
     * Computes a deep copy of this weighted graph.
     * * @return  a new DirectedWeighedGtaph equals to ours.
     */
    @Override
    public DirectedWeightedGraph copy() {
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();
        Iterator<NodeData> nit = graph.nodeIter();
        while (nit.hasNext()){
            NodeData n = nit.next();
            NodeData newNode = new NodeDataImpl(n.getKey(),n.getTag(),new Vector3(
                    n.getLocation().x(),
                    n.getLocation().y(),
                    n.getLocation().z()
            ),
                    n.getInfo());
            g.addNode(newNode);
        }
        Iterator<EdgeData> eit = graph.edgeIter();
        while(eit.hasNext()){
            EdgeData e = eit.next();
            g.connect(e.getSrc(),e.getDest(),e.getWeight());
        }
        return g;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * This algorithm,uses a DFS travel twice, once on the original graph and once on the trasposed graph,if both ot them
     * returns A tree in the size of our graph,then we return true
     * @return
     */
    @Override
    public boolean isConnected() {
        NodeData firstNode = graph.nodeIter().next();
        HashSet<NodeData> visited = new HashSet<>();
        Stack<NodeData> nextNodes = new Stack<>();
        nextNodes.push(firstNode);
        while(!nextNodes.isEmpty()){
            NodeData n = nextNodes.pop();
            Iterator<EdgeData> edges = graph.edgeIter(n.getKey());
            while (edges.hasNext()){
                EdgeData edge = edges.next();
                int dest = edge.getDest();
                if(!visited.contains(graph.getNode(dest)))
                    nextNodes.push(graph.getNode(dest));
            }
            visited.add(n);
        }

        if(visited.size() != graph.nodeSize()){
            return false;
        }

        visited = new HashSet<>();
        nextNodes.push(firstNode);

        while(!nextNodes.isEmpty()){
            NodeDataImpl n = (NodeDataImpl) nextNodes.pop();
            Iterator<EdgeData> edges = n.getTransposedEdgeIter();
            while (edges.hasNext()){
                EdgeData edge = edges.next();
                int src = edge.getSrc();
                if(!visited.contains(graph.getNode(src)))
                    nextNodes.push(graph.getNode(src));
            }
            visited.add(n);
        }

        if(visited.size() != graph.nodeSize()){
            return false;
        }

        return true;
    }

    /**
     * Computes the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        List<NodeData> path = shortestPath(src,dest);

        if(path == null)
            return -1;

        double ret = 0;
        for(int i = 1; i < path.size();i++){
            EdgeData e= getGraph().getEdge(path.get(i-1).getKey(),path.get(i).getKey());
            ret += e.getWeight();
        }

        return ret;
    }

    /**
     * Computes the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Dijkstra d = new Dijkstra(src,dest,getGraph());
        d.run();
        return d.getRet();
    }

    /**
     * Finds the NodeData which minimizes the max distance to all the other nodes.
     * Assuming the graph isConnected, elese return null. See: https://en.wikipedia.org/wiki/Graph_center

     * @return the Node data to which the max shortest path to all the other nodes is minimized.
     */
    @Override
    public NodeData center() {
        if(this.isConnected() == false)
            return null;


        ArrayList<DijkstraPool> pool = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            pool.add(new DijkstraPool(graph));
        }
        Iterator<NodeData> it = graph.nodeIter();
        int i = 0;
        while (it.hasNext()){
            pool.get(i++%pool.size()).add(it.next());
        }

        for (DijkstraPool p : pool){
            p.start();
        }

        for (DijkstraPool p : pool){
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int ret = -1;
        double minmax = Double.POSITIVE_INFINITY;
        for (DijkstraPool p : pool) {
                double m = p.getMinMaxWeight();
                if(m < minmax){
                    minmax = m;
                    ret = p.getMinMaxNodeKey();
                }
        }
        return graph.getNode(ret);
    }

    /**
     * Computes a list of consecutive nodes which go over all the nodes in cities.
     * the sum of the weights of all the consecutive (pairs) of nodes (directed) is the "cost" of the solution -
     * the lower the better.
     * See: https://en.wikipedia.org/wiki/Travelling_salesman_problem
     * @param cities
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if(cities == null || cities.isEmpty()) return null;
        LinkedList<NodeData> remaining = new LinkedList<NodeData>();
        for (NodeData c :
                cities) {
            remaining.add(c);
        }

        Dijkstra dijkstra = new Dijkstra(cities.get(0).getKey(),graph);
        dijkstra.run();
        HashMap<Integer,Double> distMap = dijkstra.getDistMap();

        for (NodeData n : cities){
            if(distMap.get(n.getKey()) == Double.POSITIVE_INFINITY) return null;
        }

        LinkedList<NodeData> ret = new LinkedList<>();
        ret.add(cities.get(0));
        while (!remaining.isEmpty()){
            NodeData city = ret.peekLast();
            remaining.remove(city);
            NodeData minDirectRoad = getMinDirectRoad(city,remaining);
            if(minDirectRoad != null){
                remaining.remove(minDirectRoad);
                ret.add(minDirectRoad);
            }else{
                List<NodeData> minRoad = getMinRoad(city,remaining);
                for (NodeData n :
                        minRoad) {
                    remaining.remove(n);
                    ret.add(n);
                }
            }
        }
        return ret;
    }

    /**
     * An halper function the help us to find the minimum road from some node to one of the remianing nodes
     * @param city
     * @param remaining
     * @return
     */
    private List<NodeData> getMinRoad(NodeData city, LinkedList<NodeData> remaining) {
        Dijkstra d = new Dijkstra(city.getKey(),graph);
        d.run();
        HashMap<Integer,Double> distMap = d.getDistMap();
        HashMap<Integer,Integer> prevMap = d.getPrevMap();
        int minNode = 0;
        double minW = Double.POSITIVE_INFINITY;
        for (NodeData r:remaining) {
            if(distMap.get(r.getKey()) < minW){
                minW = distMap.get(r.getKey());
                minNode = r.getKey();
            }
        }
        List<NodeData> ret = new ArrayList<>();
        int p = prevMap.get(minNode);
        while (p != -1) {
            if (ret.size() == graph.nodeSize()) {
                ret = null;
                return ret;
            } else {
                ret.add(graph.getNode(p));
                p = prevMap.get(p);
            }
        }
        Collections.reverse(ret);
        ret.add(graph.getNode(minNode));
        ret.remove(0);
        return ret;
    }

    /**
     * private function the help to find the minimul direct rode between nodes if exists
     * @param city
     * @param remaining
     * @return
     */
    private NodeData getMinDirectRoad(NodeData city, LinkedList<NodeData> remaining) {
        NodeData min = null;
        double minW = Double.POSITIVE_INFINITY;
        for (NodeData r: remaining) {
            EdgeData e = graph.getEdge(city.getKey(),r.getKey());
            if(e != null){
                if(e.getWeight() < minW){
                    minW = e.getWeight();
                    min = r;
                }
            }
        }
        return min;
    }

    /**
     * This method loads a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String data) {
        double minx = Double.MAX_VALUE,miny = Double.MAX_VALUE,maxx= Double.MIN_VALUE,maxy = Double.MIN_VALUE;
        DirectedWeightedGraph g = new DirectedWeightedGraphImpl();
        JsonElement elements = JsonParser.parseString(data);
        JsonObject object = elements.getAsJsonObject();
        JsonArray nodes = object.get("Nodes").getAsJsonArray();
        JsonArray edges = object.get("Edges").getAsJsonArray();

        for (JsonElement n : nodes){
            String pos = n.getAsJsonObject().get("pos").getAsString();
            int id = n.getAsJsonObject().get("id").getAsInt();

            String [] positions = pos.split(",");
            double posx = Double.parseDouble(positions[0]);
            double posy = Double.parseDouble(positions[1]);
            double posz = Double.parseDouble(positions[2]);

            if(posx < minx)
                minx = posx;
            else if(posx > maxx)
                maxx  = posx;

            if(posy < miny)
                miny = posy;
            else if(posy > maxy)
                maxy  = posy;

            NodeData node = new NodeDataImpl(id,0,new Vector3(posx,posy,posz),"");
            g.addNode(node);
        }

        for (JsonElement e : edges){
            int src = e.getAsJsonObject().get("src").getAsInt();
            int dest = e.getAsJsonObject().get("dest").getAsInt();
            double w = e.getAsJsonObject().get("w").getAsDouble();

            g.connect(src,dest,w);
        }
        min = new Vector3(minx,miny,0);
        max = new Vector3(maxx,maxy,0);
        init(g);
        return true;
    }

    public GeoLocation getMin() {
        return min;
    }

    public GeoLocation getMax() {
        return max;
    }

}
