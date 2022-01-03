package Game;

import api.DirectedWeightedGraphAlgorithms;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import implentations.Vector3;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ClientData implements Runnable {
    private int maxPokemons;
    private int moves;
    private int maxAgents;



    private int grade;

     private Client client;

    private GameData gd;


    ClientData(GameData gd) {
        this.gd = gd;
        client= new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateClientData();
        checkForNewPokemons();
        new Thread(this,"Client").start();
    }


    void updateClientData() {
        synchronized (client) {
             try {
                 JsonElement elements = JsonParser.parseString(client.getInfo()).getAsJsonObject().get("GameServer");
                 JsonObject object = elements.getAsJsonObject();
                 maxPokemons = object.get("pokemons").getAsInt();
                 moves = object.get("moves").getAsInt();
                 maxAgents = object.get("agents").getAsInt();
                 grade = object.get("grade").getAsInt();
             }catch (Exception e){
                 return;
             }
        }
    }

    public void run() {
        HashMap<Agent, Integer> agentLastPos = new HashMap<Agent, Integer>();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("client start");
        client.start();
        int time = timeToEnd();
        while (isRunning()) {
            ;
            synchronized (client) {
                updateClientData();
                checkForNewPokemons();
                updateAgents();
                boolean moved = false;
                synchronized (gd.AgentLock) {
                    for (Agent a :
                            gd.getAgents()) {
                        int node = a.getNextStaion();
                        if (!agentLastPos.containsKey(a)) {
                            moved = true;
                            agentLastPos.put(a, node);
                        } else if (agentLastPos.get(a) != node) {
                            moved = true;
                            agentLastPos.replace(a, node);
                        }
                    }
                    if (moved) {
                        for (Agent a :
                                gd.getAgents()) {
                            sendAgent(a.id, agentLastPos.get(a));
                        }
                    }
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void move(){
        synchronized (client){
            client.move();
        }
    }

    public boolean isRunning(){
        synchronized (client) {
            return client.isRunning().equals("true");
        }
    }

    public int timeToEnd(){
        synchronized (client) {
            return Integer.parseInt(client.timeToEnd());
        }
    }

    public void registerAgent(Agent a){
        client.addAgent("{\"id\":"+a.getSrc()+"}");
    }

    void updateAgents(){
            synchronized (client) {
            String json = client.getAgents();
            //System.out.println(json);
            JsonElement elements = JsonParser.parseString(json);
            JsonObject object = elements.getAsJsonObject();
            JsonArray agentsArray = object.get("Agents").getAsJsonArray();

            for (JsonElement n : agentsArray) {
                JsonObject Agent = n.getAsJsonObject().get("Agent").getAsJsonObject();
                int id = Agent.get("id").getAsInt();
                double value = Agent.get("value").getAsDouble();
                int src = Agent.get("src").getAsInt();
                int dest = Agent.get("dest").getAsInt();
                int speed = Agent.get("speed").getAsInt();
                String pos = Agent.get("pos").getAsString();

                synchronized (gd) {
                    gd.getAgents().get(id).update(value, src, dest, speed, Vector3.fromString(pos));
                }
            }
        }
    }

    void checkForNewPokemons(){
        synchronized (gd){
            synchronized (client) {
                HashSet<Pokemon> currentPokemons = new HashSet<Pokemon>();
                boolean changed = false;
                String json = client.getPokemons();
                //System.out.println(json);
                JsonElement elements = JsonParser.parseString(json);
                JsonObject object = elements.getAsJsonObject();
                JsonArray agentsArray = object.get("Pokemons").getAsJsonArray();

                for (JsonElement n : agentsArray) {
                    JsonObject Pokemon = n.getAsJsonObject().get("Pokemon").getAsJsonObject();
                    double value = Pokemon.get("value").getAsDouble();
                    int type = Pokemon.get("type").getAsInt();
                    String pos = Pokemon.get("pos").getAsString();

                    Boolean ex = false;

                    for (Pokemon p : gd.getAllPokemons()) {
                        if (p.getType() == type && p.getVal() == value && p.getPos().distance(Vector3.fromString(pos)) < 0.00001 * 0.00001) {
                            //System.out.println("is eq to pokemon");
                            currentPokemons.add(p);
                            ex = true;
                        }
                    }
                    if (!ex) {
                        Pokemon p = new Pokemon(value, type, Vector3.fromString(pos));
                        gd.addPokemon(p,timeToEnd());
                        gd.notifyAll();
                        currentPokemons.add(p);
                        changed = true;
                    }
                }

                for (int i = 0; i < gd.getFreePokemons().size();i++) {
                    if(!gd.getFreePokemons().get(i).isTaken()){
                        Pokemon p =gd.getFreePokemons().get(i);
                        gd.removePokemon(p);
                        gd.addPokemon(p,timeToEnd());
                        changed = true;
                    }

                    if (!currentPokemons.contains(gd.getFreePokemons().get(i))){
                        gd.removePokemon(gd.getFreePokemons().get(i));
                        changed = true;
                        gd.notifyAll();
                    }
                }

                if (changed)
                    synchronized (GameData.AgentLock){
                        GameData.AgentLock.notifyAll();
                    }

            }
        }
    }


    public int getMaxPokemons() {
        return maxPokemons;
    }

    public int getMoves() {
        return moves;
    }

    public int getMaxAgents() {
        return maxAgents;
    }

    public int getGrade() {
        return grade;
    }

    public Client getClient() {
        return client;
    }

    public void sendAgent(int id, int key) {
        synchronized (client) {
            client.chooseNextEdge("{\"agent_id\":" + id + ", \"next_node_id\": " + key + "}");
        }
    }

    public DirectedWeightedGraphAlgorithms getGraph() {
        synchronized (client) {
            DirectedWeightedGraphAlgorithms ga = new DirectedWeightedGraphAlgorithmsImpl();
            ga.load(client.getGraph());
            return ga;
        }
    }


    public void stop() {
        synchronized (client){
            client.stop();
        }
    }
}
