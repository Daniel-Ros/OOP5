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

    List<Pokemon> myPokemons;
    GameData gd;
    ClientData cd;

    Agent(int id ,GameData gd,ClientData cd){
        this.id = id;
        this.gd = gd;
        this.cd = cd;

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
        if(path.isEmpty()) return -1;
        return path.get(0).getKey();
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
        System.out.println("agent running");
        synchronized (cd){
            try {
                cd.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (cd.isRunning()){
            calculatePath();

            if(path == null || path.isEmpty())
                continue;

            for (NodeData n :
                    path) {
                System.out.print(n.getKey() + "->");
            }
            cd.sendAgent(id,path.get(0).getKey());
            cd.move();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculatePath(){
        if(dest != -1)
            return;

        List<Pokemon> freePokemons = gd.getFreePokemons();

        if(freePokemons.isEmpty())
            return;
        Pokemon p = freePokemons.get(0);
        System.out.println(p.getPos().distance(pos));
        while (!freePokemons.isEmpty() && p.getPos().distance(pos) < 0.002) {
            freePokemons.remove(p);
            if(freePokemons.isEmpty())
                return;
            p  = freePokemons.get(0);
        }

        int psrc = p.getEdge().getSrc();
        int pdest = p.getEdge().getDest();
        NodeData nsrc = gd.getGa().getGraph().getNode(psrc);
        NodeData ndest = gd.getGa().getGraph().getNode(pdest);
        path = gd.getGa().shortestPath(src,p.getEdge().getDest());
        if(!path.contains(ndest) || !path.contains(nsrc))
            path = gd.getGa().shortestPath(src,p.getEdge().getSrc());

        path.remove(0);
    }
}


