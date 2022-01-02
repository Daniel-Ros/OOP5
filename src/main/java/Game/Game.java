package Game;

import GUI.Window;
import api.DirectedWeightedGraphAlgorithms;
import implentations.DirectedWeightedGraphAlgorithmsImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

        System.out.println(cd.getMaxAgents());
        for (int i = 0; i < cd.getMaxAgents(); i++) {
            System.out.println("building agent");
            Agent a = new Agent(i,gd,cd);
            // TODO: Better starting position
            a.setSrc(gd.getAllPokemons().get(0).getEdge().getDest());
            gd.addAgent(a);
            cd.registerAgent(a);
        }

        synchronized (cd) {
            cd.notifyAll();
        }

        gui = new Window(gd,cd);

        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Game starting");
        while (cd.isRunning()){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cd.move();
        }
    }
}
