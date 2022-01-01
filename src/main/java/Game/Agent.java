package Game;

import api.DirectedWeightedGraphAlgorithms;
import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Agent implements Runnable{
    int id;
    double value;
    int src;
    int dest;
    double speed;
    GeoLocation pos;
    List<NodeData> path;

    int next;

    List<Pokemon> myPokemons;
    GameData gd;
    ClientData cd;

    Agent(int id ,GameData gd,ClientData cd){
        this.id = id;
        this.gd = gd;
        this.cd = cd;

        next = 10;

        new Thread(this).start();
    }

    public void update(double value,int src,int dest,double speed, GeoLocation pos)
    {
        this.value = value;
        this.dest =dest;
        this.src = src;
        this.speed = speed;
        this.pos = pos;
    }


    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        next = src;
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    public int getNextStaion() {
         return next;
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
        synchronized (cd){
            try {
                cd.wait();
                while (!cd.isRunning());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("agent running");
        while (cd.isRunning()){
            synchronized (GameData.AgentLock) {
                try {
                    GameData.AgentLock.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                calculatePath();
                if (path == null || path.isEmpty()) {
                    System.out.println("no path");
                    continue;
                }
                List<Pokemon> freePokemons = gd.getFreePokemons();
                if (freePokemons.isEmpty())
                    return;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("agent stopped");
    }

    private void calculatePath(){
        if(dest != -1) {
            return;
        }


        List<Pokemon> freePokemons = gd.getFreePokemons();
        if(freePokemons.isEmpty()) {
            System.out.println("no pokemons");
            return;
        }


        Pokemon p = freePokemons.get(0);
        while (p.getEdge() == null)
            p.calculateEdge(gd.getGa());
        int psrc = p.getEdge().getSrc();
        int pdest = p.getEdge().getDest();
        NodeData nsrc = gd.getGa().getGraph().getNode(psrc);
        NodeData ndest = gd.getGa().getGraph().getNode(pdest);
        System.out.println("from" + src + " to" + pdest);

        path = gd.getGa().shortestPath(src,p.getEdge().getSrc());
        if(!path.contains(ndest) || !path.contains(nsrc)) {
            System.out.println("from" + src + " to" + pdest);
            path = gd.getGa().shortestPath(src, p.getEdge().getDest());
        }

        path.remove(0);
        next = path.get(0).getKey();
    }
}


