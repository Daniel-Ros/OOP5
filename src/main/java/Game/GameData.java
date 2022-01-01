package Game;

import api.DirectedWeightedGraphAlgorithms;

import java.util.ArrayList;

public class GameData {

    public static final Object AgentLock = new Object();

    private ArrayList<Agent> agents;
    private ArrayList<Pokemon> allPokemons;
    private ArrayList<Pokemon> freePokemons;


    private DirectedWeightedGraphAlgorithms ga;

    GameData(){
        agents = new ArrayList<Agent>();
        allPokemons = new ArrayList<Pokemon>();
        freePokemons = new ArrayList<Pokemon>();
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<Pokemon> getAllPokemons() {
        return allPokemons;
    }

    public ArrayList<Pokemon> getFreePokemons() {
        return freePokemons;
    }

    public void setGa(DirectedWeightedGraphAlgorithms ga) {
        this.ga = ga;
    }

    public DirectedWeightedGraphAlgorithms getGa() {
        return ga;
    }

    public void addPokemon(Pokemon p) {
        allPokemons.add(p);
        freePokemons.add(p);
        if(ga != null)
            p.calculateEdge(ga);
    }

    public void addAgent(Agent a) {
        agents.add(a);
    }


}
