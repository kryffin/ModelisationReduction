package modelisationReduction;

import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    public static void main (String[] args) {
        int[][] image = new int[][] {{3, 11, 24, 39},
                                     {8, 21, 29, 39},
                                     {200, 60, 25, 0}};

        SeamCarving.writepgm(image, "/Users/nicolasklz/test.pgm");

        System.out.println("Image :\n" + Arrays.deepToString(image));

        int[][] flippedImage = Application.flip(image);

        System.out.println("Image retournée :\n" + Arrays.deepToString(flippedImage));

        flippedImage = Application.flip(flippedImage);

        System.out.println("Image retournée :\n" + Arrays.deepToString(flippedImage));

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
    }

}
