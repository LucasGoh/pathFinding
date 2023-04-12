package bfsSearch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    int SCREEN_HEIGHT_WIDTH = 800;
    int SQAURE_SIZE = 40;
    int NUMBER_SQAURE = SCREEN_HEIGHT_WIDTH / SQAURE_SIZE;

    int[][] nodes;
    Map<Node, Node> parents;
    List<Node> temp;
    Node start = null;
    Node end = null;
    boolean reachDestination;
    List<Node> obstacles_list;

    Timer timer;

    public GamePanel() {

        this.setPreferredSize(new Dimension(SCREEN_HEIGHT_WIDTH, SCREEN_HEIGHT_WIDTH));
        this.setBackground(Color.white);
        this.setFocusable(true);

        // initialize blocks to 0
        // 0 unvisited
        // 1 visited
        // 9 end point
        // 3 obstacle
        nodes = new int[100][100];
        for (int i = 0; i < NUMBER_SQAURE; i++) {
            for (int j = 0; j < NUMBER_SQAURE; j++) {
                nodes[i][j] = 0;
            }
        }

        // obstacles points
        // List<int [][]> obstacles_list;
        obstacles_list = new ArrayList<Node>();
        for (int i = 3; i < 19; i++) {
            obstacles_list.add(new Node(i, 3, nodes[i][3] = 3));
        }

        temp = new ArrayList<Node>();
        // start , end point
        start = new Node(5, 1, 1);
        end = new Node(15, 18, nodes[15][18] = 9);

        parents = new HashMap<Node, Node>();
        parents.put(start, null);
        temp.add(start);

        reachDestination = false;

        timer = new Timer(10, this);
        timer.start();

    }

    private List<Node> getChildren(Node parent) {
        List<Node> children = new ArrayList<Node>();
        int x = parent.getX();
        int y = parent.getY();

        if (x - 1 >= 0 && nodes[x - 1][y] == 9) {
            Node child = new Node(x - 1, y, 9);
            children.add(child);
        }
        if (y - 1 >= 0 && nodes[x][y - 1] == 9) {
            Node child = new Node(x, y - 1, 9);
            children.add(child);
        }
        if (x + 1 < 40 && nodes[x + 1][y] == 9) {
            Node child = new Node(x + 1, y, 9);
            children.add(child);
        }
        if (y + 1 < 40 && nodes[x][y + 1] == 9) {
            Node child = new Node(x, y + 1, 9);
            children.add(child);
        }

        if (x - 1 >= 0 && nodes[x - 1][y] == 0) {
            if (nodes[x - 1][y] != 9)
                nodes[x - 1][y] = 1;
            Node child = new Node(x - 1, y, nodes[x - 1][y]);
            children.add(child);

        }
        if (y - 1 >= 0 && nodes[x][y - 1] == 0) {
            if (nodes[x][y - 1] != 9)
                nodes[x][y - 1] = 1;
            Node child = new Node(x, y - 1, nodes[x][y - 1]);
            children.add(child);

        }
        if (x + 1 <= NUMBER_SQAURE && nodes[x + 1][y] == 0) {
            if (nodes[x + 1][y] != 9)
                nodes[x + 1][y] = 1;
            Node child = new Node(x + 1, y, nodes[x + 1][y]);
            children.add(child);
        }
        if (y + 1 <= NUMBER_SQAURE && nodes[x][y + 1] == 0) {
            if (nodes[x][y + 1] != 9)
                nodes[x][y + 1] = 1;
            Node child = new Node(x, y + 1, nodes[x][y + 1]);
            children.add(child);
        }

        if (nodes[x][y] == 0) {

        }
        return children;

    }
    // TODO Auto-generated constructor stub

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (temp.size() > 0) {
            Node currentNode = temp.remove(0);
            List<Node> children = getChildren(currentNode);
            for (Node child : children) {
                // Node can only be visted once
                if (!parents.containsKey(child)) {
                    parents.put(child, currentNode);

                    int value = child.getValue();
                    if (value == 1) {
                        temp.add(child);
                    } else if (value == 9) {
                        temp.add(child);
                        reachDestination = true;

                    }
                }
            }
        }

        repaint();
    }

    public void printPath(List<Node> path, Graphics g) {
        for (int i = 0; i < path.size() - 1; i++) {
            Node node = path.get(i);
            g.setColor(Color.yellow);
            g.fillRect(node.getX() * SQAURE_SIZE, node.getY() * SQAURE_SIZE, SQAURE_SIZE, SQAURE_SIZE);

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);

        // draw grid
        for (int i = 0; i < NUMBER_SQAURE; i++) {
            g.setColor(Color.black);
            g.drawLine(i * SQAURE_SIZE, 0, i * SQAURE_SIZE, SCREEN_HEIGHT_WIDTH);
            g.drawLine(0, i * SQAURE_SIZE, SCREEN_HEIGHT_WIDTH, i * SQAURE_SIZE);
        }

        // iterate paretns.key rectangle set red
        for (Map.Entry<Node, Node> set : parents.entrySet()) {
            if (set.getKey() == null)
                continue;
            g.setColor(Color.cyan);
            g.fillRect(SQAURE_SIZE * (set.getKey().getX()), SQAURE_SIZE * (set.getKey().getY()), SQAURE_SIZE,
                    SQAURE_SIZE);
        }

        // iterate temp rectangle set blue
        for (int i = 0; i < temp.size(); i++) {
            g.setColor(Color.green);
            g.fillRect(SQAURE_SIZE * (temp.get(i).getX()), SQAURE_SIZE * (temp.get(i).getY()), SQAURE_SIZE,
                    SQAURE_SIZE);
        }

        // print path
        if (temp.size() == 0) {
            timer.stop();
            Node node = end;
            List<Node> path = new ArrayList<Node>();
            while (node != null) {
                path.add(0, node);
                node = parents.get(node);
            }
            printPath(path, g);

        }

        // obstacles
        for (int i = 0; i < obstacles_list.size(); i++) {
            g.setColor(Color.black);
            g.fillRect(SQAURE_SIZE * obstacles_list.get(i).getX(), SQAURE_SIZE * obstacles_list.get(i).getY(),
                    SQAURE_SIZE, SQAURE_SIZE);
        }

        // starting point
        g.setColor(Color.green);
        g.fillRect(SQAURE_SIZE * (start.getX()), SQAURE_SIZE * start.getY(), SQAURE_SIZE, SQAURE_SIZE);

        // ending point
        g.setColor(Color.red);
        g.fillRect(SQAURE_SIZE * (end.getX()), SQAURE_SIZE * end.getY(), SQAURE_SIZE, SQAURE_SIZE);

    }
}
