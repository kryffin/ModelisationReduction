package modelisationReduction.model.algorythms;

import modelisationReduction.model.Graphs.Edge;
import modelisationReduction.model.Graphs.Graph;
import modelisationReduction.model.Graphs.GraphArrayList;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class SeamCarving
{

    public static int[][] readpgm(String fn)
	 {		
        try {
            BufferedReader d = new BufferedReader(new FileReader(fn));
            String magic = d.readLine();
            String line = d.readLine();
            while (line.startsWith("#")) {
              line = d.readLine();
            }
            Scanner s = new Scanner(line);
            int width = s.nextInt();
            int height = s.nextInt();
            line = d.readLine();
            s = new Scanner(line);
            int maxVal = s.nextInt();
            int[][] im = new int[height][width];
            s = new Scanner(d);
            int count = 0;
            while (count < height*width) {
                im[count / width][count % width] = s.nextInt();
                count++;
            }
            return im;
        }

        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
	 }

    /**
     * Fonction lisant un fichier ppm et la stockant dans un tableau 2 dimensions [hauteur][largeur] de Pixels
     * @param fn nom du fichier à lire
     * @return tableau 2 dimensions [hauteur][largeur] de Pixels
     */
    public static Pixel[][] readppm(String fn)
    {
        try {

            //buffer de lecture pour le fichier en argument
            BufferedReader d = new BufferedReader(new FileReader(fn));
            String magic = d.readLine();

            //string contenant la ligne courante du fichier
            String line = d.readLine();

            //parcours des commentaires
            while (line.startsWith("#")) {
                line = d.readLine();
            }

            //scanner pour lecture des entiers
            Scanner s = new Scanner(line);

            //largeur et hauteur de l'image
            int width = s.nextInt(), height = s.nextInt();

            line = d.readLine();

            s = new Scanner(line);

            int maxVal = s.nextInt();

            //tableau de remplir grâce à la lecture de l'image
            Pixel[][] im = new Pixel[height][width];

            s = new Scanner(d);

            //parcours des lignes et colonnes de l'image
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {

                    //renseignement des canaux de couleurs de chaque pixel (Rouge, Vert et Bleu respectivement)
                    im[row][col] = new Pixel(s.nextInt(), s.nextInt(), s.nextInt());

                }
            }

            return im;
        }

        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
    }

    /**
     * Fonction d'écriture d'une image .pgm à partir d'un tableau à deux dimensions d'entiers
     * @param image tableau représentant l'image
     * @param filename nom du fichier dans lequel sauvegarder l'image
     */
    public static void writepgm (int[][] image, String filename) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        /* initialisation du fichier pgm */

        StringBuilder pgm = new StringBuilder();    //instanciation d'un StringBuilder contenant les 'pixels' à écrire
        pgm.append("P2\n");                         //format pgm de type P2
        pgm.append(width + " " + height + "\n");    //écriture de la largeur suivi de la hauteur
        pgm.append("255\n");                        //écriture de la valeur max (255)

        /* parcours du tableau et écriture dans le StringBuilder */

        //parcours du tableau
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                pgm.append(image[row][col] + " ");  //renseignement du pixel correspondant

            }
            pgm.append("\n");                       //retour chariot à chaque fin de ligne de pixels
        }

        //écriture dans le fichier spécifié
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false));
            bw.write(pgm.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction d'écriture d'une image .ppm à partir d'un tableau à 2 dimensions de Pixels
     * @param image tableau représentant l'image
     * @param filename nom du fichier dans lequel sauvegarder l'image
     */
    public static void writeppm (Pixel[][] image, String filename) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        /* initialisation du fichier pgm */

        StringBuilder ppm = new StringBuilder();    //instanciation d'un StringBuilder contenant les 'pixels' à écrire
        ppm.append("P3\n");                         //format pgm de type P2
        ppm.append(width + " " + height + "\n");    //écriture de la largeur suivi de la hauteur
        ppm.append("255\n");                        //écriture de la valeur max (255)

        /* parcours du tableau et écriture dans le StringBuilder */

        //parcours du tableau
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                ppm.append(image[row][col].getR() + " ");  //renseignement du canal rouge du pixel correspondant
                ppm.append(image[row][col].getG() + " ");  //renseignement du canal vert du pixel correspondant
                ppm.append(image[row][col].getB() + " ");  //renseignement du canal bleu du pixel correspondant


            }
            ppm.append("\n");                       //retour chariot à chaque fin de ligne de pixels
        }

        //écriture dans le fichier spécifié
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false));
            bw.write(ppm.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction retournant un tableau deux dimensions représentant l'intérêt de chaque pixels
     * @param image image à analyser
     * @return tableau 2d représentant l'intérêt de l'image
     */
    public static int[][] interest (int[][] image) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        //initialisation du tableau d'intérêts
        int[][] interest = new int[height][width];

        //parcours du tableau
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                //moyenne du/des voisin(s) du pixel courant
                int average;

                if (col == 0) {

                    //la moyenne vaut le pixel suivant si il n'y a pas de voisin de gauche
                    average = image[row][col+1];

                } else if (col == width-1) {

                    //la moyenne vaut le pixel précédent si il n'y a pas de voisin de droite
                    average = image[row][col-1];

                } else {

                    //la moyenne est faite entre le voisin de droite et gauche
                    average = (image[row][col-1] + image[row][col+1]) / 2;

                }

                //calcul de la différence absolue et stockage dans le tableau d'intérêt
                interest[row][col] = Math.abs(image[row][col] - average);

            }
        }

        return interest;
    }

    /**
     * Fonction retournant un tableau deux dimensions d'entiers représentant l'intérêt de chaque pixels
     * @param image image à analyser
     * @return tableau 2d représentant l'intérêt de l'image
     */
    public static int[][] interest (Pixel[][] image) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        //initialisation du tableau d'intérêts
        int[][] interest = new int[height][width];

        //parcours du tableau
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                //moyenne du/des voisin(s) du pixel courant
                int average;

                if (col == 0) {

                    //la moyenne vaut la moyenne pixel suivant si il n'y a pas de voisin de gauche
                    average = Pixel.average(image[row][col+1]);

                } else if (col == width-1) {

                    //la moyenne vaut la moyenne du pixel précédent si il n'y a pas de voisin de droite
                    average = Pixel.average(image[row][col-1]);

                } else {

                    //la moyenne est faite entre le pixel voisin de droite et gauche
                    average = Pixel.average(new Pixel(image[row][col-1], image[row][col+1]));

                }

                //calcul de la différence absolue et stockage dans le tableau d'intérêt
                interest[row][col] = Math.abs(Pixel.average(image[row][col]) - average);

            }
        }

        return interest;
    }

    /**
     * Fonction retournant un graphe (DAG) orienté pondéré représentant le tableau deux dimensions passé en argument
     * @param itr tableau 2d à convertir en graphe
     * @return
     */
    public static Graph tograph (int[][] itr) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = itr[0].length;
        int height = itr.length;

        //initialisation du graphe avec largeur * hauteur + 2 sommets (nombre de pixels, sommet source et sommet destination)
        Graph g = new GraphArrayList((width * height) + 2);

        //création des arêtes depuis le sommet source (le premier du haut)
        for (int i = 1; i <= width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(0, i, 0));
        }

        //sommet courant pour le parcours
        int vertex = 1;

        //parcours du tableau
        for (int row = 0; row < height-1; row++) {
            for (int col = 0; col < width; col++) {

                if (col == 0) {

                    //si le sommet représente un pixel de gauche on le lie au pixel d'en dessous ainsi que celui d'en dessous à droite
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, itr[row][col]));

                } else if (col == width-1) {

                    //si le sommet représente un pixel de droite on le lie au pixel d'en dessous ainsi que celui d'en dessous à gauche
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[row][col]));

                } else {

                    //si le sommet représente un pixel n'étant pas sur le bord de l'image on le lie aux trois voisins de dessous
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, itr[row][col]));

                }

                vertex++;

            }
        }

        //création des arêtes en direction du sommet destination (le dernier du bas)
        for (int i = 0; i < width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1 ,itr[height-1][i]));
        }

        return g;
    }

    /**
     * Fonction d'énergie avant pour la construction d'un graphe orienté pondéré à partir d'une image (tableau 2d)
     * @param image image à convertir en graphe
     * @return graphe représentant l'image par fonction d'énergie avant
     */
    public static Graph energieAvant (int[][] image) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        //initialisation du graphe avec largeur * hauteur + 2 sommets (nombre de pixels, sommet source et sommet destination)
        Graph g = new GraphArrayList((width * height) + 2);

        //création des arêtes depuis le sommet source (le premier du haut)
        for (int i = 1; i <= width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(0, i, 0));
        }

        //sommet courant pour le parcours
        int vertex = 1;

        //parcours du tableau
        for (int row = 0; row < height-1; row++) {
            for (int col = 0; col < width; col++) {

                if (col == 0) {

                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, image[row][col+1]));

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, image[row][col+1]));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, Math.abs(image[row][col+1] - image[row+1][col])));
                    }

                } else if (col == width-1) {

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, image[row][col-1]));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, Math.abs(image[row][col-1] - image[row+1][col])));
                    }

                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, image[row][col-1]));

                } else {

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, image[row][col-1]));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, Math.abs(image[row][col-1] - image[row+1][col])));
                    }

                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, Math.abs(image[row][col+1] - image[row][col-1])));

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, image[row][col+1]));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, Math.abs(image[row][col+1] - image[row+1][col])));
                    }

                }

                vertex++;

            }
        }

        //création des arêtes en direction du sommet destination (le dernier du bas)
        for (int i = 0; i < width; i++) {

            if (i == 0) {

                ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1, image[height-1][i+1]));

            } else if (i == width - 1) {

                ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1, image[height-1][i-1]));

            } else {

                ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1, Math.abs(image[height-1][i-1] - image[height-1][i+1])));

            }

        }

        return g;
    }

    /**
     * Fonction d'énergie avant pour la construction d'un graphe orienté pondéré à partir d'une image (tableau 2d de pixels)
     * @param image image à convertir en graphe
     * @return graphe représentant l'image par fonction d'énergie avant
     */
    public static Graph energieAvant (Pixel[][] image) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        //initialisation du graphe avec largeur * hauteur + 2 sommets (nombre de pixels, sommet source et sommet destination)
        Graph g = new GraphArrayList((width * height) + 2);

        //création des arêtes depuis le sommet source (le premier du haut)
        for (int i = 1; i <= width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(0, i, 0));
        }

        //sommet courant pour le parcours
        int vertex = 1;

        //parcours du tableau
        for (int row = 0; row < height-1; row++) {
            for (int col = 0; col < width; col++) {

                if (col == 0) {

                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, Pixel.average(image[row][col+1])));

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, Pixel.average(image[row][col+1])));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, Pixel.average(Pixel.absoluteDifference(image[row][col+1], image[row+1][col]))));

                    }

                } else if (col == width-1) {

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, Pixel.average(image[row][col-1])));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, Pixel.average(Pixel.absoluteDifference(image[row][col-1], image[row+1][col]))));
                    }

                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, Pixel.average(image[row][col-1])));

                } else {

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, Pixel.average(image[row][col-1])));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, Pixel.average(Pixel.absoluteDifference(image[row][col-1], image[row+1][col]))));
                    }

                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, Pixel.average(Pixel.absoluteDifference(image[row][col+1], image[row][col-1]))));

                    if (row == height - 2) {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, Pixel.average(image[row][col+1])));
                    } else {
                        ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, Pixel.average(Pixel.absoluteDifference(image[row][col+1], image[row+1][col]))));
                    }

                }

                vertex++;

            }
        }

        //création des arêtes en direction du sommet destination (le dernier du bas)
        for (int i = 0; i < width; i++) {

            if (i == 0) {

                ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1, Pixel.average(image[height-1][i+1])));

            } else if (i == width - 1) {

                ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1, Pixel.average(image[height-1][i-1])));

            } else {

                ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1, Pixel.average(Pixel.absoluteDifference(image[height-1][i-1], image[height-1][i+1]))));

            }

        }

        return g;
    }

    /**
     * Fonction retournant une liste d'entiers (sommets du graphe) dans l'ordre suivi par le tri topologique appliqué au graphe
     * @param g graphe à analyser
     * @return liste d'entiers (sommets du graphe)
     */
    public static ArrayList<Integer> tritopo (Graph g) {
        return DFS.depthFirstSearch(g);
    }

    /**
     * Fonction appliquant l'algorithme de Bellman-Ford (amélioré pour un DAG) sur un graphe depuis un sommet vers un autre,
     * retournant une liste d'entiers (sommets du graphe) dans l'ordre du plus court chemin
     * @param g graphe sur lequel appliquer Bellman-Ford
     * @param s sommet source de la recherche de chemin
     * @param t sommet final de la recherche de chemin
     * @param order ordre (tri topologique) améliorant la recherche (uniquement pour un DAG)
     * @return liste d'entiers (sommets) dans l'ordre du plus court chemin
     */
    public static ArrayList<Integer> Bellman (Graph g, int s, int t, ArrayList<Integer> order) {

        /* initialisation des distances */

        //tableau d'entiers de taille le nombre de sommets
        int[] distances = new int[g.vertices()];

        //initialisation des distances à l'infini (valeur Integer maximum / faux infini mais satisfaisant ici)
        for (int i = 0; i < g.vertices(); i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        //initialisation de la valeur du sommet source à 0
        distances[s] = 0;

        /* HashMap du chemin parcouru */

        /*
        clé -> élément est ici représenté par sommet -> parent
        exemple : path<1, 0> signifie que le parent de 1 est 0
        */

        HashMap<Integer, Integer> path = new HashMap<>();

        //initialisation des 'parents' de chaque sommet à null
        for (int i = 0; i < g.vertices(); i++) {
            path.put(i, null);
        }

        /* parcours du graphe */

        //sommet actuel
        int u;

        //parcours des sommets du graphe
        for (int k = 0; k < g.vertices(); k++) {

            //récupération de l'élément k de la liste (tri topologique), c'est l'élément à parcourir
            u = order.get(k);

            //pour chaque arêtes partante de u
            for (Edge e : g.next(u)) {

                //si la distance vers v (arête u -> v) est inférieure à la distance vers u + poids de l'arête on ne le modifie pas,
                //sinon on lui donne la distance vers u + poids de l'arête et on ajoute ce chemin dans la HashMap (sommet -> parent)
                if (distances[e.to] < distances[e.from] + e.cost) {
                    distances[e.to] = distances[e.to];
                } else {
                    distances[e.to] = distances[e.from] + e.cost;
                    path.put(e.to, e.from);
                }

            }

        }

        /* consultation et sauvegarde du chemin parcouru de poids le plus faible */

        //sommet courant, en commencant par le sommet final
        Integer current = t;

        //liste d'entiers représentant le plus court chemin en partant de la fin
        ArrayList<Integer> shortestPath = new ArrayList<>();

        //tant que le parent n'est pas null, on ajoute le sommet à la liste
        while (current != null) {
            shortestPath.add(current);      //ajout à la liste
            current = path.get(current);    //avancement dans le 'parent' du 'sommet' actuel
        }

        //inversion de la liste pour un plus court chemin dans le bon ordre
        Collections.reverse(shortestPath);

        return shortestPath;
    }
   
}
