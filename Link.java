/*
Gordon Adam
1107425
ANC4 Assessed Exercise
*/

// Class to represent a link between two nodes
public class Link {

	private int cost;
	private int destination;

	public Link(int d, int c) {
		destination = d;
		cost = c;
	}

	public void setCost(int c) {
		cost = c;
	}

	public int getCost() {
		return cost;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int d) {
		destination = d;
	}

	public boolean equals(Link l) {
		if(l.cost == cost) {
			if(l.destination == destination) {
				return true;
			}
		}
		return false;
	}
}