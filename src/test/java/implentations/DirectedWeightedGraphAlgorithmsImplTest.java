package implentations;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import implentations.NodeDataImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// TODO implements more test
class DirectedWeightedGraphAlgorithmsImplTest {

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
    void init() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        assertNotEquals(dwga.getGraph(),null);
    }

    @Test
    void getGraph() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        assertNotEquals(dwga.getGraph(),null);
    }

    @Test
    void copy() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        DirectedWeightedGraph g1 = dwga.copy();
        g1.removeNode(1);
        assertNotNull(dwga.getGraph().getNode(1));
    }

    @Test
    void isConnected() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        assertTrue(dwga.isConnected());
        dwga.getGraph().addNode(new NodeDataImpl(9999,new Vector3(0,0,0)));
        assertTrue(!dwga.isConnected());
    }

    @Test
    void shortestPathDist() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        double d = dwga.shortestPathDist(14,12);
        double test = dwga.getGraph().getEdge(14,13).getWeight() + dwga.getGraph().getEdge(13,12).getWeight();
        assertEquals(d,test);
    }

    @Test
    void shortestPath() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        List<NodeData> nodes = dwga.shortestPath(14,12);
        assertEquals(nodes.get(0),dwga.getGraph().getNode(14));
        assertEquals(nodes.get(1),dwga.getGraph().getNode(13));
        assertEquals(nodes.get(2),dwga.getGraph().getNode(12));
    }

    @Test
    void center() {
        assertEquals(dwga.getGraph(),null);
        dwga.load(this.data);
        assertEquals(dwga.center().getKey(),8);
    }

    @Test
    void load() {
        assertTrue(dwga.load(this.data));
    }
}