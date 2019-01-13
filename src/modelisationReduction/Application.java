package modelisationReduction;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author KLEINHENTZ 'Kryffin' Nicolas
 */

public class Application {

    /**
     * Fonction retournant un tableau d'entiers
     * @param image tableau à retourner
     * @return tableau d'entiers 2 dimensions retourné
     */
    public static int[][] flip (int[][] image) {
        int width = image[0].length;
        int height = image.length;

        int[][] flippedImage = new int[width][height];

        //parcours de l'image originale
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                flippedImage[col][row] = image[row][col];

            }
        }

        return flippedImage;
    }

    /**
     * Fonction retournant un tableau de pixels
     * @param image tableau à retourner
     * @return tableau de pixels 2 dimensions retourné
     */
    public static Pixel[][] flip (Pixel[][] image) {
        int width = image[0].length;
        int height = image.length;

        Pixel[][] flippedImage = new Pixel[width][height];

        //parcours de l'image originale
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                flippedImage[col][row] = image[row][col];

            }
        }

        return flippedImage;
    }

    /**
     * Fonction de lancement de l'application prenant en argument le fichier .pgm ou .ppm source et destination
     * @param args arguments de lancement
     */
    public static void main (String[] args) {
        //test sur les arguments du programme
        if (args.length != 2) {
            System.out.println("Le programme nécessite 2 arguments : java Application [fichierSource.pgm] [fichierCible.pgm]");
            return;
        }

        if (args[0].endsWith(".pgm") && args[1].endsWith(".pgm")) {
            pgm(args);
        } else if (args[0].endsWith(".ppm") && args[1].endsWith(".ppm")) {
            ppm(args);
        } else {
            System.out.println("Les fichiers n'ont pas d'extensions ou ne sont pas de la même extension.");
            return;
        }
    }

    /**
     * Fonction traitant une image .pgm (en niveaux de gris)
     * @param args arguments de lancement
     */
    private static void pgm (String[] args) {
        //lecture du fichier source dans un tableau d'entier
        int[][] image = SeamCarving.readpgm(args[0]);

        /* initialisation des variables */

        //nombre de colonnes à retirer
        int iteCol = 200;

        //nombre de lignes à retirer
        int iteRow = 0;

        //affichage de début de traitement
        System.out.println("\nRéduction de " + iteCol + " colonnes et " + iteRow + " lignes depuis l'image " + args[0] + " vers l'image " + args[1]);

        //graphe représentant l'image avec des arêtes pondérée vers les 3 voisins du bas depuis chaque pixel par fonction d'énergie avant
        Graph g;

        //liste des sommets du graphe (pixels de l'image avec le sommet source et destination), ordonné par tri topologique
        ArrayList<Integer> list;

        //liste des sommets (pixels) du graphe, ordonné pour le meilleur chemin possible par l'algorithme de Bellman-Ford
        ArrayList<Integer> bestPath;

        //largeur et hauteur de l'image
        int width = image[0].length, height = image.length;

        //nouvelle image contenant l'image source -1 colonne
        int[][] newImage = new int[height][width-1];

        /* traitement de l'image */

        System.out.println("Début du traitement...");

        /* suppression des colonnes */

        //itérations pour chaque colonne à retirer (on va de 1 à iteCol plutôt que 0 à iteCol-1 uniquement pour l'affichage du pourcentage de traitement)
        for (int i = 1; i <= iteCol; i++) {

            //appel du ramasse miettes
            System.gc();

            //production du graphe de l'image par fonction d'énergie avant
            g = SeamCarving.energieAvant(image);

            //production du tri topologique sur le graphe
            list = SeamCarving.tritopo(g);

            //calcul du meilleur chemin par l'algorithme de Bellman-Ford entre le sommet source et le sommet destination (haut et bas)
            //modifié/amélioré pour suivre le tri topologique car le graphe est un DAG
            bestPath = SeamCarving.Bellman(g, 0, (image.length * image[0].length) + 1, list);

            //je retire le premier élément 0 qui n'est pas un pixel
            bestPath.remove(0);

            //écrasement de la nouvelle image pour redimensionnement
            newImage = new int[height][width-1];

            //pixel à traiter (nom du sommet)
            int pixel = 1;

            //colonne de la nouvelle image (finira à col-1 car on retire une colonne lors du parcours de l'image)
            int newCol = 0;

            //parcours des pixels de de l'image
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {

                    //si le pixel en lecture n'est pas à supprimer, on l'affecte à la nouvelle image et on incrémente la colonne de la nouvelle image
                    if (pixel != bestPath.get(0)) {
                        newImage[row][newCol] = image[row][col];
                        newCol++;
                    } else {
                        //si le pixel est à supprimer il est donc traité et on avance dans la liste
                        bestPath.remove(0);
                    }

                    //itération du pixel courant
                    pixel++;

                }
                newCol = 0; //réinitialisation de la colonne
            }

            //réduction du nombre de colonnes de l'image
            width--;

            //on réitérera sur l'image réduite
            image = newImage;

            //affichage du pourcentage de traitement
            System.out.print("\rTraitement des colonnes : " + String.format(Locale.FRANCE,"%.2f", ((float) i / (float) iteCol) * 100.f) + " %");

        }

        System.out.println();

        /* suppression des lignes */

        //itérations pour chaque lignes à retirer (on va de 1 à iteRow plutôt que 0 à iteRow-1 uniquement pour l'affichage du pourcentage de traitement)
        for (int i = 1; i <= iteRow; i++) {

            //appel du ramasse miettes
            System.gc();

            //retournement de l'image pour la traiter comme des colonnes (qui sont en fait des lignes)
            int[][] flippedImage = flip(image);

            //production du graphe de l'image par fonction d'énergie avant
            g = SeamCarving.energieAvant(flippedImage);

            //production du tri topologique sur le graphe
            list = SeamCarving.tritopo(g);

            //calcul du meilleur chemin par l'algorithme de Bellman-Ford entre le sommet source et le sommet destination (haut et bas)
            //modifié/amélioré pour suivre le tri topologique car le graphe est un DAG
            bestPath = SeamCarving.Bellman(g, 0, (image.length * image[0].length) + 1, list);

            //je retire le premier élément 0 qui n'est pas un pixel
            bestPath.remove(0);

            //écrasement de la nouvelle image pour redimensionnement
            newImage = new int[height-1][width];

            //pixel à traiter (nom du sommet)
            int pixel = 1;

            //colonne de la nouvelle image (finira à col-1 car on retire une colonne lors du parcours de l'image)
            int newRow = 0;

            //parcours des pixels de de l'image
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {

                    //si le pixel en lecture n'est pas à supprimer, on l'affecte à la nouvelle image et on incrémente la colonne de la nouvelle image
                    if (pixel != bestPath.get(0)) {
                        newImage[newRow][col] = image[row][col];
                        newRow++;
                    } else {
                        //si le pixel est à supprimer il est donc traité et on avance dans la liste
                        bestPath.remove(0);
                    }

                    //itération du pixel courant
                    pixel++;

                }
                newRow = 0; //réinitialisation de la colonne
            }

            //réduction du nombre de colonnes de l'image
            height--;

            //on réitérera sur l'image réduite
            image = newImage;

            //affichage du pourcentage de traitement
            System.out.print("\rTraitement des lignes : " + String.format(Locale.FRANCE,"%.2f", ((float) i / (float) iteRow) * 100.f) + " %");

        }

        /* sauvegarde de l'image modifiée */

        //écriture de l'image dans le fichier destination
        SeamCarving.writepgm(newImage, args[1]);

        //affichage de confirmation de la fin du programme
        System.out.println("\nTraitement terminé, nouvelle image enregistrée sous " + args[1] + "\n");
    }

    /**
     * Fonction traitant une image .ppm (en couleur)
     * @param args arguments de lancement
     */
    private static void ppm (String[] args) {

        //lecture du fichier source dans un tableau de Pixels
        Pixel[][] image = SeamCarving.readppm(args[0]);

        /* initialisation des variables */

        //nombre de colonnes à retirer
        int iteCol = 200;

        //nombre de lignes à retirer
        int iteRow = 0;

        //affichage de début de traitement
        System.out.println("\nRéduction de " + iteCol + " colonnes et " + iteRow + " lignes depuis l'image " + args[0] + " vers l'image " + args[1]);

        //graphe représentant l'image avec des arêtes pondérée vers les 3 voisins du bas depuis chaque pixel par fonction d'énergie avant
        Graph g;

        //liste des sommets du graphe (pixels de l'image avec le sommet source et destination), ordonné par tri topologique
        ArrayList<Integer> list;

        //liste des sommets (pixels) du graphe, ordonné pour le meilleur chemin possible par l'algorithme de Bellman-Ford
        ArrayList<Integer> bestPath;

        //largeur et hauteur de l'image
        int width = image[0].length, height = image.length;

        //nouvelle image contenant l'image source -1 colonne
        Pixel[][] newImage = new Pixel[height][width-1];

        /* traitement de l'image */

        System.out.println("Début du traitement...");

        /* suppression des colonnes */

        //itérations pour chaque colonne à retirer (on va de 1 à iteCol plutôt que 0 à iteCol-1 uniquement pour l'affichage du pourcentage de traitement)
        for (int i = 1; i <= iteCol; i++) {

            //appel du ramasse miettes
            System.gc();

            //production du graphe de l'image par fonction d'énergie avant
            g = SeamCarving.energieAvant(image);

            //production du tri topologique sur le graphe
            list = SeamCarving.tritopo(g);

            //calcul du meilleur chemin par l'algorithme de Bellman-Ford entre le sommet source et le sommet destination (haut et bas)
            //modifié/amélioré pour suivre le tri topologique car le graphe est un DAG
            bestPath = SeamCarving.Bellman(g, 0, (image.length * image[0].length) + 1, list);

            //je retire le premier élément 0 qui n'est pas un pixel
            bestPath.remove(0);

            //écrasement de la nouvelle image pour redimensionnement
            newImage = new Pixel[height][width-1];

            //pixel à traiter (nom du sommet)
            int pixel = 1;

            //colonne de la nouvelle image (finira à col-1 car on retire une colonne lors du parcours de l'image)
            int newCol = 0;

            //parcours des pixels de de l'image
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {

                    //si le pixel en lecture n'est pas à supprimer, on l'affecte à la nouvelle image et on incrémente la colonne de la nouvelle image
                    if (pixel != bestPath.get(0)) {
                        newImage[row][newCol] = image[row][col];
                        newCol++;
                    } else {
                        //si le pixel est à supprimer il est donc traité et on avance dans la liste
                        bestPath.remove(0);
                    }

                    //itération du pixel courant
                    pixel++;

                }
                newCol = 0; //réinitialisation de la colonne
            }

            //réduction du nombre de colonnes de l'image
            width--;

            //on réitérera sur l'image réduite
            image = newImage;

            //affichage du pourcentage de traitement
            System.out.print("\rTraitement des colonnes : " + String.format(Locale.FRANCE,"%.2f", ((float) i / (float) iteCol) * 100.f) + " %");

        }

        System.out.println();

        /* suppression des lignes */

        //itérations pour chaque lignes à retirer (on va de 1 à iteRow plutôt que 0 à iteRow-1 uniquement pour l'affichage du pourcentage de traitement)
        for (int i = 1; i <= iteRow; i++) {

            //appel du ramasse miettes
            System.gc();

            //retournement de l'image pour la traiter comme des colonnes (qui sont en fait des lignes)
            Pixel[][] flippedImage = flip(image);

            //production du graphe de l'image par fonction d'énergie avant
            g = SeamCarving.energieAvant(flippedImage);

            //production du tri topologique sur le graphe
            list = SeamCarving.tritopo(g);

            //calcul du meilleur chemin par l'algorithme de Bellman-Ford entre le sommet source et le sommet destination (haut et bas)
            //modifié/amélioré pour suivre le tri topologique car le graphe est un DAG
            bestPath = SeamCarving.Bellman(g, 0, (image.length * image[0].length) + 1, list);

            //je retire le premier élément 0 qui n'est pas un pixel
            bestPath.remove(0);

            //écrasement de la nouvelle image pour redimensionnement
            newImage = new Pixel[height-1][width];

            //pixel à traiter (nom du sommet)
            int pixel = 1;

            //colonne de la nouvelle image (finira à col-1 car on retire une colonne lors du parcours de l'image)
            int newRow = 0;

            //parcours des pixels de de l'image
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {

                    //si le pixel en lecture n'est pas à supprimer, on l'affecte à la nouvelle image et on incrémente la colonne de la nouvelle image
                    if (pixel != bestPath.get(0)) {
                        newImage[newRow][col] = image[row][col];
                        newRow++;
                    } else {
                        //si le pixel est à supprimer il est donc traité et on avance dans la liste
                        bestPath.remove(0);
                    }

                    //itération du pixel courant
                    pixel++;

                }
                newRow = 0; //réinitialisation de la colonne
            }

            //réduction du nombre de colonnes de l'image
            height--;

            //on réitérera sur l'image réduite
            image = newImage;

            //affichage du pourcentage de traitement
            System.out.print("\rTraitement des lignes : " + String.format(Locale.FRANCE,"%.2f", ((float) i / (float) iteRow) * 100.f) + " %");

        }

        /* sauvegarde de l'image modifiée */

        //écriture de l'image dans le fichier destination
        SeamCarving.writeppm(newImage, args[1]);

        //affichage de confirmation de la fin du programme
        System.out.println("\nTraitement terminé, nouvelle image enregistrée sous " + args[1] + "\n");
    }

}
