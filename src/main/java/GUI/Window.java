package GUI;

import Game.Agent;
import Game.Pokemon;
import api.DirectedWeightedGraphAlgorithms;
import implentations.DirectedWeightedGraphAlgorithmsImpl;

import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;

/**
 * This is the main window the combines all the panels together
 */
public class Window {

    DirectedWeightedGraphAlgorithms ga;
    JFrame window;
    GrapPanel graphPanel;
    public Window(DirectedWeightedGraphAlgorithms graphAlgorithms, ArrayList<Agent> agents, ArrayList<Pokemon> pokemons){
        ga = graphAlgorithms;


        window = new JFrame();
        graphPanel = new GrapPanel(ga,((DirectedWeightedGraphAlgorithmsImpl)ga).getMin(),((DirectedWeightedGraphAlgorithmsImpl)ga).getMax(),agents,pokemons);

        window.setSize(800,800);
        window.setTitle("Daniel Roseberg and Daniel Zinn");
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(graphPanel,BorderLayout.CENTER);
    }

    public void repaint()
    {
        graphPanel.getTopLevelAncestor().repaint();
    }

}
