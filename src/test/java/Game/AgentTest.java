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

class AgentTest {

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
    void distToTarget() {
        GameData gd = new GameData();
        DirectedWeightedGraphAlgorithms ga = new DirectedWeightedGraphAlgorithmsImpl();
        ga.load(data);
        gd.setGa(ga);
        Agent a = new Agent();
        a.setGd(gd);
        Pokemon p = new Pokemon(0,1, Vector3.fromString("35.199963710098416,32.105723673136964,0.0"));
        p.calculateEdge(ga);

        a.update(0,1,-1,1,Vector3.fromString("0,0,0"));
        assertEquals(a.distToTarget(),0.1);
        a.update(0,1,2,1,Vector3.fromString("0,0,0"));
        assertEquals(a.distToTarget(),0.1);
    }

    @Test
    void bid() {
        GameData gd = new GameData();
        DirectedWeightedGraphAlgorithms ga = new DirectedWeightedGraphAlgorithmsImpl();
        ga.load(data);
        gd.setGa(ga);
        Agent a = new Agent();
        a.setGd(gd);
        Pokemon p = new Pokemon(0,1, Vector3.fromString("35.199963710098416,32.105723673136964,0.0"));
        p.calculateEdge(ga);

        a.update(0,1,-1,1,Vector3.fromString("0,0,0"));
        assertEquals(a.bid(p),6.204771159825874);
        a.update(0,1,2,1,Vector3.fromString("0,0,0"));
        assertEquals(a.bid(p),6.204771159825874);
        a.update(0,8,2,1,Vector3.fromString("0,0,0"));
        assertEquals(a.bid(p),0.0);
    }
}