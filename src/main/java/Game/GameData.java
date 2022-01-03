package Game;

import api.DirectedWeightedGraphAlgorithms;

import java.util.ArrayList;

public class GameData {

    public static final Object AgentLock = new Object();

    private ArrayList<Agent> agents;
    private ArrayList<Pokemon> allPokemons;


    private DirectedWeightedGraphAlgorithms ga;

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
            if(tmp < dist) {
                dist = tmp;
                winner = a;
            }
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
}
