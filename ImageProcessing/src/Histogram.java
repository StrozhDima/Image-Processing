import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * Created by Dzmitry on 04.07.2017.
 */
public class Histogram extends JLabel {
    private int SIZE = 256;
    // Red, Green, Blue, Bright
    private int NUMBER_OF_CHANNELS = 4;

    private final int RED = 0;
    private final int GREEN = 1;
    private final int BLUE = 2;
    private final int BRIGHT = 3;

    private int[][] colourBins;
    private volatile boolean loaded = false;
    private int maxY;

    public Histogram() {
        colourBins = new int[NUMBER_OF_CHANNELS][];

        for (int i = 0; i < NUMBER_OF_CHANNELS; i++) {
            colourBins[i] = new int[SIZE];
        }
        loaded = false;
    }

    public void load(BufferedImage bufferedImage) {

        // обнуляю все пиксели
        for (int i = 0; i < NUMBER_OF_CHANNELS; i++) {
            for (int j = 0; j < SIZE; j++) {
                colourBins[i][j] = 0;
            }
        }

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                Color c = new Color(bufferedImage.getRGB(x, y));
                colourBins[RED][c.getRed()]++;
                colourBins[GREEN][c.getGreen()]++;
                colourBins[BLUE][c.getBlue()]++;
                colourBins[BRIGHT][(int) (0.3 * c.getRed() + 0.59 * c.getGreen() + 0.11 * c.getBlue())]++;
            }
        }
        maxY = 0;

        for (int i = 0; i < NUMBER_OF_CHANNELS; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (maxY < colourBins[i][j]) {
                    maxY = colourBins[i][j];
                }
            }
        }
        loaded = true;
    }

    @Override
    public void paint(Graphics g) {
        if (loaded) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            int xInterval = (int) ((double) getWidth() / ((double) SIZE - 1));
            g2.setColor(Color.black); //?????

            for (int i = 0; i < NUMBER_OF_CHANNELS; i++) {
                if (i == RED) {
                    g2.setColor(Color.red);
                } else if (i == GREEN) {
                    g2.setColor(Color.green);
                } else if (i == BLUE) {
                    g2.setColor(Color.blue);
                } else if (i == BRIGHT) {
                    g2.setColor(Color.black);
                }

                // рисование графиков разных цветов по точкам.
                for (int j = 0; j < SIZE - 1; j++) {
                    int value = (int) (((double) colourBins[i][j] / (double) maxY) * getHeight());
                    int value2 = (int) (((double) colourBins[i][j + 1] / (double) maxY) * getHeight());
                    g2.drawLine(j * xInterval, getHeight() - value, (j + 1) * xInterval, getHeight() - value2);
                    //System.err.println(j + " -  x1, y1: " + j * xInterval + ", " + (getHeight() - value) + "   x2, y2: " + (j + 1) * xInterval + ", " + (getHeight() - value2));
                }
            }
        } else {
            super.paint(g);
        }
    }

    void showHistogram(BufferedImage bufferedImage, String tittle) {
        JFrame jFrame = new JFrame("Histogram. " + tittle);
        jFrame.setSize(517, 460);
        jFrame.setResizable(false);
        jFrame.setLayout(new BorderLayout());
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        load(bufferedImage);
        jFrame.add(this, BorderLayout.CENTER);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
    }
}