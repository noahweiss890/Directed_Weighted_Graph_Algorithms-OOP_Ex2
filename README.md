# OOP_Ex2
In this project we plan to implement the DirectedWeightedGraph and DirectedWeightedGraphAlgorithms interfaces. 
We will create a class called MyDirectedWeightedGraph that implements the DirectedWeightedGraphs interface. 
MyDirectedWeightedGraph will hold a hash map of nodes, a hash map of edges, and an int mode counter. Each node will hold a location, key, weight, info, tag, prev node, a hashmap of edges entering the node, and a hashmap of edges exiting the node. Each edge will hold its source, destination, weight, info and tag. Using a hashmap allows us to get nodes and edges, connect nodes together through edges, and connect in runtime O(1).
MyDirectedWeightedGraphAlgorithms will implement the DirectedWeightedGraphAlgorithms interface and will hold a graph. We will actualize the functions of the interface in the following ways:
isConnected: 
We will perform a DFS visit (notfull DFS) on the graph starting with a random node, then reverse all edges of the graph to get the graph Transpose, and perform a DFS visit again. If during both DFS visits, all the nodes on the graph have been touched, the function will return true.
shortestPathDist: 
We will implement Dijkstra's shortest path algorithm using a min heap priority queue. 
center:
We will calculate the eccentricity of each node and return the mim value of them all. 
tsp:
This function will be implemented using a greedy algorithm. We will calculate the cost of the path when starting from each city in the list, and reaching every other city in the list. The function will use a varient of Dijkstra to calculate the shortest path from the starting city until we will reach the first city on the list that has not yet been visited. The algorithim will do the same from that found city, until the next instance of a city from the list. This will go on until we have visited all the citites in the list. The function will check all posibiites for the staring city and return the path with the min cost. We will also provide another function that will return a more accurate answer but has a much longer runtime.
GUI:
Our GUI is called Window. Window allows us to visually illustrate the algorithms we wrote on a graph. We used a menu bar with options that all have actionlisters in order to perform different functions depending on the menu item chosen. We used paintComponent to paint to the graph, and to highlight the shortest path and tsp path when those menu items are chosen. The center node will be highlighted when the center algorithm is chosen.
After running our algorithms on graphs of sizes 1000, 10,000, 100,000 and 1,000,000, we received the following data.
1000: isConnected: 173 ms, SPD(30,900): 53 ms, SP(30,900): 46 ms, Center: 2522 ms, TSP(304,0,900,4300,230,19): 184 ms
10000: isConnected: 184 ms, SPD(30,9000): 124 ms, SP(30,9000): 90 ms, Center: 541303 ms, TSP(304,0,9000,430,23,19): 2540 ms
100000: isConnected: 19660 ms, SPD(30,9000): 5745 ms, SP(30,9000): 6052 ms, Center: TIMED OUT, TSP(304,0,9000,4300,230,19): TIMED OUT
There was not enough java heap space to create a graph of 1,000,000 nodes. 
