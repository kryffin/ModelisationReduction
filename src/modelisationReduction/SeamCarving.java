package modelisationReduction;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;

public class SeamCarving
{

    public static int[][] readpgm(String fn)
	 {		
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
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

    public static void writepgm (int[][] image, String filename) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image.length;
        int height = image[0].length;

        //instanciation d'un StringBuilder contenant les 'pixels' à écrire
        StringBuilder pgm = new StringBuilder("");
        pgm.append(width + " " + height + "\n"); //écriture de la largeur suivi de la hauteur
        pgm.append("255\n"); //écriture de la valeur max (255)

        //parcours du tableau et écriture dans le StringBuilder
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pgm.append(image[col][row] + " ");
            }
            pgm.append("\n");
        }

        //vérification de l'extension du fichier à créer
        if (!filename.endsWith(".pgm")) {
            filename += ".pgm";
        }

        //écriture dans le fichier spécifié
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(pgm.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] interest (int[][] image) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image.length;
        int height = image[0].length;

        int[][] interest = new int[width][height];

        //parcours du tableau
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                int average; //moyenne

                if (col == 0) {
                    //la moyenne vaut le pixel suivant si il n'y a pas de voisin de gauche
                    average = image[col+1][row];
                } else if (col == width-1) {
                    //la moyenne vaut le pixel précédent si il n'y a pas de voisin de droite
                    average = image[col-1][row];
                } else {
                    //la moyenne est faite entre le voisin de droite et gauche
                    average = (image[col-1][row] + image[col+1][row]) / 2;
                }

                //calcul de la différence absolue et stockage dans le tableau d'intérêt
                if (average < image[col][row]) {
                    interest[col][row] = Math.abs(image[col][row] - average);
                } else {
                    interest[col][row] = Math.abs(average - image[col][row]);
                }

            }
        }

        return interest;
    }

    public static Graph tograph (int[][] itr) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = itr.length;
        int height = itr[0].length;

        Graph g = new GraphArrayList((width * height) + 2);

        for (int i = 1; i <= width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(0, i, 0));
        }

        int vertex = 1;

        //parcours du tableau
        for (int row = 0; row < height-1; row++) {
            for (int col = 0; col < width; col++) {

                if (col == 0) {
                    System.out.println(vertex + " : à gauche");
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[col][row]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, itr[col][row]));
                } else if (col == width-1) {
                    System.out.println(vertex + " : à droite");
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, itr[col][row]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[col][row]));
                } else {
                    System.out.println(vertex + " : au milieu");
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, itr[col][row]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[col][row]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, itr[col][row]));
                }

                vertex++;
            }
        }

        for (int i = 0; i < width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1 ,itr[i][height-1]));
        }

        return g;
    }

    public static ArrayList<Integer> tritopo (Graph g) {
        return DFS.dfs(g);
    }

    public static int Bellman (Graph g, int s, int t, ArrayList<Integer> order) {
        int[] distances = new int[g.vertices()];
        for (int i = 0; i < g.vertices(); i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[s] = 0;

        for (int k = 1; k < g.vertices(); k++) {

            for (int u = 0; u < g.vertices(); u++) {
                for (Edge e : g.next(u)) {
                    distances[e.to] = Math.min(distances[e.to], distances[e.from] + e.cost);
                }
            }

        }

        return distances[t];
    }
   
}
