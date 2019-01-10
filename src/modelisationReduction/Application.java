package modelisationReduction;

import java.util.ArrayList;

public class Application {

    /**
     * Fonction de lancement de l'application prenant en argument le fichier .pgm source, le .pgm cible et le nombre de colonne à retirer
     * @param args arguments de lancement
     */
    public static void main (String[] args) {
        //test sur les arguments du programme
        if (args.length != 3) {
            System.out.println("Le programme nécessite 2 arguments : java Application [fichierSource(.pgm)] [fichierCible(.pgm)] [nombre d'itérations]");
            return;
        }

        //ajout de l'extension si elle n'est pas présente dans le nom du fichier source
        if (!args[0].endsWith(".pgm")) {
            args[0] += ".pgm";
        }

        //lecture du fichier source dans un tableau d'entier
        int[][] image = SeamCarving.readpgm(args[0]);

        /* initialisation des variables */

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

        //récupération du 3ème argument étant le nombre de colonnes à retirer
        int ite = Integer.parseInt(args[2]);

        /* traitement de l'image */

        //itérations pour chaque colonne à retirer
        for (int i = 0; i < ite; i++) {

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

        }

        /* sauvegarde de l'image modifiée */

        //ajout de l'extension si elle n'est pas présente dans le nom du fichier destination
        if (!args[1].endsWith(".pgm")) {
            args[1] += ".pgm";
        }

        //écriture de l'image dans le fichier destination
        SeamCarving.writepgm(newImage, args[1]);
    }

}
