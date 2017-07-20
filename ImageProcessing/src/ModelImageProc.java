import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Dzmitry on 04.07.2017.
 */
public class ModelImageProc {
    private File openedFile;
    private File forSavingFile;
    private BufferedImage openedImage;
    private BufferedImage forSavingImage;

    public BufferedImage fileToImageBuffered(File file) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public void saveImageBufferedToFile(File file, BufferedImage bufferedImage) {
        String filename = file.toString();
        if (!filename.endsWith(".jpg"))
            filename += ".jpg";
        try {
            ImageIO.write(bufferedImage, "jpg", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage sepia(BufferedImage image, int sepiaDept) {
        int width = 0;
        int height = 0;

        try {
            width = image.getWidth();
            height = image.getHeight();
        } catch (NullPointerException e) {
            System.err.println(e);
        }

        if (sepiaDept < 0) sepiaDept = 0;
        if (sepiaDept > 50) sepiaDept = 50;

        BufferedImage temp = new BufferedImage(width, height, image.getType());

        int[][] redVector = new int[width][height];
        int[][] greenVector = new int[width][height];
        int[][] blueVector = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                redVector[i][j] = new Color(image.getRGB(i, j)).getRed();
                greenVector[i][j] = new Color(image.getRGB(i, j)).getGreen();
                blueVector[i][j] = new Color(image.getRGB(i, j)).getBlue();
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    redVector[i][j] = greenVector[i][j] = blueVector[i][j] = (int) (0.299 * redVector[i][j] + 0.587 * greenVector[i][j] + 0.114 * blueVector[i][j]);
                    redVector[i][j] = redVector[i][j] + (sepiaDept * 2);
                    greenVector[i][j] = greenVector[i][j] + sepiaDept;
                } catch (ArithmeticException e) {
                }
                temp.setRGB(i, j, new Color(normalize(redVector[i][j]), normalize(greenVector[i][j]), normalize(blueVector[i][j])).getRGB());
            }
        }
        return temp;
    }

    public BufferedImage linearContrast(BufferedImage image, int gMin, int gMax) {
        int width = 0;
        int height = 0;
        try {
            width = image.getWidth();
            height = image.getHeight();
        } catch (NullPointerException e) {
            System.err.println(e);
        }

        BufferedImage temp = new BufferedImage(width, height, image.getType());

        int[][] redVector = new int[width][height];
        int[][] greenVector = new int[width][height];
        int[][] blueVector = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                redVector[i][j] = new Color(image.getRGB(i, j)).getRed();
                greenVector[i][j] = new Color(image.getRGB(i, j)).getGreen();
                blueVector[i][j] = new Color(image.getRGB(i, j)).getBlue();
            }
        }

        int fMaxRed = getMaxValue(redVector);
        int fMaxGreen = getMaxValue(greenVector);
        int fMaxBlue = getMaxValue(blueVector);
        int fMinRed = getMinValue(redVector);
        int fMinGreen = getMinValue(greenVector);
        int fMinBlue = getMinValue(greenVector);

        int g = gMax - gMin;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    redVector[i][j] = (redVector[i][j] - fMinRed) * g / (fMaxRed - fMinRed) + gMin;
                    greenVector[i][j] = (greenVector[i][j] - fMinGreen) * g / (fMaxGreen - fMinGreen) + gMin;
                    blueVector[i][j] = (blueVector[i][j] - fMinBlue) * g / (fMaxBlue - fMinBlue) + gMin;
                } catch (ArithmeticException e) {
                }
                temp.setRGB(i, j, new Color(normalize(redVector[i][j]), normalize(greenVector[i][j]), normalize(blueVector[i][j])).getRGB());
            }
        }
        return temp;
    }

    public BufferedImage operatorRoberts(BufferedImage image) {
        int width = 0;
        int height = 0;

        try {
            width = image.getWidth();
            height = image.getHeight();
        } catch (NullPointerException e) {
            System.err.println(e);
        }

        BufferedImage temp = new BufferedImage(width, height, image.getType());

        int[][] redVector = new int[width][height];
        int[][] greenVector = new int[width][height];
        int[][] blueVector = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                redVector[i][j] = new Color(image.getRGB(i, j)).getRed();
                greenVector[i][j] = new Color(image.getRGB(i, j)).getGreen();
                blueVector[i][j] = new Color(image.getRGB(i, j)).getBlue();
            }
        }

        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height - 1; y++) {
                try {

                    /*redVector[x][y] = (int) Math.sqrt((redVector[x + 1][y] - redVector[x][y + 1]) * (redVector[x + 1][y] - redVector[x][y + 1]) +
                            (redVector[x][y] - redVector[x + 1][y + 1]) * (redVector[x][y] - redVector[x + 1][y + 1]));

                    greenVector[x][y] = (int) Math.sqrt((greenVector[x + 1][y] - greenVector[x][y + 1]) * (greenVector[x + 1][y] - greenVector[x][y + 1]) +
                            (greenVector[x][y] - greenVector[x + 1][y + 1]) * (greenVector[x][y] - greenVector[x + 1][y + 1]));

                    blueVector[x][y] = (int) Math.sqrt((blueVector[x + 1][y] - blueVector[x][y + 1]) * (blueVector[x + 1][y] - blueVector[x][y + 1]) +
                            (blueVector[x][y] - blueVector[x + 1][y + 1]) * (blueVector[x][y] - blueVector[x + 1][y + 1]));*/

                    redVector[x][y] = Math.abs(redVector[x][y] - redVector[x + 1][y + 1]) +
                            Math.abs(redVector[x + 1][y] - redVector[x][y + 1]);
                    greenVector[x][y] = Math.abs(greenVector[x][y] - greenVector[x + 1][y + 1]) +
                            Math.abs(greenVector[x + 1][y] - greenVector[x][y + 1]);
                    blueVector[x][y] = Math.abs(blueVector[x][y] - blueVector[x + 1][y + 1]) +
                            Math.abs(blueVector[x + 1][y] - blueVector[x][y + 1]);
                } catch (ArithmeticException e) {
                }
                temp.setRGB(x, y, new Color(normalize(redVector[x][y]), normalize(greenVector[x][y]), normalize(blueVector[x][y])).getRGB());
            }
        }
        return temp;
    }

    public int normalize(int pixel) {
        if (pixel < 0) pixel = 0;
        if (pixel > 255) pixel = 255;
        return pixel;
    }

    public static int getMaxValue(int[][] numbers) {
        int maxValue = numbers[0][0];
        for (int j = 0; j < numbers.length; j++) {
            for (int i = 0; i < numbers[j].length; i++) {
                if (numbers[j][i] > maxValue) {
                    maxValue = numbers[j][i];
                }
            }
        }
        return maxValue;
    }

    public static int getMinValue(int[][] numbers) {
        int minValue = numbers[0][0];
        for (int j = 0; j < numbers.length; j++) {
            for (int i = 0; i < numbers[j].length; i++) {
                if (numbers[j][i] < minValue) {
                    minValue = numbers[j][i];
                }
            }
        }
        return minValue;
    }

    public File getOpenedFile() {
        return openedFile;
    }

    public void setOpenedFile(File openedFile) {
        this.openedFile = openedFile;
    }

    public File getForSavingFile() {
        return forSavingFile;
    }

    public void setForSavingFile(File forSavingFile) {
        this.forSavingFile = forSavingFile;
    }

    public BufferedImage getForSavingImage() {
        return forSavingImage;
    }

    public void setForSavingImage(BufferedImage forSavingImage) {
        this.forSavingImage = forSavingImage;
    }

    public BufferedImage getOpenedImage() {
        return openedImage;
    }

    public void setOpenedImage(BufferedImage openedImage) {
        this.openedImage = openedImage;
    }
}
