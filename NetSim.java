/*
Gordon Adam
1107425
ANC4 Assessed Exercise
*/

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import javax.swing.*;

public class NetSim {
	public static void main(String[] args) {

		// Checks if network description file has been provided
		if (args.length == 0) {
			System.out.println("Error: Need to provide network description file");
			System.exit(0);
		}
		File file = new File(args[0]);
		BufferedReader reader = null;
		Network network = null;
    	
		// Tries to open and read in the file
		try {
    		reader = new BufferedReader(new FileReader(file));
    		network = new Network(reader);
		} catch (FileNotFoundException e) {
    		System.out.println("Error: File does not exist");
    		System.exit(0);
		} 

		// Initialises the GUI
		GUI gui = new GUI(network);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1100, 600);
        gui.setVisible(true);
	} 
}