package common.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(String imagePath) {
        setOpaque(false);
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                backgroundImage = ImageIO.read(is);
            } else {
                System.err.println("Could not find resource: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        } else {
            g.setColor(new Color(74, 74, 74));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}