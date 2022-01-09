package Game;

import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;

import java.util.ArrayList;

public class GameData {

    public static final Object AgentLock = new Object();

    private ArrayList<Agent> agents;
    private ArrayList<Pokemon> allPokemons;


    private DirectedWeightedGraphAlgorithms ga;
    private NodeData center;

    GameData(){
        agents = new ArrayList<Agent>();
        allPokemons = new ArrayList<Pokemon>();
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<Pokemon> getAllPokemons() {
        return allPokemons;
    }

    public ArrayList<Pokemon> getFreePokemons() {
        return allPokemons;
    }

    public void setGa(DirectedWeightedGraphAlgorithms ga) {
        this.ga = ga;
    }

    public DirectedWeightedGraphAlgorithms getGa() {
        return ga;
    }

    /**
     * add pokemon by looping throught all agents and asking which one is closer to the pokemon
     *
     * @param p
     * @param time
     */
    public void addPokemon(Pokemon p,int time) {
        allPokemons.add(p);
        if(ga != null)
            p.calculateEdge(ga);
        if(agents == null || agents.isEmpty())
            return;
        double dist = Double.POSITIVE_INFINITY;
        Agent winner = null;
        for (Agent a :
                agents) {
            double tmp = a.bid(p);
            if(tmp == -1)
                continue;
            if(tmp < dist) {
                dist = tmp;
                winner = a;
            }
        }
        if(winner == null){
            return;
        }
        p.setTaken(time);
        winner.addPokemon(p);
    }

    public void addAgent(Agent a) {
        agents.add(a);
    }


    public void removePokemon(Pokemon pokemon) {
        allPokemons.remove(pokemon);
        for (Agent a :
                agents) {
            a.removePokemon(pokemon);
        }
    }

    public void setCenter(NodeData center) {
        this.center = center;
    }

    public int getCenter() {
        if(center == null) return 0;
        return center.getKey();
    }
}
