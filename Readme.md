# Directed Weighted Graph - And the Pokemon game
### Project Overview

- This project was built as part of the OOP Course of the Computer Science Department at Ariel University.

- in this project we needeg to build upon our [second assigment](https://github.com/Daniel-Ros/OOP3) and create agents
that will grab "pokemons"

- **Project contributors**: Daniel Rosenberg

### Interface Summary

| Interface                       | Description                                                        |
|---------------------------------|--------------------------------------------------------------------|
| EdgeData                        | This interface represents a grpah edge                             |
| NodeData                        | This interface represents a grpah node                             |
| GeoLocation                     | This interface represents a point in 3D space                      |
| DirectedWeightedGraphAlgorithms | This interface allows us to run some basic algorithms on our graph |
| DirectedWeightedGraph           | This interface represents a directed weighted grpah                |

This interfaces were written in previous assignment, and were modified to be better suited to our needs.

### Overview
- We implemented a Wrapped class for the given client, to suit the workflow of the project
- We modeled the project around the MVC project type:
  - A working GUI that syncs with the client
  - every single segment run on its own thread thus making it possible to run GUI and intensive CPU algorithms simultaneity 
- We implemented a working algorithms for our agents and improved it until we got good results


###  Graph Algorithms
- The methods: `isConnected`, `shortestPath` , `shortestPathDist` and `center` are based on two well known algorithms:

    - **BFS - Breadth-first search:** This algorithm is used by `isConnected` method. we use it twice, once for the original graph
  second ttime we use it ont the transversed graph, this will allow us to verify the connectivity of the graph
    - **Dijkstra:** This algorithm is used by `shortestPath` , `shortestPathDist` methods to find the shortest path between two vertices and for `center` to see all pair shortest path .
    
> for more info you can see: [Ex3](https://github.com/Daniel-Ros/OOP3)

### Agent algorithm
- Each Agent run on his own thread
- When an Agent is free he will search for the best free pokemon, and will move in its direction


### Ingame footage
![](https://github.com/Daniel-Ros/OOP5/blob/master/img.png)
### Scores per level
| Level        | Grade | Moves made |
|   :---:      |:-----:|:----------:|
| 0            |  125  |    161     |
| 1            |  521  |    330     |
| 2            |  315  |    219     |
| 3            |  828  |    554     |
| 4            |  481  |    275     |
| 5            | 1008  |    555     |
| 6            |  147  |    272     |
| 7            |  521  |    550     |
| 8            |  98   |    548     |
| 9            |  469  |    548     |
| 10           |  168  |    274     |
| 11           | 1068  |    515     |
| 12           |  40   |    150     |
| 13           |  273  |    597     |
| 14           |  135  |    298     |
| 15           |  239  |    404     |


## Unit Tests
this project was test using the JUINT5 library. the tests can be run by an IDE of choice.

## Importing and Using the Project
- In order to be able to use this project, you should have JDK 11 or above (not tetsted on older versions).

- Simply clone this project to you computer and then import it to your favorite IDE (Eclipse, IntelliJ, etc..).
- run the server and then run the program.

you can also run it like so:

    git clone https://github.com/Daniel-Ros/OOP5.git
    java -jar Ex4_Server_v0.0.jar {TEST CASE}
    java -jar Ex4
