package implentations;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import implentations.Dijkstra;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    static String data;

    @BeforeAll
    static void init(){
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

    @Test
    void run() {
        DirectedWeightedGraphAlgorithms ga = new DirectedWeightedGraphAlgorithmsImpl();
        ga.load(this.data);

        Dijkstra d = new Dijkstra(0, ga.getGraph());
        d.run();

        HashMap<Integer,Double> distMap = d.getDistMap();

        for (Double dist: distMap.values()){
            assertNotEquals(dist,Double.POSITIVE_INFINITY);
        }

        assertTrue(distMap.get(2) < distMap.get(3));
        assertEquals(d.getPrevMap().get(3),2);
        assertEquals(d.getMaximumDist(),distMap.get(10));
    }
}