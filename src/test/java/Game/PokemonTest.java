package Game;

import api.DirectedWeightedGraphAlgorithms;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import implentations.Vector3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

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
    void calculateEdge() {
        DirectedWeightedGraphAlgorithms ga = new DirectedWeightedGraphAlgorithmsImpl();
        ga.load(data);
        Pokemon p = new Pokemon(0,1, Vector3.fromString("35.199963710098416,32.105723673136964,0.0"));
        p.calculateEdge(ga);
        assertEquals(p.getEdge().getSrc(),8);
        assertEquals(p.getEdge().getDest(),9);
    }
}