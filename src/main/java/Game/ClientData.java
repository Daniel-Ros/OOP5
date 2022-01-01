package Game;

import api.DirectedWeightedGraphAlgorithms;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import implentations.Vector3;

import java.io.IOException;
import java.util.HashSet;

public class ClientData implements Runnable {
    private int maxPokemons;
    private int moves;
    private int maxAgents;

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

        initClientData();
        checkForNewPokemons();
        new Thread(this).start();
    }


    private void initClientData() {
        JsonElement elements = JsonParser.parseString(client.getInfo()).getAsJsonObject().get("GameServer");
        JsonObject object = elements.getAsJsonObject();

        maxPokemons = object.get("pokemons").getAsInt();
        moves = object.get("moves").getAsInt();
        maxAgents = object.get("agents").getAsInt();
    }

    public void run()
    {
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("client start");
        client.start();
        while (isRunning()) {;
            synchronized (client){
                checkForNewPokemons();
                updateAgents();
                move();
            }
            try {
                Thread.sleep(1);
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

    public void registerAgent(Agent a){
        client.addAgent("{\"id\":"+a.getSrc()+"}");
    }

    private void updateAgents(){
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

    private void checkForNewPokemons(){
        synchronized (gd){
            synchronized (client) {
                HashSet<Pokemon> currentPokemons = new HashSet<Pokemon>();
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
                        System.out.println("need to add pokemon");
                        Pokemon p = new Pokemon(value, type, Vector3.fromString(pos));
                        gd.addPokemon(p);
                        gd.notifyAll();
                        currentPokemons.add(p);
                    }
                }

                for (int i = 0; i < gd.getFreePokemons().size();i++) {
                    if (!currentPokemons.contains(gd.getFreePokemons().get(i))){
                        System.out.println("removing pokemon");
                        gd.getFreePokemons().remove(gd.getFreePokemons().get(i));
                    }
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

    public Client getClient() {
        return client;
    }

    public void sendAgent(int id, int key) {
        synchronized (client) {
            System.out.println("agent " + id + " to " + key);
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
}
