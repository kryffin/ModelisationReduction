package modelisationReduction;

import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    public static void main (String[] args) {
        int[][] image = new int[][] {{3, 8, 200}, {11, 21, 60}, {24, 29, 25}, {39, 39, 0}};

        System.out.println(Arrays.deepToString(image));

        System.out.println("Intérêt :");

        int[][] interet = SeamCarving.interest(image);

        System.out.println(Arrays.deepToString(interet));

        Graph g = SeamCarving.tograph(interet);

        g.writeFile("graph.dot");

        ArrayList<Integer> list = SeamCarving.tritopo(g);

        System.out.println(list);

        System.out.println("Cout minimal : " + SeamCarving.Bellman(g, 0, 13, list));
    }

}
