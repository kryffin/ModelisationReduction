package modelisationReduction;

import javafx.application.Platform;

import java.util.ArrayList;

public class Application {

    public static void main (String[] args) {
        if (args.length != 3) {
            System.out.println("Le programme nécessite 2 arguments : java Application [fichierSource.pgm] [fichierCible.pgm] [nombre d'itérations]");
            return;
        }

        /*
        On convertit l'image fournie en tableau d'intérêt, on le convertit ensuite en graphe sur lequel on effectuera Bellman
        pour retrouver le chemin ayant le moins d'intérêt.
        D'une manière ou d'une autre on garde ce parcours en mémoire pour supprimer les pixels parcourus sur l'image.
         */

        if (!args[0].endsWith(".pgm")) {
            args[0] += ".pgm";
        }

        int[][] image = SeamCarving.readpgm(args[0]);

        int[][] interet = SeamCarving.interest(image);
        SeamCarving.writepgm(interet, "i_" + args[1]);

        Graph g;

        ArrayList<Integer> list;

        ArrayList<Integer> bestPath;

        int width = image[0].length - 1, height = image.length;
        int[][] newImage = new int[height][width];

        int ite = Integer.parseInt(args[2]);

        for (int i = 0; i < ite; i++) {

            interet = SeamCarving.interest(image);

            g = SeamCarving.tograph(interet);

            list = SeamCarving.tritopo(g);

            bestPath = SeamCarving.Bellman(g, 0, (interet.length * interet[0].length) + 1, list);

            newImage = new int[height][width];

            int pixel = 1, newCol = 0;
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {

                    //si le pixel en lecture n'est pas à supprimer, on l'affecte à la nouvelle image et on incrémente la colonne de la nouvelle image
                    if (!bestPath.contains(pixel)) {
                        try {
                            newImage[row][newCol] = image[row][col];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(height + " : " + row + " | " + width + " : " + newCol + " - " + col + " :: " + image.length + " / " + newImage.length);
                            e.printStackTrace();
                            return;
                        }
                        newCol++;
                    }
                    pixel++;

                }
                newCol = 0;
            }

            width--;
            image = newImage;

        }

        if (!args[1].endsWith(".pgm")) {
            args[1] += ".pgm";
        }

        SeamCarving.writepgm(newImage, args[1]);
    }

}
