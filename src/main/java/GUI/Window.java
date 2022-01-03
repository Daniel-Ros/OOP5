package GUI;

import Game.Agent;
import Game.ClientData;
import Game.GameData;
import Game.Pokemon;
import api.DirectedWeightedGraphAlgorithms;
import implentations.DirectedWeightedGraphAlgorithmsImpl;

import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;

/**
 * This is the main window the combines all the panels together
 */
public class Window implements Runnable{

    GameData gd;
    ClientData cd;

    DirectedWeightedGraphAlgorithms ga;
    JFrame window;
    GrapPanel graphPanel;

    public Window(GameData gd, ClientData cd) {
        this.gd = gd;
        this.cd = cd;

        ga = gd.getGa();

        window = new JFrame();
        graphPanel = new GrapPanel(ga,((DirectedWeightedGraphAlgorithmsImpl)ga).getMin(),((DirectedWeightedGraphAlgorithmsImpl)ga).getMax(),gd,cd);

        window.setSize(800,800);
        window.setTitle("Daniel Roseberg and Daniel Zinn");
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(graphPanel,BorderLayout.CENTER);

        new Thread(this,"GUI").start();
    }

    public void repaint()
    {
        graphPanel.getTopLevelAncestor().repaint();
    }

    @Override
    public void run() {
        while (cd.isRunning()) {
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
