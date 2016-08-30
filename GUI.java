/*
Gordon Adam
1107425
ANC4 Assessed Exercise
*/

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.*;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GUI extends JFrame {

    private static final long serialVersionUID = 8083868183987456695L;
    private HashMap<Integer, mxICell> cells;
    private final mxGraph graph;
    private Object parent;
    private ArrayList<Node> nodes;
    private Network network;

    mxICell a,b,c,d,e,f,g,h;

    // Constructor for GUI
    public GUI(Network net) {

        // Sets the title of the window
        super("Network Simulation");

        network = net;

        graph = new mxGraph();
        parent = graph.getDefaultParent();
        cells = new HashMap<Integer, mxICell>();

        nodes = net.getNodes();

        // updates the model with the nodes and links
        graph.getModel().beginUpdate();
        try {

            for(int i = 0; i < nodes.size(); i++) {
                mxICell o = (mxICell)graph.insertVertex(parent, null, nodes.get(i).getID() + "", 0, 0, 80, 30, "fontColor=#FF0000;");
                cells.put(nodes.get(i).getID(), o);
            }

            for(int i = 0; i < nodes.size(); i++) {
                ArrayList<Integer> connections = nodes.get(i).getConnections();
                for(int j = 0; j < connections.size(); j++) {
                    int a = nodes.get(i).getID();
                    int b = connections.get(j);
                    if (graph.getEdgesBetween(cells.get(a), cells.get(b)).length < 1) { 
                        graph.insertEdge(parent, null, net.getCost(a, b) + "", cells.get(a), cells.get(b), "fontColor=#FF0000;");
                    }
                }
            }

        } finally {
            graph.getModel().endUpdate();
        }

        // define layout
        mxIGraphLayout layout = new mxCircleLayout(graph);
        layout.execute(graph.getDefaultParent());

        JTextArea ta = new JTextArea(8,0); // creates the output window
        JScrollPane ta_sp = new JScrollPane(ta); // creates the scroll pane for the output window
        JTextField tf = new JTextField(); // creates the input text field for commands

        ta.setEditable(false);
        ta.setFont(new Font(Font.SANS_SERIF, 9, 15));

        ta.append(net.toString());

        ta_sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ta_sp.setPreferredSize(new Dimension(600, 250));

        // Creates the listener for enter
        tf.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e){
                ta.append("> " + tf.getText() + "\n");
                ta.append(processCommand(tf.getText()) + "\n");
                if(tf.getText().equals("clear")) {
                    ta.setText("");
                }
                tf.setText("");
            }
        });

        // sets the layout of the window
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tf, BorderLayout.SOUTH);
        getContentPane().add(ta_sp, BorderLayout.EAST);
        getContentPane().add(graphComponent, BorderLayout.WEST);
        setResizable(false);

    }

    // parses the command and chooses the correct function to call
    public String processCommand(String cmd) {
        String[] cmds = cmd.split(" ");

        if(cmds[0].equals("table")) {
            if(cmds.length == 1) {
                return network.toString();
            } else {
                return "Invalid arguments for \"table\" command";
            }
        }

        if(cmds[0].equals("exchange")) {
            if (cmds.length > 1) {
                return exchangeCommand(Arrays.copyOfRange(cmds, 1, cmds.length));
            } else {
                String[] e_array = new String[1];
                e_array[0] = "1";
                return exchangeCommand(e_array);
            }
        }

        if(cmds[0].equals("remove")) {
            if (cmds.length == 3) {
                return removeCommand(Arrays.copyOfRange(cmds, 1, cmds.length));
            } else {
                return "Wrong number of arguments provided for \"remove\" command";
            }
        }

        if(cmds[0].equals("edit")) {
            if (cmds.length == 4) {
                return editCommand(Arrays.copyOfRange(cmds, 1, cmds.length));
            } else {
                return "Wrong number of arguments provided for \"edit\" command";
            }
        }

        if(cmds[0].equals("trace")) {
            if (cmds.length == 4) {
                return traceCommand(Arrays.copyOfRange(cmds, 1, cmds.length));
            } else if (cmds.length == 3) {
                cmds[0] = cmds[1];
                cmds[1] = cmds[2];
                cmds[2] = "16";
                return traceCommand(cmds);
            } else {
                return "Wrong number of arguments provided for \"trace\" command";
            }
        }
        
        if(cmds[0].equals("splithorizon")) {
            if (cmds.length == 2) {
                return splitHorizonCommand(Arrays.copyOfRange(cmds, 1, cmds.length));
            } else {
                return "Wrong number of arguments provided for \"splithorizon\" command";
            }
        }
        if(cmds[0].equals("clear")) {
            return "clear";
        }
        return "Command not recognised";
    }

    // Performs the splithorizon command
    public String splitHorizonCommand(String[] cmds) {
        if(cmds[0].equals("on")) {
            if(network.getSplitHorizon()) {
                return "Split Horizon capability is already on";
            } else {
                network.setSplitHorizon(true);
                return "Split Horizon capability has been turned on";
            }
        }
        if(cmds[0].equals("off")) {
            if(network.getSplitHorizon()) {
                network.setSplitHorizon(false);
                return "Split Horizon capability has been turned off";
            } else {
                return "Split Horizon capability is already off";
            }
        } else {
            return "Argument for \"splithorizon\" is not recognised";
        }
    }

    // Performs the trace command
    public String traceCommand(String[] cmds) {
        int id_a = -1;
        int id_b = -1;
        int iter = 0;
        try {
            id_a = Integer.parseInt(cmds[0]);
            id_b = Integer.parseInt(cmds[1]);
            iter = Integer.parseInt(cmds[2]);
            if (id_a == id_b) {
                return "Cannot trace route to self";
            }
        } catch (Exception e) {
            return "Node ID's and number of iterations must be integers";
        }
        Node nd_a = network.getNodeByID(id_a);
        Node nd_b = network.getNodeByID(id_b);

        ArrayList<Integer> route = network.traceRoute(nd_a, nd_b, iter);
        String st = "";
        for(int i : route) {
            if(i == -1) {
                return st += "?\nDestination could not be reached.";
            }
            if(id_b == i) {
                return st += (i + "\n" + "Distance: " + network.getCost(id_a, id_b));
            }
            st += (i + " -> ");
        }
        return st;
    }

    // Performs the remove command
    public String removeCommand(String[] cmds) {
        int id_a = -1;
        int id_b = -1;
        try {
            id_a = Integer.parseInt(cmds[0]);
            id_b = Integer.parseInt(cmds[1]);
        } catch (Exception e) {
            return "Node ID's must be an integers";
        }
        Node nd_a = network.getNodeByID(id_a);
        Node nd_b = network.getNodeByID(id_b);

        if((nd_a != null) && (nd_b != null)) {
            graph.getModel().beginUpdate();
            try {
                network.removeLink(nd_a, nd_b);

                Object[] edges = graph.getEdgesBetween(cells.get(nd_a.getID()), cells.get(nd_b.getID()));

                for (Object e : edges) {
                    graph.getModel().remove(e);
                }
            } finally {
                graph.getModel().endUpdate();
                return "Edge " + nd_a.getID() + " -> " + nd_b.getID() + " has been removed.";
            }
        }
        return "Incorrect Node ID";
    }

    // Performs the edit command
    public String editCommand(String[] cmds) {
        int id_a = -1;
        int id_b = -1;
        int cost;
        try {
            id_a = Integer.parseInt(cmds[0]);
            id_b = Integer.parseInt(cmds[1]);
            cost = Integer.parseInt(cmds[2]);
        } catch (Exception e) {
            return "Node ID's and cost must be an integer";
        }
        Node nd_a = network.getNodeByID(id_a);
        Node nd_b = network.getNodeByID(id_b);

        if((nd_a != null) && (nd_b != null)) {
            graph.getModel().beginUpdate();
            try {
                network.editLink(nd_a, nd_b, cost);

                Object[] edges = graph.getEdgesBetween(cells.get(nd_a.getID()), cells.get(nd_b.getID()));

                for (Object e : edges) {
                    graph.getModel().remove(e);
                }

                graph.insertEdge(parent, null, cost + "", cells.get(id_a), cells.get(id_b), "fontColor=#FF0000;");
            } finally {
                graph.getModel().endUpdate();
                return "Edge " + nd_a.getID() + " -> " + nd_b.getID() + " has been updated to cost " + cost + ".";
            }
        }
        return "Incorrect Node ID";
    }

    // Performs the exchange command
    public String exchangeCommand(String[] cmds) {
        try {
            for (int i = 0; i < Integer.parseInt(cmds[0]); i++) {
                network.exchange();
            }
            return network.toString();
        } catch(Exception e) {
            if (cmds[0].equals("stable")) {
                boolean stable = false;
                int count = 0;
                while(!stable) {
                    if (network.toString().equals(network.exchange().toString())) {
                        return "Network stable after " + count + " iterations";
                    }
                    count++;
                }
            }
            return "Argument for \"exchange\" command not recognised";
        }
    }
}







