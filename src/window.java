import api.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class window extends JFrame implements ActionListener {

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu algoMenu;
    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem addNode;
    JMenuItem removeNode;
    JMenuItem addEdge;
    JMenuItem removeEdge;
    JMenuItem connect;
    JMenuItem init;
    JMenuItem getGraph;
    JMenuItem copy;
    JMenuItem isConnected;
    JMenuItem shortestPathDist;
    JMenuItem shortestPath;
    JMenuItem center;
    JMenuItem tsp;


    window() {
        this.setTitle("Graph Title i guess");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit Graph");
        algoMenu = new JMenu("Algorithms");

        loadItem = new JMenuItem("Load");
        saveItem = new JMenuItem("Save");

        addNode = new JMenuItem("Add Node");
        removeNode = new JMenuItem("Remove Node");
        addEdge = new JMenuItem("Add Edge");
        removeEdge = new JMenuItem("Remove Edge");
        connect = new JMenuItem("Connect");

        init = new JMenuItem("Init");
        getGraph = new JMenuItem("getGraph");
        copy = new JMenuItem("copy");
        isConnected = new JMenuItem("isConnected");
        shortestPathDist = new JMenuItem("shortestPathDist");
        shortestPath = new JMenuItem("shortestPath");
        center = new JMenuItem("center");
        tsp = new JMenuItem("tsp");


        loadItem.addActionListener(this);
        saveItem.addActionListener(this);

        addNode.addActionListener(this);
        removeNode.addActionListener(this);
        addEdge.addActionListener(this);
        removeEdge.addActionListener(this);
        connect.addActionListener(this);

        init.addActionListener(this);
        getGraph.addActionListener(this);
        copy.addActionListener(this);
        isConnected.addActionListener(this);
        shortestPathDist.addActionListener(this);
        shortestPath.addActionListener(this);
        center.addActionListener(this);
        tsp.addActionListener(this);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        editMenu.add(addNode);
        editMenu.add(removeNode);
        editMenu.add(addEdge);
        editMenu.add(removeEdge);
        editMenu.add(connect);

        algoMenu.add(init);
        algoMenu.add(getGraph);
        algoMenu.add(copy);
        algoMenu.add(isConnected);
        algoMenu.add(shortestPathDist);
        algoMenu.add(shortestPath);
        algoMenu.add(center);
        algoMenu.add(tsp);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(algoMenu);

        this.setJMenuBar(menuBar);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.BLACK);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadItem) {
            JFileChooser jFileChooser = new JFileChooser();
           int response = jFileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                String file = jFileChooser.getSelectedFile().getAbsolutePath();
                DirectedWeightedGraphAlgorithms graphAl = new MyDirectedWeightedGraphAlgorithms();
                graphAl.load(file);
                System.out.println(graphAl.getGraph());
            }
        }
        if(e.getSource() == saveItem){
            JFileChooser jFileChooser = new JFileChooser();
            int response = jFileChooser.showSaveDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());

            }
        }
        if(e.getSource() == addNode){
            System.out.println("will replace with adding node");
        }
        if(e.getSource() == removeNode){
            System.out.println("will replace with removing node");
        }
        if(e.getSource() == addEdge){
            System.out.println("will replace with adding edge");
        }
        if(e.getSource() == removeEdge){
            System.out.println("will replace with removing edge");
        }
        if(e.getSource() == connect){
            System.out.println("will replace with connect");
        }
        if(e.getSource() == init){
            System.out.println("will replace with init");
        }
        if(e.getSource() == getGraph){
            System.out.println("will replace with getgraph");
        }
        if(e.getSource() == copy){
            System.out.println("will replace with copy");
        }
        if(e.getSource() == isConnected){
            System.out.println("will replace with isConnected");
        }
        if(e.getSource() == shortestPathDist){
            System.out.println("will replace with shortestPathDist");
        }
        if(e.getSource() == shortestPath){
            System.out.println("will replace with shortestPath");
        }
        if(e.getSource() == center){
            System.out.println("will replace with center");
        }
        if(e.getSource() == tsp){
            System.out.println("will replace with tsp");
        }
    }
    public static void main(String[] args) {
        window wind = new window();
    }


}