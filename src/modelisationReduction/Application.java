package modelisationReduction;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author KLEINHENTZ 'Kryffin' Nicolas
 */

public class Application {

    /**
     * Fonction de lancement de l'application prenant en argument le fichier .pgm source, le .pgm cible
     * @param args arguments de lancement
     */
    public static void main (String[] args) {
        //test sur les arguments du programme
        if (args.length != 2) {
            System.out.println("Le programme nécessite 2 arguments : java Application [fichierSource(.pgm)] [fichierCible(.pgm)]");
            return;
        }

        //ajout de l'extension si elle n'est pas présente dans le nom du fichier source
        if (!args[0].endsWith(".pgm")) {
            args[0] += ".pgm";
        }

        //lecture du fichier source dans un tableau d'entier
        int[][] image = SeamCarving.readpgm(args[0]);

        /* initialisation des variables */

        //nombre de colonnes à retirer
        int ite = 50;

        //affichage de début de traitement
        System.out.println("\nRéduction de " + ite + " colonnes depuis l'image " + args[0] + " vers l'image " + args[1]);

        //tableau d'entiers représentant l'intérêt de chaque pixel
        int[][] interet;

        //graphe représentant l'image avec des arêtes pondérée vers les 3 voisins du bas depuis chaque pixel
        Graph g;

        //liste des sommets du graphe (pixels de l'image avec le sommet source et destination), ordonné par tri topologique
        ArrayList<Integer> list;

        //liste des sommets (pixels) du graphe, ordonné pour le meilleur chemin possible par l'algorithme de Bellman-Ford
        ArrayList<Integer> bestPath;

        //largeur et hauteur de l'image
        int width = image[0].length, height = image.length;

        //nouvelle image contenant l'image source sans n colonnes
        int[][] newImage = new int[height][width-1];

        /* traitement de l'image */

        System.out.println("Début du traitement...");

        //itérations pour chaque colonne à retirer (on va de 1 à ite plutôt que 0 à ite-1 uniquement pour l'affichage du pourcentage de traitement)
        for (int i = 1; i <= ite; i++) {

            //appel du ramasse miettes
            System.gc();

            //récupération de l'intérêt de l'image
            interet = SeamCarving.interest(image);

            //production du graphe de l'intérêt de l'image
            g = SeamCarving.tograph(interet);

            //production du tri topologique sur le graphe
            list = SeamCarving.tritopo(g);

            //calcul du meilleur chemin par l'algorithme de Bellman-Ford entre le sommet source et le sommet destination (haut et bas)
            //modifié/amélioré pour suivre le tri topologique car le graphe est un DAG
            bestPath = SeamCarving.Bellman(g, 0, (interet.length * interet[0].length) + 1, list);

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
                    if (!bestPath.contains(pixel)) {
                        newImage[row][newCol] = image[row][col];
                        newCol++;
                    }

                    pixel++;

                }
                newCol = 0; //réinitialisation de la colonne
            }

            //réduction du nombre de colonnes de l'image
            width--;

            //on réitérera sur l'image réduite
            image = newImage;

            //affichage du pourcentage de traitement
            System.out.print("\rTraitement : " + String.format(Locale.FRANCE,"%.2f", ((float) i / (float) ite) * 100.f) + " %");

        }

        /* sauvegarde de l'image modifiée */

        //ajout de l'extension si elle n'est pas présente dans le nom du fichier destination
        if (!args[1].endsWith(".pgm")) {
            args[1] += ".pgm";
        }

        //écriture de l'image dans le fichier destination
        SeamCarving.writepgm(newImage, args[1]);

        //affichage de confirmation de la fin du programme
        System.out.println("\nTraitement terminé, nouvelle image enregistrée sous " + args[1] + "\n");
    }

}
