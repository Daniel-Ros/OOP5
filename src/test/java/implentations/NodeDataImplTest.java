package implentations;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import implentations.EdgeDataImpl;
import implentations.NodeDataImpl;
import implentations.Vector3;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class NodeDataImplTest {

    @org.junit.jupiter.api.Test
    void getKey() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        NodeData n2 = new NodeDataImpl(10,new Vector3(0D,0D,0D));
        NodeData n3 = new NodeDataImpl(115,new Vector3(0D,0D,0D));

        assertEquals(n1.getKey(),1);
        assertEquals(n2.getKey(),10);
        assertEquals(n3.getKey(),115);
    }

    @org.junit.jupiter.api.Test
    void getLocation() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setLocation(new Vector3(0,3,50));

        GeoLocation g = n1.getLocation();
        assertEquals(g.x(),0);
        assertEquals(g.y(),3);
        assertEquals(g.z(),50);
    }

    @org.junit.jupiter.api.Test
    void setLocation() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setLocation(new Vector3(0,3,50));

        GeoLocation g = n1.getLocation();
        assertEquals(g.x(),0);
        assertEquals(g.y(),3);
        assertEquals(g.z(),50);
    }

    @org.junit.jupiter.api.Test
    void getWeight() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setWeight(10);
        assertEquals(n1.getWeight(),10);
    }

    @org.junit.jupiter.api.Test
    void setWeight() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setWeight(10);
        assertEquals(n1.getWeight(),10);
    }

    @org.junit.jupiter.api.Test
    void getInfo() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setInfo("test test");
        assertEquals(n1.getInfo(),"test test");
    }

    @org.junit.jupiter.api.Test
    void setInfo() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setInfo("test test");
        assertEquals(n1.getInfo(),"test test");
    }

    @org.junit.jupiter.api.Test
    void getTag() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setTag(10);
        assertEquals(n1.getTag(),10);
    }

    @org.junit.jupiter.api.Test
    void setTag() {
        NodeData n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));

        n1.setTag(10);
        assertEquals(n1.getTag(),10);
    }

    @org.junit.jupiter.api.Test
    void addEdgeTo() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        n1.addEdgeTo(new EdgeDataImpl(1,10,1));
        Iterator<EdgeData> it = n1.getEdgeIter();
        assertEquals(it.hasNext(),true);
        EdgeData e = it.next();
        assertEquals(e.getDest(),10);
        assertEquals(e.getWeight(),1);
        assertEquals(it.hasNext(),false);
    }

    @org.junit.jupiter.api.Test
    void addEdgeFrom() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        n1.addEdgeFrom(new EdgeDataImpl(1,10,1));
        Iterator<EdgeData> it = n1.getTransposedEdgeIter();
        assertEquals(it.hasNext(),true);
        EdgeData e = it.next();
        assertEquals(e.getDest(),10);
        assertEquals(e.getWeight(),1);
        assertEquals(it.hasNext(),false);
    }

    @org.junit.jupiter.api.Test
    void removeEdgeTo() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        EdgeData e = new EdgeDataImpl(1,10,1);
        n1.addEdgeTo(e);
        n1.removeEdgeTo(e);
        Iterator<EdgeData> it = n1.getEdgeIter();
        assertEquals(it.hasNext(),false);
    }

    @org.junit.jupiter.api.Test
    void removeEdgeFrom() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        EdgeData e = new EdgeDataImpl(1,10,1);
        n1.addEdgeFrom(e);
        n1.removeEdgeFrom(e);
        Iterator<EdgeData> it = n1.getTransposedEdgeIter();
        assertEquals(it.hasNext(),false);
    }


    @org.junit.jupiter.api.Test
    void getEdgeByDest() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        EdgeData e10 = new EdgeDataImpl(1,10,1);
        EdgeData e15 = new EdgeDataImpl(1,15,1);
        n1.addEdgeTo(e10);
        n1.addEdgeTo(e15);
        EdgeData e = n1.getEdgeByDest(15);
        assertEquals(e,e15);
    }

    @org.junit.jupiter.api.Test
    void getEdgeIter() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        n1.addEdgeTo(new EdgeDataImpl(1,10,1));
        Iterator<EdgeData> it = n1.getEdgeIter();
        assertEquals(it.hasNext(),true);
        EdgeData e = it.next();
        assertEquals(e.getDest(),10);
        assertEquals(e.getWeight(),1);
        assertEquals(it.hasNext(),false);
    }

    @org.junit.jupiter.api.Test
    void getTransposedEdgeIter() {
        NodeDataImpl n1 = new NodeDataImpl(1,new Vector3(0D,0D,0D));
        n1.addEdgeFrom(new EdgeDataImpl(1,10,1));
        Iterator<EdgeData> it = n1.getTransposedEdgeIter();
        assertEquals(it.hasNext(),true);
        EdgeData e = it.next();
        assertEquals(e.getDest(),10);
        assertEquals(e.getWeight(),1);
        assertEquals(it.hasNext(),false);
    }
}