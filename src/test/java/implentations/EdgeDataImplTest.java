package implentations;

import api.EdgeData;
import implentations.EdgeDataImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeDataImplTest {
    EdgeData e = new EdgeDataImpl(0,5,0.1D);
    @Test
    void getSrc() {
        assertEquals(e.getSrc(),0);
    }

    @Test
    void getDest() {
        assertEquals(e.getDest(),5);
    }

    @Test
    void getWeight() {
        assertEquals(e.getWeight(),0.1);
    }

    @Test
    void getInfo() {
        e.setInfo("loooool");
        assertEquals(e.getInfo(),"loooool");
    }

    @Test
    void setInfo() {
        e.setInfo("123");
        assertEquals(e.getInfo(),"123");
    }

    @Test
    void getTag() {
        e.setTag(123);
        assertEquals(e.getTag(),123);
    }

    @Test
    void setTag() {
        e.setTag(123);
        assertEquals(e.getTag(),123);
    }
}