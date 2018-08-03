package inlupp2_prog2;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import javax.swing.*;

public class Place extends JComponent {

    private String name;
    protected Position p;
    private String category;
    private boolean marked;

    protected static final int[] xPoint = {20, 10, 0};
    protected static final int[] yPoint = {0, 20, 0};

    public Place(String category, Position p, String name) {
        this.name = name;
        this.p = p;
        this.category = category;

        setBounds(p.getXCoordinate() - 10, p.getYCoordinate() - 10, 20, 20);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }

    Place() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return p;
    }

    public String getCategory() {
        return category;
    }

    public void setMarked(boolean b) {
        marked = b;
    }

    public boolean getMarked() {
        return this.marked;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (category.equals("Bus")) {
            g.setColor(Color.RED);
        } else if (category.equals("Underground")) {
            g.setColor(Color.BLUE);
        } else if (category.equals("Train")) {
            g.setColor(Color.GREEN);
        } else if (category.equals("None")) {
            g.setColor(Color.BLACK);
        }
        if (marked) {
            System.out.println("Going to marked...");

            requestFocusInWindow();
            g.setColor(Color.YELLOW);
        }

        g.fillPolygon(xPoint, yPoint, 3);

    }

    @Override
    public String toString() {
        return "Place is " + category + "," + p.getXCoordinate() + "," + p.getYCoordinate() + "," + name;
    }

}
