package Game;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Agent implements Runnable {
    int id;
    double value;
    int src;
    int dest;
    double speed;
    GeoLocation pos;
    Queue<NodeData> path;

    List<Pokemon> myPokemons;
    NodeData pokeDest;
    GameData gd;
    ClientData cd;
    double currentDist;

    Agent(int id, GameData gd, ClientData cd) {
        this.id = id;
        this.gd = gd;
        this.cd = cd;

        currentDist = 0;
        myPokemons = new ArrayList<>();
        pokeDest = null;
        path = new LinkedList<>();
        new Thread(this, "Agent" + id).start();
    }

    public void update(double value, int src, int dest, double speed, GeoLocation pos) {
        this.value = value;
        this.dest = dest;
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
        synchronized (this) {
            if (path.isEmpty() || path == null)
                return -1;
            if (src == path.peek().getKey()) {
                path.poll();
                if (path.isEmpty() || path == null)
                    return -1;
            }
            return path.peek().getKey();
        }
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
        synchronized (cd) {
            try {
                cd.wait();
                while (!cd.isRunning()) ;
            } catch (InterruptedException e) {


                e.printStackTrace();
            }
        }
        System.out.println("agent running");
        while (cd.isRunning()) {
            synchronized (GameData.AgentLock) {
                calculatePath();
                synchronized (GameData.AgentLock) {
                    try {
                        GameData.AgentLock.wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (path == null || path.isEmpty()) {
                    continue;
                }
            }
        }
        System.out.println("agent stopped");
    }

    private void calculatePath() {
        if (!path.isEmpty())
            return;

        synchronized (this) {
                path = new LinkedList<>();
                if (myPokemons!= null && myPokemons.isEmpty()) {
                    return;
                }

                double dist = Double.POSITIVE_INFINITY;
                List<NodeData> minPath = null;

                for (Pokemon p :
                        myPokemons) {
                    while (p.getEdge() == null)
                        p.calculateEdge(gd.getGa());
                    int psrc = p.getEdge().getSrc();
                    int pdest = p.getEdge().getDest();
                    NodeData nsrc = gd.getGa().getGraph().getNode(psrc);
                    NodeData ndest = gd.getGa().getGraph().getNode(pdest);
                    List<NodeData> tpath;

                    tpath = gd.getGa().shortestPath(src, p.getEdge().getSrc());
                    tpath.add(ndest);

                    double ret = 0;

                    for (int i = 1; i < tpath.size(); i++) {
                        EdgeData e = gd.getGa().getGraph().getEdge(tpath.get(i - 1).getKey(), tpath.get(i).getKey());
                        ret += e.getWeight();
                    }
                    if (ret < dist) {
                        dist = ret;
                        minPath = tpath;
                        currentDist = dist;
                        pokeDest = ndest;
                    }
                }
                for (NodeData t :
                        minPath) {
                    path.add(t);
                }
                path.poll();

            }
    }

    public double distToTarget(){
        if(dest == -1){
            return 0.1;
        }

        EdgeData edge = gd.getGa().getGraph().getEdge(src, dest);
        double time = edge.getWeight()/ speed;
        if(path.size() < 2 ) {
            return Math.min(Math.max(time / 10,0.1),0.15);
        }else {
            return Math.min(Math.max(time / 2,0.1),0.15);
        }
    }

    public double bid(Pokemon pokemon) {
        synchronized (gd){
            if(path.size() > 0 && pokeDest != null){
                synchronized (this) {
                    return gd.getGa().shortestPathDist(pokeDest.getKey(), pokemon.getEdge().getSrc()) + pokemon.getEdge().getWeight() + currentDist * path.size();
                }
            } else{
                return gd.getGa().shortestPathDist(src,pokemon.getEdge().getSrc());
            }
        }
    }

    public void addPokemon(Pokemon p){
        myPokemons.add(p);
    }

    public void removePokemon(Pokemon p){
        if(myPokemons != null && myPokemons.contains(p))
            myPokemons.remove(p);
    }
}



