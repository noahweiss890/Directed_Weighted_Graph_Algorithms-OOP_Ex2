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

    JPanel menuPanel;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu algoMenu;
    JMenu tspMenu;
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
    JMenuItem tspGreedy;
    JMenuItem tspLong;


    public Window(DirectedWeightedGraphAlgorithms dwga) {
        this.dwga = dwga;
        MenuBar();
    }

    private void MenuBar() {
        menuPanel = new JPanel();
        menuPanel.setBounds(0,0, 1000, 50);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit Graph");
        algoMenu = new JMenu("Algorithms");
        tspMenu = new JMenu("TSP");

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
        tspGreedy = new JMenuItem("TSP Greedy");
        tspLong = new JMenuItem("TSP Long");

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
        tspGreedy.addActionListener(this);
        tspLong.addActionListener(this);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        editMenu.add(addNode);
        editMenu.add(removeNode);
        editMenu.add(addEdge);
        editMenu.add(removeEdge);
        editMenu.add(connect);

        tspMenu.add(tspGreedy);
        tspMenu.add(tspLong);

        algoMenu.add(init);
        algoMenu.add(getGraph);
        algoMenu.add(copy);
        algoMenu.add(isConnected);
        algoMenu.add(shortestPathDist);
        algoMenu.add(shortestPath);
        algoMenu.add(center);
        algoMenu.add(tspMenu);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(algoMenu);

        this.setJMenuBar(menuBar);
//        menuPanel.add(menuBar);
//        this.add(menuPanel);
//        this.pack();
//        return menuBar;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        mBuffer_image = createImage(width, height);
        mBuffer_graphics = mBuffer_image.getGraphics();
        paintComponents(mBuffer_graphics);
        g2d.drawImage(mBuffer_image, 0, 0, this);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2D = (Graphics2D) g;
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
        Iterator<EdgeData> eIt = dwga.getGraph().edgeIter();
        g2D.setColor(Color.BLUE);
        while (eIt.hasNext()) {
            EdgeData e = eIt.next();
            NodeData nSrc = dwga.getGraph().getNode(e.getSrc());
            NodeData nDest = dwga.getGraph().getNode(e.getDest());
            g2D.drawLine((int)(((nSrc.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25), (int)(((nSrc.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100), (int)(((nDest.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25), (int)(((nDest.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100));
        }
        nIt = dwga.getGraph().nodeIter();
        while (nIt.hasNext()) {
            NodeData n = nIt.next();
            g2D.setColor(Color.RED);
            g2D.fillOval((int)(((n.getLocation().x() - minX) / (maxX - minX)) * (width-50) - kRADIUS + 25), (int)(((n.getLocation().y() - minY) / (maxY - minY)) * (height-150) - kRADIUS + 100), 2 * kRADIUS, 2 * kRADIUS);
            g2D.setColor(Color.DARK_GRAY);
            g2D.drawString(n.getKey() + "", (int)(((n.getLocation().x() - minX) / (maxX - minX)) * (width-50) - kRADIUS + 25), (int)(((n.getLocation().y() - minY) / (maxY - minY)) * (height-150) - kRADIUS + 100));
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
            this.repaint();
        }
        if (e.getSource() == saveItem) {
            JFileChooser jFileChooser = new JFileChooser();
            int response = jFileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());
                dwga.save(file.toString());
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
        if (e.getSource() == tspGreedy) {
            System.out.println("will replace with tsp greedy");
        }
        if (e.getSource() == tspLong) {
            System.out.println("will replace with tsp long");
        }
    }


}