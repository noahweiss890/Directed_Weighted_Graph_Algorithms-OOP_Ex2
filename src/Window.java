import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class Window extends JFrame implements ActionListener {
    private DirectedWeightedGraphAlgorithms dwga;
    private Image mBuffer_image;
    private Graphics mBuffer_graphics;
    private int height = 700;
    private int width = 1000;
    private int RADIUS = 15;
    ArrayList<EdgeData> toHighlight = new ArrayList<>();
    int center_node = -1;

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
        menuPanel.setBounds(0, 0, 1000, 50);

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

        tspMenu.add(tspGreedy);
        tspMenu.add(tspLong);

        algoMenu.add(isConnected);
        algoMenu.add(shortestPathDist);
        algoMenu.add(shortestPath);
        algoMenu.add(center);
        algoMenu.add(tspMenu);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(algoMenu);

        this.setJMenuBar(menuBar);
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
        while (nIt.hasNext()) {
            NodeData n = nIt.next();
            if (n.getLocation().x() < minX) {
                minX = n.getLocation().x();
            }
            if (n.getLocation().x() > maxX) {
                maxX = n.getLocation().x();
            }
            if (n.getLocation().y() < minY) {
                minY = n.getLocation().y();
            }
            if (n.getLocation().y() > maxY) {
                maxY = n.getLocation().y();
            }
        }
        Iterator<EdgeData> eIt = dwga.getGraph().edgeIter();
        g2D.setStroke(new BasicStroke(3));
        while (eIt.hasNext()) {
            EdgeData e = eIt.next();
            g2D.setColor(Color.BLUE);
            NodeData nSrc = dwga.getGraph().getNode(e.getSrc());
            NodeData nDest = dwga.getGraph().getNode(e.getDest());
            g2D.drawLine((int)(((nSrc.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25), (int)(((nSrc.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100), (int)(((nDest.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25), (int)(((nDest.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100));
            g2D.setColor(Color.MAGENTA);
            double newX = nDest.getLocation().x() - ((nDest.getLocation().x() - nSrc.getLocation().x()) / 4);
            double newY = nDest.getLocation().y() - ((nDest.getLocation().y() - nSrc.getLocation().y()) / 4);
            g2D.drawString(String.format("%.3f", e.getWeight()), (int)(((newX - minX) / (maxX - minX)) * (width-50) + 25), (int)(((newY - minY) / (maxY - minY)) * (height-150) + 100));
        }
        if(!toHighlight.isEmpty()) {
            g2D.setStroke(new BasicStroke(5));
            g2D.setColor(Color.ORANGE);
            for(EdgeData e: toHighlight) {
                NodeData nSrc = dwga.getGraph().getNode(e.getSrc());
                NodeData nDest = dwga.getGraph().getNode(e.getDest());
                g2D.drawLine((int)(((nSrc.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25), (int)(((nSrc.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100), (int)(((nDest.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25), (int)(((nDest.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100));
            }
        }
        g2D.setStroke(new BasicStroke(3));
        toHighlight.clear();
        nIt = dwga.getGraph().nodeIter();
        if(dwga.getGraph().nodeSize() == 1) {
            NodeData n = nIt.next();
            g2D.setColor(Color.RED);
            g2D.fillOval(width/2, height/2, 2 * RADIUS, 2 * RADIUS);
            g2D.setColor(Color.green);
            g2D.drawString(n.getKey() + "", width/2 + RADIUS - 5, height/2 + RADIUS + 5);
        }
        else {
            while (nIt.hasNext()) {
                NodeData n = nIt.next();
                g2D.setColor(Color.RED);
                g2D.fillOval((int) (((n.getLocation().x() - minX) / (maxX - minX)) * (width - 50) - RADIUS + 25), (int) (((n.getLocation().y() - minY) / (maxY - minY)) * (height - 150) - RADIUS + 100), 2 * RADIUS, 2 * RADIUS);
                g2D.setColor(Color.green);
                g2D.drawString(n.getKey() + "", (int) (((n.getLocation().x() - minX) / (maxX - minX)) * (width - 50) + 25 - 5), (int) (((n.getLocation().y() - minY) / (maxY - minY)) * (height - 150) + 100 + 5));
            }
        }
        if(center_node != -1) {
            NodeData n = dwga.getGraph().getNode(center_node);
            g2D.setColor(Color.YELLOW);
            g2D.fillOval((int)(((n.getLocation().x() - minX) / (maxX - minX)) * (width-50) - RADIUS + 25), (int)(((n.getLocation().y() - minY) / (maxY - minY)) * (height-150) - RADIUS + 100), 2 * RADIUS, 2 * RADIUS);
            g2D.setColor(Color.green);
            g2D.drawString(n.getKey() + "", (int)(((n.getLocation().x() - minX) / (maxX - minX)) * (width-50) + 25 - 5), (int)(((n.getLocation().y() - minY) / (maxY - minY)) * (height-150) + 100 + 5));
            center_node = -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadItem) {
            try {
                JFileChooser jFileChooser = new JFileChooser();
                int response = jFileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    String file = jFileChooser.getSelectedFile().getAbsolutePath();
                    boolean worked = dwga.load(file);
                    if (!worked) {
                        JOptionPane.showMessageDialog(null, "This file was not successfully loaded!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                repaint();
            }
            catch (Exception exp) {
                JOptionPane.showMessageDialog(null, "This file was not successfully loaded!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == saveItem) {
            try {
                JFileChooser jFileChooser = new JFileChooser();
                int response = jFileChooser.showSaveDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());
                    boolean worked = dwga.save(file.toString());
                    if (!worked) {
                        JOptionPane.showMessageDialog(null, "The graph was not successfully saved!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch (Exception exp) {
                JOptionPane.showMessageDialog(null, "The graph was not successfully saved!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == addNode) {
            while (true) {
                try {
                    String coordinatesX = JOptionPane.showInputDialog(null,"Enter X Coordinate new Node", "Add Node", JOptionPane.QUESTION_MESSAGE);
                    if (coordinatesX == null) {
                        break;
                    }
                    String coordinatesY = JOptionPane.showInputDialog(null,"Enter Y Coordinate new Node", "Add Node", JOptionPane.QUESTION_MESSAGE);
                    if (coordinatesY == null) {
                        break;
                    }
                    String coordinatesZ = JOptionPane.showInputDialog(null,"Enter Z Coordinate new Node", "Add Node", JOptionPane.QUESTION_MESSAGE);
                    if (coordinatesZ == null) {
                        break;
                    }
                    String ID = JOptionPane.showInputDialog(null,"Enter ID for this Node", "Add Node", JOptionPane.QUESTION_MESSAGE);
                    if (ID == null) {
                        break;
                    }
                    int id = Integer.parseInt(ID);
                    NodeData n1 = new Node(coordinatesX + "," + coordinatesY + "," + coordinatesZ, id);
                    dwga.getGraph().addNode(n1);
                    repaint();
                    break;
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == removeNode) {
            while (true) {
                try {
                    String id = JOptionPane.showInputDialog(null, "Enter the ID of the node you want to remove", "Remove Node", JOptionPane.QUESTION_MESSAGE);
                    if (id == null) break;
                    dwga.getGraph().removeNode(Integer.parseInt(id));
                    repaint();
                    break;
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == addEdge) {
            while (true) {
                try {
                    String source_node_key = JOptionPane.showInputDialog(null, "Enter Source node key", " Add Edge", JOptionPane.QUESTION_MESSAGE);
                    if (source_node_key == null) break;
                    String destination_node_key = JOptionPane.showInputDialog(null, "Enter Destination node key", "Add Edge", JOptionPane.QUESTION_MESSAGE);
                    if (destination_node_key == null) break;
                    String weight = JOptionPane.showInputDialog(null, "Enter edge weight", "Add Edge", JOptionPane.QUESTION_MESSAGE);
                    if (weight == null) break;
                    int sourceKey = Integer.parseInt(source_node_key);
                    int destKey = Integer.parseInt(destination_node_key);
                    double weightD = Double.parseDouble(weight);
                    dwga.getGraph().connect(sourceKey, destKey, weightD);
                    repaint();
                    break;
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == removeEdge) {
            while (true) {
                try {
                    String sourceNode = JOptionPane.showInputDialog(null, "Enter Source node key", "Remove Edge", JOptionPane.QUESTION_MESSAGE);
                    if (sourceNode == null) break;
                    String destNode = JOptionPane.showInputDialog(null, "Enter Destination node key", "Remove Edge", JOptionPane.QUESTION_MESSAGE);
                    if (destNode == null) break;
                    int sourceKey = Integer.parseInt(sourceNode);
                    int destKey = Integer.parseInt(destNode);
                    dwga.getGraph().removeEdge(sourceKey, destKey);
                    repaint();
                    break;
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == isConnected) {
            if (dwga.isConnected()) {
                JOptionPane.showMessageDialog(null, "This graph is connected!", "isConnected", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "This graph is NOT connected!", "isConnected", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (e.getSource() == shortestPathDist) {
            while (true) {
                try {
                    String sourceNode = JOptionPane.showInputDialog(null, "Enter Source node key", "Shortest Path Distance", JOptionPane.QUESTION_MESSAGE);
                    if (sourceNode == null) break;
                    String destNode = JOptionPane.showInputDialog(null, "Enter Destination node key", "Shortest Path Distance", JOptionPane.QUESTION_MESSAGE);
                    if (destNode == null) break;
                    double shortestdist = dwga.shortestPathDist(Integer.parseInt(sourceNode), Integer.parseInt(destNode));
                    if(shortestdist == -1) {
                        JOptionPane.showMessageDialog(null, "There is no path from Node: " + sourceNode + " to Node: " + destNode, "Shortest Path Distance", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "The shortest Distance from Node: " + sourceNode + " to Node: " + destNode + " is " + shortestdist, "Shortest Path Distance", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == shortestPath) {
            while (true) {
                try {
                    String sourceNode = JOptionPane.showInputDialog("Enter Source node key");
                    if (sourceNode == null) break;
                    String destNode = JOptionPane.showInputDialog("Enter Destination node key");
                    if (destNode == null) break;
                    ArrayList<NodeData> path = (ArrayList<NodeData>) dwga.shortestPath(Integer.parseInt(sourceNode), Integer.parseInt(destNode));
                    int source = path.get(0).getKey();
                    String pathString = source + "";
                    for (int i = 1; i < path.size(); i++) {
                        pathString += " -> " + path.get(i).getKey();
                    }
                    for (int i = 0; i < path.size() - 1; i++) {
                        toHighlight.add(dwga.getGraph().getEdge(path.get(i).getKey(), path.get(i + 1).getKey()));
                    }
                    repaint();
                    JOptionPane.showMessageDialog(null, "The shortest Distance Path from Node: " + sourceNode + " to Node: " + destNode + " is " + pathString, "Shortest Path", JOptionPane.PLAIN_MESSAGE);
                    break;
                }
                catch (NullPointerException exc) {
                    JOptionPane.showMessageDialog(null, "There is no path between these two nodes", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == center) {
            if (!dwga.isConnected()) {
                JOptionPane.showMessageDialog(null, "The graph is not connected, therefore there is no center!", "Center", JOptionPane.PLAIN_MESSAGE);
            }
            else {
                int centerNode = dwga.center().getKey();
                center_node = centerNode;
                repaint();
                JOptionPane.showMessageDialog(null, "The center node of the graph is " + centerNode, "Center", JOptionPane.PLAIN_MESSAGE);
            }
        }
        if (e.getSource() == tspGreedy) {
            while (true) {
                try {
                    String city = "";
                    ArrayList<NodeData> cities = new ArrayList<>();
                    String amountS = JOptionPane.showInputDialog(null, "Enter amount of nodes you'd like to add to set Cities", "TSP Greedy", JOptionPane.PLAIN_MESSAGE);
                    if (amountS == null) break;
                    int amount = Integer.parseInt(amountS);
                    while (amount != 0) {
                        city = JOptionPane.showInputDialog(null, "Enter node to be included in cities", "TSP Greedy", JOptionPane.PLAIN_MESSAGE);
                        if (city == null) break;
                        cities.add(dwga.getGraph().getNode(Integer.parseInt(city)));
                        amount--;
                    }
                    ArrayList<NodeData> tspPath = (ArrayList<NodeData>) dwga.tsp(cities);
                    int tspSrc = tspPath.get(0).getKey();
                    String tspString = tspSrc + "";
                    for (int i = 1; i < tspPath.size(); i++) {
                        tspString += " -> " + tspPath.get(i).getKey();
                    }
                    for (int i = 0; i < tspPath.size()-1; i++) {
                        toHighlight.add(dwga.getGraph().getEdge(tspPath.get(i).getKey(),tspPath.get(i+1).getKey()));
                    }
                    repaint();
                    JOptionPane.showMessageDialog(null, "The shortest path visiting all cities is: " + tspString, "TSP Greedy", JOptionPane.PLAIN_MESSAGE);
                    break;
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == tspLong) {
            while (true) {
                try {
                    String city = "";
                    ArrayList<NodeData> cities = new ArrayList<>();
                    String amountS = JOptionPane.showInputDialog(null, "Enter amount of nodes you'd like to add to set cities", "TSP Long", JOptionPane.PLAIN_MESSAGE);
                    if (amountS == null) break;
                    int amount = Integer.parseInt(amountS);
                    while (amount != 0) {
                        city = JOptionPane.showInputDialog(null, "Enter node to be included in cities", "TSP Long", JOptionPane.PLAIN_MESSAGE);
                        if (city == null) break;
                        cities.add(dwga.getGraph().getNode(Integer.parseInt(city)));
                        amount--;
                    }
                    ArrayList<NodeData> tspPath = (ArrayList<NodeData>) ((MyDirectedWeightedGraphAlgorithms) dwga).tspLong(cities);
                    int tspSrc = tspPath.get(0).getKey();
                    String tspString = tspSrc + "";
                    for (int i = 1; i < tspPath.size(); i++) {
                        tspString += " -> " + tspPath.get(i).getKey();
                    }
                    for (int i = 0; i < tspPath.size()-1; i++) {
                        toHighlight.add(dwga.getGraph().getEdge(tspPath.get(i).getKey(),tspPath.get(i+1).getKey()));
                    }
                    repaint();
                    JOptionPane.showMessageDialog(null, "The shortest path visiting all cities is: " + tspString, "TSP Long", JOptionPane.PLAIN_MESSAGE);
                    break;
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "You've entered incorrect data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}