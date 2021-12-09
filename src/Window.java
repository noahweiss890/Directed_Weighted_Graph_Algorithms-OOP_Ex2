import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;


public class Window extends JFrame implements ActionListener {
    private DirectedWeightedGraphAlgorithms dwga;
    private Image mBuffer_image;
    private Graphics mBuffer_graphics;
    private int height = 700;
    private int width = 1000;
    private int kRADIUS = 15;

    JPanel panel;
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


    public Window(DirectedWeightedGraphAlgorithms dwga) {
        this.dwga = dwga;
        initGUI();
    }

    private void initGUI() {
        this.getContentPane().setBackground(Color.white);
        this.setTitle("Ex2 GUI");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(width, height);
        this.setLayout(null);

        panel = new JPanel();
        panel.setBackground(Color.RED);
        panel.setBounds(0, 0, 50, 1000);
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

        panel.add(menuBar);
        this.setJMenuBar(menuBar);
        this.add(panel);
    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        // Create a new "canvas"
        mBuffer_image = createImage(width, height);
        mBuffer_graphics = mBuffer_image.getGraphics();

        // Draw on the new "canvas"
        paintComponents(mBuffer_graphics);

        // "Switch" the old "canvas" for the new one
        g.drawImage(mBuffer_image, 0, 0, this);

    }

    public void paintComponents(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Iterator<NodeData> nIt = dwga.getGraph().nodeIter();
        double minX = Double.MAX_VALUE, maxX = 0, minY = Double.MAX_VALUE, maxY = 0;
        while(nIt.hasNext()) {
            NodeData n = nIt.next();
            if(n.getLocation().x() < minX) {
                minX = n.getLocation().x();
            }
            if(n.getLocation().x() > maxX) {
                maxX = n.getLocation().x();
            }
            if(n.getLocation().y() < minY) {
                minY = n.getLocation().y();
            }
            if(n.getLocation().y() > maxY) {
                maxY = n.getLocation().y();
            }
        }
//        minX += kRADIUS/width;
//        maxX -= kRADIUS/width;
//        minY += kRADIUS/height;
//        maxY -= kRADIUS/height;
        Iterator<EdgeData> eIt = dwga.getGraph().edgeIter();
        g2d.setColor(Color.BLUE);
        while (eIt.hasNext()) {
            EdgeData e = eIt.next();
            NodeData nSrc = dwga.getGraph().getNode(e.getSrc());
            NodeData nDest = dwga.getGraph().getNode(e.getDest());
            g2d.drawLine((int)(((nSrc.getLocation().x() - minX) / (maxX - minX)) * width), (int)(((nSrc.getLocation().y() - minY) / (maxY - minY)) * height), (int)(((nDest.getLocation().x() - minX) / (maxX - minX)) * width), (int)(((nDest.getLocation().y() - minY) / (maxY - minY)) * height));
        }
        nIt = dwga.getGraph().nodeIter();
        while (nIt.hasNext()) {
            NodeData n = nIt.next();
            g2d.setColor(Color.RED);
            g2d.fillOval((int)(((n.getLocation().x() - minX) / (maxX - minX)) * width - kRADIUS), (int)(((n.getLocation().y() - minY) / (maxY - minY)) * height - kRADIUS), 2 * kRADIUS, 2 * kRADIUS);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString(n.getKey() + "", (int)(((n.getLocation().x() - minX) / (maxX - minX)) * width - kRADIUS), (int)(((n.getLocation().y() - minY) / (maxY - minY)) * height - kRADIUS));
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadItem) {
            JFileChooser jFileChooser = new JFileChooser();
            int response = jFileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                String file = jFileChooser.getSelectedFile().getAbsolutePath();
                dwga.load(file);
            }
        }
        if (e.getSource() == saveItem) {
            JFileChooser jFileChooser = new JFileChooser();
            int response = jFileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());

            }
        }
        if (e.getSource() == addNode) {
            System.out.println("will replace with adding node");
        }
        if (e.getSource() == removeNode) {
            System.out.println("will replace with removing node");
        }
        if (e.getSource() == addEdge) {
            System.out.println("will replace with adding edge");
        }
        if (e.getSource() == removeEdge) {
            System.out.println("will replace with removing edge");
        }
        if (e.getSource() == connect) {
            System.out.println("will replace with connect");
        }
        if (e.getSource() == init) {
            System.out.println("will replace with init");
        }
        if (e.getSource() == getGraph) {
            System.out.println("will replace with getgraph");
        }
        if (e.getSource() == copy) {
            System.out.println("will replace with copy");
        }
        if (e.getSource() == isConnected) {
            System.out.println("will replace with isConnected");
        }
        if (e.getSource() == shortestPathDist) {
            System.out.println("will replace with shortestPathDist");
        }
        if (e.getSource() == shortestPath) {
            System.out.println("will replace with shortestPath");
        }
        if (e.getSource() == center) {
            System.out.println("will replace with center");
        }
        if (e.getSource() == tsp) {
            System.out.println("will replace with tsp");
        }
    }


}