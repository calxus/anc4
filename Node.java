/*
Gordon Adam
1107425
ANC4 Assessed Exercise
*/

import java.util.ArrayList;

public class Node {

	private int id;
	private ArrayList<Link> links;
	private ArrayList<ArrayList<Integer>> routingTable;
	private boolean splithorizon;

	// Clank constructor for a node
	public Node() {
		splithorizon = false;
		routingTable = new ArrayList<ArrayList<Integer>>();
		routingTable.add(new ArrayList<Integer>());
		routingTable.add(new ArrayList<Integer>());
		routingTable.add(new ArrayList<Integer>());
	}

	// Constructor for a node
	public Node(String st) {

		st = st.replaceAll("\\s","");
		String[] row = st.split(",");
		splithorizon = false;
		links = new ArrayList<Link>();
		routingTable = new ArrayList<ArrayList<Integer>>();
		routingTable.add(new ArrayList<Integer>());
		routingTable.add(new ArrayList<Integer>());
		routingTable.add(new ArrayList<Integer>());
		
		id = Integer.parseInt(row[0]);

		for (int i = 1; i < row.length; i++) {
			String[] cost_dest = row[i].split(":");
			int destination = Integer.parseInt(cost_dest[0]);
			int cost = Integer.parseInt(cost_dest[1]);
			if (destination == id) {
				continue;
			}
			routingTable.get(0).add(destination);
			routingTable.get(1).add(cost);
			if (cost < 0) {
				routingTable.get(2).add(-1);
			} else {
				Link link = new Link(destination, cost);
				links.add(link);
				routingTable.get(2).add(destination); 
			}
		}
	}

	// takes a routing table from another node and updates this nodes routing table from it
	public boolean updateRoutingTable(int foreign_id, ArrayList<ArrayList<Integer>> foreign_rt) {
		int foreign_id_idx = routingTable.get(0).indexOf(foreign_id); // gets the index of the foreign id in this routing table
		int foreign_link = -1;

		// gets the cost of link to the node passed into the function
		for(Link l : links) {
			if(l.getDestination() == foreign_id) {
				foreign_link = l.getCost();
			}
		}

		// if the link is dead return
		if (foreign_link == -1) {
			return false;
		}

		// checks to see if the cost of the link is less than the cost currently in the routing table
		// or if the node passed into the function in the routing table is currently marked as dead
		if((foreign_link < routingTable.get(1).get(foreign_id_idx)) 
		|| (routingTable.get(2).get(foreign_id_idx) == foreign_id) 
		|| (routingTable.get(1).get(foreign_id_idx) == -1)) {

			routingTable.get(1).set(foreign_id_idx, foreign_link);
			routingTable.get(2).set(foreign_id_idx, foreign_id);
		}

		// Iterates through each item in the routing table that has just been received
		for(int i = 0; i < foreign_rt.get(0).size(); i++) {

			// finds the corresponding entry in the local routing table
			int foreign_row_idx = routingTable.get(0).indexOf(foreign_rt.get(0).get(i));
			if(foreign_row_idx < 0) {
				continue;
			}

			// if the distance for the entry in the foreign routing table is equal to 0 and split horizon capability is on
			if (foreign_rt.get(1).get(i) <= 0) {
				if(splithorizon) {

					// if the foreign routing table shows that a link has gone dead to some other node then set the local routing to show that
					if((routingTable.get(2).get(foreign_row_idx) == foreign_id) && (foreign_rt.get(1).get(i) == -1)) {
						routingTable.get(1).set(foreign_row_idx, -1);
						routingTable.get(2).set(foreign_row_idx, -1);
					}
				}
				continue;
			}

			// if split horizon is off
			// if the local routing table does not have a route to the node that the foreign node is advertising
			// or the one being advertised is better
			// or if the node is the next hop in the local routing table
			// then update the local routing table
			if ( ( (routingTable.get(1).get(foreign_row_idx) < 0) 
			|| ( (routingTable.get(1).get(foreign_row_idx) ) > (foreign_rt.get(1).get(i) + routingTable.get(1).get(foreign_id_idx) ) ) 
			|| (routingTable.get(2).get(foreign_row_idx) == foreign_id ) ) 
			&&  (!splithorizon) ) {
				routingTable.get(1).set(foreign_row_idx, foreign_rt.get(1).get(i) + routingTable.get(1).get(foreign_id_idx));
				routingTable.get(2).set(foreign_row_idx, routingTable.get(2).get(foreign_id_idx));
			}

			// if split horizon is on
			// same as before except if this node is the next hop on the routing table being advertised don't accept
			if ( ( (routingTable.get(1).get(foreign_row_idx) < 0) 
			|| ( (routingTable.get(1).get(foreign_row_idx) ) > (foreign_rt.get(1).get(i) + routingTable.get(1).get(foreign_id_idx) ) ) 
			|| (routingTable.get(2).get(foreign_row_idx) == foreign_id ) )	
			&& (splithorizon) ) {
				if(foreign_rt.get(2).get(i) != id) {
					routingTable.get(1).set(foreign_row_idx, foreign_rt.get(1).get(i) + routingTable.get(1).get(foreign_id_idx));
					routingTable.get(2).set(foreign_row_idx, foreign_id);
				}
			}

		}
		return true;
	}

	// Returns all the node id's connected to this node
	public ArrayList<Integer> getConnections() {
		ArrayList<Integer> connections = new ArrayList<Integer>();
		for (Link l : links) {
			connections.add(l.getDestination());
		}
		return connections;

	}

	// getter for routing table
	public ArrayList<ArrayList<Integer>> getRoutingTable() {
		return routingTable;
	}

	// getter for id
	public int getID() {
		return id;
	}

	// setter for split horizon
	public void setSplitHorizon(boolean sh) {
		splithorizon = sh;
	}

	// gets lowest cost between this and another node
	public int getCost(int newID) {
		int idx = routingTable.get(0).indexOf(newID);
		if (idx > -1) {
			return routingTable.get(1).get(idx);
		} else {
			System.out.println("Error");
			return 0;
		}
	}

	// gets the outgoing link towards a particular destination
	public int getOutgoingLink(int newID) {
		int idx = routingTable.get(0).indexOf(newID);
		return routingTable.get(2).get(idx);
	}

	// removes a particular link from the node
	public void removeLink(int newID) {
		for (int i = 0; i < routingTable.get(0).size(); i++) {
			if ((routingTable.get(0).get(i) == newID) || (routingTable.get(2).get(i) == newID)) {
				routingTable.get(1).set(i, -1);
				routingTable.get(2).set(i, -1);
			}
		}
		for (Link l : links) {
			if (l.getDestination() == newID) {
				int idx = links.indexOf(l);
				System.out.println(links.get(idx).getDestination());
				links.remove(idx);
				return;
			}
		}
	}

	// edits the cost of a link to a particular node
	public void editLink(int id, int cost) {
		for (Link l : links) {
			if(l.getDestination() == id) {
				l.setCost(cost);
			}
		}
	}

	public boolean equals(Node nd) {
		if (id == nd.getID()) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		String st = id + ": [";
		for (int i = 0; i < routingTable.get(0).size(); i++) {
			st += " (" + routingTable.get(0).get(i) + ", " + routingTable.get(1).get(i) + ", " + routingTable.get(2).get(i) + ")";
			if (i < (routingTable.get(0).size()-1)) {
				st += ",";
			}
		}
		st += " ]\n";
		return st;
	}
}