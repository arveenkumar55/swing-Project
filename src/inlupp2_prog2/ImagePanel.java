package inlupp2_prog2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class ImagePanel extends JPanel {

    BufferedImage image;

    public ImagePanel() {
    }

    public void setImage(String path) {

        try {
            image = ImageIO.read(new File(path));
            int w = image.getWidth();
            int h = image.getHeight();
            setPreferredSize(new Dimension(w, h));
            setMaximumSize(new Dimension(w, h));
            setMinimumSize(new Dimension(w, h));
            setBounds(0, 0, w, h);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not open file, please choose another " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException np) {
            JOptionPane.showMessageDialog(null, "Could not open file, please choose another " + np, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}
