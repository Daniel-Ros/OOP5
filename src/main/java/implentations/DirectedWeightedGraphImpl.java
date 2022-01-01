package implentations;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DirectedWeightedGraphImpl implements DirectedWeightedGraph {
    HashMap<Integer, NodeData> nodes;
    int mc;

    public DirectedWeightedGraphImpl(){
        this.nodes = new HashMap<>();
        mc = 0;
    }

    private int numOfEdges = 0;
    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public NodeData getNode(int key) {
        return nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        try {
            return ((NodeDataImpl)nodes.get(src)).getEdgeByDest(dest);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(NodeData n) {
        nodes.put(n.getKey() , n);
        mc++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (getEdge(src,dest) != null)
            return;
        if(getNode(src) == null || getNode(dest) == null)
            return;
        numOfEdges++;
        EdgeDataImpl e = new EdgeDataImpl(src,dest,w);
        ((NodeDataImpl)nodes.get(src)).addEdgeTo(e);
        ((NodeDataImpl)nodes.get(dest)).addEdgeFrom(e);
        mc++;
    }

    /**
     * This method returns an Iterator for the
     * collection representing all the nodes in the graph.
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @return Iterator<node_data>
     */
    @Override
    public Iterator<NodeData> nodeIter() {
        return new WrapperIterator<NodeData>(nodes.values().iterator(),this);
    }

    /**
     * This method returns an Iterator for all the edges in this graph.
     * Note: if any of the edges going out of this node were changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @return Iterator<EdgeData>
     */
    @Override
    public Iterator<EdgeData> edgeIter() {
        CominedIterator<EdgeData> it = new CominedIterator<>();

        for (NodeData n:nodes.values()) {
            NodeDataImpl node = (NodeDataImpl) n;
            it.addIt(node.getEdgeIter());
        }

        return new WrapperIterator<EdgeData>(it,this);
    }

    /**
     * This method returns an Iterator for edges getting out of the given node (all the edges starting (source) at the given node).
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @param node_id
     * @return Iterator<EdgeData>
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new WrapperIterator<EdgeData>(((NodeDataImpl)getNode(node_id)).getEdgeIter(),this);
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public NodeData removeNode(int key) {
        mc++;
        Iterator<EdgeData> it = ((NodeDataImpl)getNode(key)).getEdgeIter();
        ArrayList<EdgeData> deleteEdges = new ArrayList<>();
        while (it.hasNext()){
            deleteEdges.add(it.next());
        }
        it = ((NodeDataImpl)getNode(key)).getTransposedEdgeIter();
        while (it.hasNext()){
            deleteEdges.add(it.next());
        }
        for (EdgeData e: deleteEdges){
            removeEdge(e.getSrc(),e.getDest());
        }
        NodeData n = nodes.get(key);
        nodes.remove(key);

        return n;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        EdgeData e = getEdge(src, dest);
        if(e == null) {
            return null;
        }
        mc++;
        numOfEdges--;
        ((NodeDataImpl)nodes.get(src)).removeEdgeTo(e);
        ((NodeDataImpl)nodes.get(dest)).removeEdgeFrom(e);
        return e;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return numOfEdges;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }
}
