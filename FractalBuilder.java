import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class FractalBuilder extends JFrame {

    int width;
    int height;
    public int nodes = Seed.treeNodes;

    public FractalBuilder() {
        super("Van Gogu");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();

        setBounds(0, 0, width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setVisible(true);
        this.setOpacity(1);
        this.setBackground(Color.blue);

    }

    public List<Integer> getSequence(int iterations) {
        List<Integer> turnSequence = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            List<Integer> copy = new ArrayList<>(turnSequence);
            Collections.reverse(copy);
            turnSequence.add(1);
            for (Integer turn : copy) {
                turnSequence.add(-turn);
            }
        }
        return turnSequence;
    }

    private void drawBranch(Graphics g, int x1, int y1, double angle, int depth, int max) {

        g.setColor(Seed.treeColor);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(2 * depth));

        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * depth * 10.0);
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * depth * 10.0);
        g2D.drawLine(x1, y1, x2, y2);
        drawTree(g, x2, y2, angle - Seed.branchAngle, depth - 1, max);
        drawTree(g, x2, y2, angle + Seed.branchAngle, depth - 1, max);

    }

    private void drawLeafs(Graphics g, int x1, int y1, double angle, int depth) {

        Random rand = new Random();

        float re = rand.nextFloat();
        float gr = rand.nextFloat();
        float bl = rand.nextFloat();

        Color randomColor = new Color(re, gr, bl);
        g.setColor(randomColor);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(2));

        List<Integer> turns;
        double side;
        int iter = Seed.leafIterations;

        turns = getSequence(iter * (depth + nodes) / nodes);
        side = 1.5 * (depth + nodes) / nodes;

        int x2 = x1 + (int) (Math.cos(angle) * side);
        int y2 = y1 + (int) (Math.sin(angle) * side);
        g.drawLine(x1, y1, x2, y2);
        x1 = x2;
        y1 = y2;
        for (Integer turn : turns) {
            angle += turn * (Math.PI / 2);
            x2 = x1 + (int) (Math.cos(angle) * side);
            y2 = y1 + (int) (Math.sin(angle) * side);
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }

    private void drawTree(Graphics g, int x1, int y1, double angle, int depth, int max) {


        if (depth == 0) { //draw only leafs
            drawLeafs(g, x1, y1, angle, depth);
        } else { //draw branches
            drawBranch(g, x1, y1, angle, depth, max);
            if (depth <= max - 3)
                drawLeafs(g, x1, y1, angle, depth);
        }
    }

    public void drawForest(Graphics g) {
        //versiunea non-random
        drawTree(g, 0 * (width / 3) + (width / 6), 1 * (height / 3), Seed.treeAngle, nodes - 2, nodes - 2);
        drawTree(g, 2 * (width / 3) + (width / 6), 2 * (height / 3), Seed.treeAngle, nodes - 1, nodes - 1);
        drawTree(g, 1 * (width / 3) + (width / 6), 3 * (height / 3) - (height / 12), Seed.treeAngle, nodes, nodes);

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Seed.backgroundColor);
        Rectangle r = new Rectangle(0, 0, width, height);
        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());

        drawForest(g);

    }
}