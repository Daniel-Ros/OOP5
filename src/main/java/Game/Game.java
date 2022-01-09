package Game;

import GUI.Window;
import api.NodeData;

public class Game extends GameData implements Runnable{
    private ClientData cd;
    private GameData gd;


    private Window gui;
    private int moves, grade;

    public Game() {
        gd = new GameData();
        cd = new ClientData(gd);

        gd.setGa(cd.getGraph());
    }


    /**
     * build the game and init all the agents. also start all the therads
     *
     * @param isGui
     */
    public void build(boolean isGui)
    {
        for (Pokemon p :
                gd.getAllPokemons()) {
            p.calculateEdge(gd.getGa());
        }
        NodeData c = gd.getGa().center();
        gd.setCenter(c);
        System.out.println("set center");
        int poke = 0;
        for (int i = 0; i < cd.getMaxAgents(); i++) {
            System.out.println("building agent");
            Agent a = new Agent(i,gd,cd);
            if(poke < cd.getMaxPokemons()) {
                a.setSrc(gd.getAllPokemons().get(poke).getEdge().getSrc());
                for (Pokemon p :
                        gd.getAllPokemons()) {
                    while(p.getEdge().getSrc() == gd.getAllPokemons().get(poke).getEdge().getDest()){
                        poke++;
                    }
                    poke++;
                }
            }
            else
                a.setSrc(c.getKey());

            gd.addAgent(a);
            cd.registerAgent(a);
        }

        synchronized (cd) {
            cd.notifyAll();
        }
        if(isGui) {
            gui = new Window(gd, cd);
        }
        new Thread(this,"Game").start();
    }


    /**
     * just run move acording to the agents data
     */
    @Override
    public void run() {
        System.out.println("Game starting");
        while (cd.isRunning()){
            Double minWatingTime = Double.POSITIVE_INFINITY;
            for (Agent a:
                    gd.getAgents()){
                double t = a.distToTarget();
                if(t < minWatingTime)
                    minWatingTime = t;
            }
            try {

                Thread.sleep((long)(minWatingTime * 1000.0D));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cd.move();
        }

        moves = cd.getMoves();
        grade = cd.getGrade();
    }

    public ClientData getCd() {
        return cd;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
