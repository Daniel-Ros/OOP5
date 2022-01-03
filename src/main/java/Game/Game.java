package Game;

import GUI.Window;
import api.NodeData;

public class Game implements Runnable{
    ClientData cd;
    GameData gd;

    Window gui;

    public Game() {
        gd = new GameData();
        cd = new ClientData(gd);

        gd.setGa(cd.getGraph());
    }

    public void build()
    {
        for (Pokemon p :
                gd.getAllPokemons()) {
            p.calculateEdge(gd.getGa());
        }
        NodeData c = gd.getGa().center();
        int poke = 0;
        for (int i = 0; i < cd.getMaxAgents(); i++) {
            System.out.println("building agent");
            Agent a = new Agent(i,gd,cd);
            if(poke < cd.getMaxPokemons())
                a.setSrc(gd.getAllPokemons().get(poke++).getEdge().getSrc());
            else
                a.setSrc(c.getKey());
            gd.addAgent(a);
            cd.registerAgent(a);
        }

        synchronized (cd) {
            cd.notifyAll();
        }

        gui = new Window(gd,cd);

        new Thread(this,"Game").start();
    }

    @Override
    public void run() {
        System.out.println("Game starting");
        int time = cd.timeToEnd();
        while (cd.isRunning()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int timeLeft = cd.timeToEnd();
            if(time - timeLeft >= 100) {
                time = timeLeft;
                cd.move();
            }
        }
    }
}
