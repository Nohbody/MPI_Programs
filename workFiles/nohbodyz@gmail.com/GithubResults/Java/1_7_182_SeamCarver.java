import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

/**
 * Created by zora on 4/3/16.
 */
public class SeamCarver {
    private static final int MARGIN_ENERGY = 1000;
    private int height, width;
    private Picture picture;
    // Store RGB information for each pixel
    private int[][] pixels;

    // Create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.picture.setOriginUpperLeft();
        height = this.picture.height();
        width = this.picture.width();

        // Generate pixel arrays
        pixels = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = this.picture.get(i, j).getRGB();
            }
        }
    }

    // Return current picture
    public Picture picture() {
        Color tempColor;
        picture = new Picture(width, height);
        // Reconstruct picture from pixel information
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tempColor = new Color(pixels[i][j]);
                picture.set(i, j, tempColor);
            }
        }
        return picture;
    }

    // width of current picture
    public int width()
    {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public  double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1)
            throw new java.lang.IndexOutOfBoundsException("Index out of range");
        return calculateEnergy(x, y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[][] path = new int[width][height];
        int[] minPath = new int[width];
        double minTotalEnergy;

        if (width <= 1 || height <= 1) {
            // If only single line: find the pixel with smallest energy change
            minTotalEnergy = calculateEnergy(0, 0);
            minPath[0] = 0;
            for (int j = 1; j < height; j++) {
                if (calculateEnergy(0, j) < minTotalEnergy) {
                    minTotalEnergy = calculateEnergy(0, j);
                    minPath[0] = j;
                }
            }
        }
        else {
            // For iteration
            double[][] totalEnergy = new double[2][height];
            int dummy = 0, minFinal;

            // Initialize path and total energy
            for (int i = 0; i < width; i++) {
                path[i][0] = -1;
                path[i][height - 1] = -1;
            }
            for (int j = 0; j < height; j++) {
                path[0][j] = -1;
                totalEnergy[dummy][j] = calculateEnergy(dummy, j);
                totalEnergy[1 - dummy][j] = totalEnergy[dummy][j];
            }

            // Search path
            for (int i = 1; i < width; i++) {
                totalEnergy[1 - dummy][0] = calculateEnergy(1 - dummy, 0) + totalEnergy[dummy][1];
                totalEnergy[1 - dummy][height - 1] = calculateEnergy(1 - dummy, height - 1) + totalEnergy[dummy][height - 2];
                path[i][0] = 1;
                path[i][height - 1] = 1;
                // Choose the shortest path(parent) from three parents
                for (int j = 1; j < height - 1; j++) {
                    if (totalEnergy[dummy][j - 1] <= totalEnergy[dummy][j]) {
                        if (totalEnergy[dummy][j - 1] <= totalEnergy[dummy][j + 1]) {
                            totalEnergy[1 - dummy][j] = totalEnergy[dummy][j - 1] + calculateEnergy(i, j);
                            path[i][j] = j - 1;
                        } else {
                            totalEnergy[1 - dummy][j] = totalEnergy[dummy][j + 1] + calculateEnergy(i, j);
                            path[i][j] = j + 1;
                        }
                    } else {
                        if (totalEnergy[dummy][j] <= totalEnergy[dummy][j + 1]) {
                            totalEnergy[1 - dummy][j] = totalEnergy[dummy][j] + calculateEnergy(i, j);
                            path[i][j] = j;
                        } else {
                            totalEnergy[1 - dummy][j] = totalEnergy[dummy][j + 1] + calculateEnergy(i, j);
                            path[i][j] = j + 1;
                        }
                    }
                }
                dummy = 1 - dummy;
            }

            // Find endpoint
            minTotalEnergy = totalEnergy[dummy][0];
            minFinal = 0;
            for (int j = 1; j < height; j++) {
                if (totalEnergy[dummy][j] < minTotalEnergy) {
                    minTotalEnergy = totalEnergy[dummy][j];
                    minFinal = j;
                }
            }

            // Trace back
            minPath[width - 1] = minFinal;
            for (int i = width - 1; i > 0; i--) {

                minPath[i - 1] = path[i][minPath[i]];
            }
        }
        return minPath;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // Transpose and then call findHorizontalSeam
        int[] minPath;
        int temp;
        int[][] tempPixels = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tempPixels[i][j] = pixels[i][j];
            }
        }

        pixels = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = tempPixels[j][i];
            }
        }
        temp = height;
        height = width;
        width = temp;
        minPath = findHorizontalSeam();

        temp = width;
        width = height;
        height = temp;
        pixels = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = tempPixels[i][j];
            }
        }
        return minPath;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new java.lang.NullPointerException("Object not found");
        if (height <= 1 || seam.length != width) throw new java.lang.IllegalArgumentException("Cannot seam");
        if (seam[0] < 0 || seam[0] >= height) throw new java.lang.IllegalArgumentException("Wrong entry in seam[]");
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height || seam[i] - seam[i - 1] > 1 || seam[i] - seam[i - 1] < -1)
                throw new java.lang.IllegalArgumentException("Wrong entry in seam[]");
        }

        for (int i = 0; i < seam.length; i++) {
            for (int j = seam[i]; j < height - 1; j++) {
                pixels[i][j] = pixels[i][j + 1];
            }
        }
        height -= 1;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new java.lang.NullPointerException("Object not found");
        if (width <= 1 || seam.length != height) throw new java.lang.IllegalArgumentException("Cannot seam");
        if (seam[0] < 0 || seam[0] >= width) throw new java.lang.IllegalArgumentException("Wrong entry in seam[]");
        for (int j = 1; j < seam.length; j++) {
            if (seam[j] < 0 || seam[j] >= width || seam[j] - seam[j - 1] > 1 || seam[j] - seam[j - 1] < -1)
                throw new java.lang.IllegalArgumentException("Wrong entry in seam[]");
        }

        for (int j = 0; j < seam.length; j++) {
            for (int i = seam[j]; i < width - 1; i++) {
                pixels[i][j] = pixels[i + 1][j];
            }
        }
        width -= 1;
    }

    private double calculateEnergy(int x, int y) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return 1000;
        }
        else {
            Color color1, color2;
            double squareX, squareY, result;

            color1 = new Color(pixels[x - 1][y]);
            color2 = new Color(pixels[x + 1][y]);
            squareX = Math.pow(color1.getRed() - color2.getRed(), 2) + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                        + Math.pow(color1.getBlue() - color2.getBlue(), 2);
            color1 = new Color(pixels[x][y - 1]);
            color2 = new Color(pixels[x][y + 1]);
            squareY = Math.pow(color1.getRed() - color2.getRed(), 2) + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                    + Math.pow(color1.getBlue() - color2.getBlue(), 2);
            result = Math.sqrt(squareX + squareY);
            return result;
        }
    }

    public static void main(String[] args) { }
}
