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
