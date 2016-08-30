/*
Gordon Adam
1107425
ANC4 Assessed Exercise
*/

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;

public class Network {

	private ArrayList<Node> nodes;
	private boolean splithorizon;
	
	// Constructor for a network
	public Network(BufferedReader reader) {

		nodes = new ArrayList<Node>();
		splithorizon = false;

		try {
			String text = null;

    		while ((text = reader.readLine()) != null) {
        		nodes.add(new Node(text));
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
		} 
	}

	// Performs an exchange of routing tables between the nodes across the whole network
	public Network exchange() {
		for (Node nd : nodes) {
			ArrayList<Integer> connections = nd.getConnections();
			ArrayList<Node> connectedNodes = getNodes(connections);
			System.out.println(nd.getID() + ", " + connectedNodes.size());
			for (Node cnd : connectedNodes) {
				if(!cnd.updateRoutingTable(nd.getID(), nd.getRoutingTable())) {
					System.out.println("Error!");
					System.exit(0);
				}
			}
		}
		return this;
	}

	// Removes the links connecting nodes in both directions
	public void removeLink(Node a, Node b) {
		a.removeLink(b.getID());
		b.removeLink(a.getID());
	}

	// Converts an arraylist of node id's to an arraylist of the respective nodes
	public ArrayList<Node> getNodes(ArrayList<Integer> connections) {
		ArrayList<Node> connectedNodes = new ArrayList<Node>();
		for (int i : connections) {
			for (Node nd : nodes) {
				if (nd.getID() == i) {
					connectedNodes.add(nd);
				}
			}
		}	
		return connectedNodes;	
	}

	// Returns the lowest cost between two node id's
	public int getCost(int a, int b) {
		Node nd = getNodeByID(a);
		if (nd != null) {
			return nd.getCost(b);
		} else {
			return 0;
		}
	}

	// getter for the split horizon variable
	public boolean getSplitHorizon() {
		return splithorizon;
	}

	// setter for split horizon variable
	public void setSplitHorizon(boolean sh) {
		splithorizon = sh;
		for (Node nd : nodes) {
			nd.setSplitHorizon(sh);
		}
	}

	// returns an arraylist for the path between two nodes
	public ArrayList<Integer> traceRoute(Node a, Node b, int iter) {
		ArrayList<Integer> route = new ArrayList<Integer>();
		route.add(a.getID());
		Node nd = a;
		for(int i = 0; i < iter; i++) {
			int ol = nd.getOutgoingLink(b.getID());
			if((ol == b.getID()) || (ol < 0)) {
				route.add(ol);
				return route;
			}
			nd = getNodeByID(ol);
			route.add(ol);
		}
		return route;
	}

	// edits the cost of a link between two nodes
	public void editLink(Node a, Node b, int cost) {
		a.editLink(b.getID(), cost);
		b.editLink(a.getID(), cost);
	}

	//gets a node by it's id
	public Node getNodeByID(int a) {
		for (Node nd : nodes) {
			if (nd.getID() == a) {
				return nd;
			}
		}
		return null;
	}

	// getter for the list of nodes 
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	//returns as a string the routing table of the network
	public String toString() {
		String st = "Routing Table (Destination, Distance, Outgoing Link)\n==========\n";
		for (Node nd : nodes) {
			st += nd;
		}
		return st;
	}
}