package modelisationReduction;

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

    public static void writepgm (int[][] image, String filename) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = image[0].length;
        int height = image.length;

        //instanciation d'un StringBuilder contenant les 'pixels' à écrire
        StringBuilder pgm = new StringBuilder("");
        pgm.append("P2\n");
        pgm.append(width + " " + height + "\n"); //écriture de la largeur suivi de la hauteur
        pgm.append("255\n"); //écriture de la valeur max (255)

        //parcours du tableau et écriture dans le StringBuilder
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pgm.append(image[row][col] + " ");
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
        int width = image[0].length;
        int height = image.length;

        int[][] interest = new int[height][width];

        //parcours du tableau
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                int average; //moyenne

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
                if (average < image[row][col]) {
                    interest[row][col] = Math.abs(image[row][col] - average);
                } else {
                    interest[row][col] = Math.abs(average - image[row][col]);
                }

            }
        }

        return interest;
    }

    public static Graph tograph (int[][] itr) {
        //récupération de la taille (largeur et hauteur) du tableau de 'pixels'
        int width = itr[0].length;
        int height = itr.length;

        Graph g = new GraphArrayList((width * height) + 2);

        for (int i = 1; i <= width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(0, i, 0));
        }

        int vertex = 1;

        //parcours du tableau
        for (int row = 0; row < height-1; row++) {
            for (int col = 0; col < width; col++) {

                if (col == 0) {
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, itr[row][col]));
                } else if (col == width-1) {
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[row][col]));
                } else {
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width - 1, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width, itr[row][col]));
                    ((GraphArrayList) g).addEdge(new Edge(vertex, vertex + width + 1, itr[row][col]));
                }

                vertex++;
            }
        }

        for (int i = 0; i < width; i++) {
            ((GraphArrayList) g).addEdge(new Edge(((width*height) - (width-1)) + i,(width * height) + 1 ,itr[height-1][i]));
        }

        return g;
    }

    public static ArrayList<Integer> tritopo (Graph g) {
        return DFS.dfs(g);
    }

    public static ArrayList<Integer> Bellman (Graph g, int s, int t, ArrayList<Integer> order) {
        int[] distances = new int[g.vertices()];
        for (int i = 0; i < g.vertices(); i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[s] = 0;

        //sauvegarde des chemins dans une hashmap
        HashMap<Integer, Integer> path = new HashMap<>();
        for (int i = 0; i < g.vertices(); i++) {
            path.put(i, null);
        }

        int v;
        for (int k = 0; k < g.vertices(); k++) {

            v = order.get(k);
            for (Edge e : g.prev(v)) {

                if (distances[e.to] < distances[e.from] + e.cost) {
                    distances[e.to] = distances[e.to];
                } else {
                    distances[e.to] = distances[e.from] + e.cost;
                    path.put(e.to, e.from);
                }

            }

        }

        Integer current = t;
        ArrayList<Integer> shortestPath = new ArrayList<>();
        while (current != null) {
            shortestPath.add(current);
            current = path.get(current);
        }
        Collections.reverse(shortestPath);

        return shortestPath;
    }
   
}
