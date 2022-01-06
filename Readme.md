# Directed Weighted Graph - And the Pokemon game
### Project Overview

- This project was built as part of the OOP Course of the Computer Science Department at Ariel University.

- in this project we nned to build upon our [second assigment](https://github.com/Daniel-Ros/OOP3) and create agents
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
![Image of graph ds methods](https://media0.giphy.com/media/MgY5wtOTRZDscUiLdJ/giphy.gif)

### Scores per level
| Level        | Grade          | Moves made    |
|   :---:      |     :---:      |      :---:    |
| 0            | 147            | 83            |
| 1            | 578            | 334           |
| 2            | 363            | 165           |
| 3            | 942            | 383           |
| 4            | 356            | 155           |
| 5            | 848            | 344           |
| 6            | 79             | 52            |
| 7            | 437            | 280           |
| 8            | 130            | 87            |
| 9            | 511            | 312           |
| 10           | 169            | 95            |
| 11           | 2483           | 558           |
| 12           | 66             | 53            |
| 13           | 465            | 360           |
| 14           | 187            | 122           |
| 15           | 310            | 237           |



### UI Classes
![Image of graph algo constructors](https://github.com/miko-t/OOEx2/blob/main/res/Part2_Graphics.png?raw=true)
### Game Classes
![Image of graph ds methods](https://github.com/miko-t/OOEx2/blob/main/res/Part2_Game.png?raw=true)
### Utilites
![Image of graph ds methods](https://github.com/miko-t/OOEx2/blob/main/res/Part2_Utilities.png?raw=true)

## Unit Tests
- This project was tested using JUnit 5 (Jupiter) unit tests.
- Inside the tests folder you can find two JUnit test classes:
    - **DWGraph_AlgoTest:** this class was used to test the DWGraph_Algo class.
    - **DWGraph_DSTest:** this class was used to test the DWGraph_DS class.
    - **JsonGraoh_Test:** this class was used to test the Json Graph loader class.
    - **invertedGraoh_Test:** this class was used to test the Inverted Graph class.
    - **testsPart2:** this class was used to test the Part2, honestly the game and the ui is a test so its useless

## Importing and Using the Project
- In order to be able to use this project, you should have JDK 11 or above (not tetsted on older versions).

- Simply clone this project to you computer and then import it to your favorite IDE (Eclipse, IntelliJ, etc..).

- to run the game you can simply Click on the Ex2.jar, or run the Ex2.java file, either way, it depends on outside Server with would probably be dead in a month.
  
