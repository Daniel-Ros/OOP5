package implentations;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import implentations.NodeDataImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphImplTest {

    static String data;

    @BeforeAll
    static void initData(){
        data = "";
        File f = new File("data/G1.json");
        try {
            FileReader reader = new FileReader(f);
            int ch;
            while ((ch = reader.read()) != -1){
                data += (char)ch;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithmsImpl();

    @Test
    void getNode() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();

        assertEquals(g.getNode(1).getTag(), 0);
        assertEquals(g.getNode(-1), null);
    }

    @Test
    void getEdge() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();

        assertEquals(g.getEdge(0,1).getTag(), 0);
        assertEquals(g.getEdge(0,1).getSrc(), 0);
        assertEquals(g.getEdge(0,1).getDest(), 1);
        assertEquals(g.getEdge(0,1).getWeight(), 1.232037506070033);
        assertEquals(g.getEdge(0,999), null);

    }

    @Test
    void addNode() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        g.addNode(new NodeDataImpl(100,new Vector3(0,0,0)));
        assertEquals(mc +1 , g.getMC());
        assertEquals(g.getNode(100).getTag(), 0);
    }

    @Test
    void connect() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        g.connect(11,14,1);
        assertNotEquals(mc,g.getMC());
        assertEquals(g.getEdge(11,14).getWeight(), 1);
    }

    @Test
    void nodeIter() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int i = 1;
        Iterator<NodeData> it = g.nodeIter();
        while (it.hasNext()) {
            i++;
            it.next();
        }
        assertNotEquals(i,g.nodeSize());
    }

    @Test
    void edgeIter() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int i = 1;
        Iterator<EdgeData> it = g.edgeIter();
        while (it.hasNext()) {
            i++;
            it.next();
        }
        assertNotEquals(i,g.nodeSize());
    }

    @Test
    void removeNode() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        g.removeNode(1);
        assertNotEquals(mc,g.getMC());
        assertEquals(g.getEdge(0,1), null);
        assertEquals(g.getNode(1), null);

    }

    @Test
    void removeEdge() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        g.removeEdge(0,1);
        assertNotEquals(mc,g.getMC());
        assertEquals(g.getEdge(0,1), null);
    }

    @Test
    void nodeSize() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        assertEquals(g.nodeSize(), 17);
    }

    @Test
    void edgeSize() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        assertEquals(g.edgeSize(), 36);
    }

    @Test
    void getMC() {
        dwga.load(this.data);
        DirectedWeightedGraph g = dwga.getGraph();
        int mc = g.getMC();
        g.removeNode(1);
        assertNotEquals(mc,g.getMC());
    }
}