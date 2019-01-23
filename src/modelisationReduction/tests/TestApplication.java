package modelisationReduction.tests;

import modelisationReduction.graphs.GraphImplicit;
import modelisationReduction.pixels.ColorPixel;
import modelisationReduction.pixels.GrayPixel;
import modelisationReduction.pixels.Pixel;
import modelisationReduction.algorythms.SeamCarving;
import modelisationReduction.application.Application;
import modelisationReduction.graphs.Graph;

import java.util.ArrayList;
import java.util.Arrays;

public class TestApplication {

    public static void main (String[] args) {

        /* tests pgm */

        Pixel[][] imageP = new GrayPixel[][] {{new GrayPixel(3), new GrayPixel(11), new GrayPixel(24), new GrayPixel(39)},
                                            {new GrayPixel(8), new GrayPixel(21), new GrayPixel(29), new GrayPixel(39)},
                                            {new GrayPixel(200), new GrayPixel(60), new GrayPixel(25), new GrayPixel(0)}};

        System.out.println("Image :\n" + Arrays.deepToString(imageP));

        Pixel[][] flippedImage = Application.flip(imageP);

        System.out.println("Image retournée :\n" + Arrays.deepToString(flippedImage));

        flippedImage = Application.flip(flippedImage);

        System.out.println("Image retournée :\n" + Arrays.deepToString(flippedImage));

        int[][] image = new int[][] {{3, 11, 24, 39},
                                    {8, 21, 29, 39},
                                    {200, 60, 25, 0}};

        System.out.println("Intérêt :");

        int[][] interet = SeamCarving.interest(image);

        System.out.println(Arrays.deepToString(interet));

        Graph g = SeamCarving.tograph(interet);

        g.writeFile("graph.dot");

        Graph g1 = SeamCarving.energieAvant(image);

        g1.writeFile("graph1.dot");

        System.out.println("tri topo :");

        ArrayList<Integer> list = SeamCarving.tritopo(g);

        System.out.println(list);

        System.out.println("Cout minimal : " + SeamCarving.Bellman(g, 0, 13, list));

        /* tests ppm */

        Pixel[][] imagePpm = new Pixel[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                imagePpm[row][col] = new ColorPixel((row + col) * 50, (row + col) * 50, (row + col) * 50);
            }
        }

        System.out.println(Arrays.deepToString(imagePpm));

        System.out.println("\n--- Test GraphImplicit ---\n");

        Graph gi = new GraphImplicit(interet, interet[0].length, interet.length);

        System.out.println("tri topo :");

        list = SeamCarving.tritopo(gi);

        System.out.println(list);
    }

}
