package modelisationReduction.graphs;

import java.util.ArrayList;

public class GraphImplicit extends Graph{
    int N;
    int width;
    int height;
    int[][] interest;
    
@SuppressWarnings("unchecked")    
    GraphImplicit(int N){
	this.N = N;
    }

    public GraphImplicit (int[][] interest, int w, int h) {
        this((w * h) + 2);
        this.width = w;
        this.height = h;
        this.interest = interest;
    }

    public int vertices(){
	return N;
    }
    
@SuppressWarnings("unchecked")    
    public Iterable<Edge> next(int u)
	 {
	     ArrayList<Edge> edges = new ArrayList();

	     if (u == 0) {
	         //premier sommet
	         for (int i = 1; i <= width; i++) {
	             edges.add(new Edge(u, i, 0));
             }
         } else if (u < vertices() - 1 && u >= vertices() - width - 1) {
	         //derniere ligne de sommets
             edges.add(new Edge(u, vertices() - 1, interest[(u-1) / width][u % width]));
         } else if (u > 0 && u < vertices() - width - 1) {
             //sommets intermédiaires

             if (u % width == 0) {
                 //arête vers bas gauche
                 edges.add(new Edge(u, (u + width) - 1, interest[(u-1) / width][(u-1) % width]));
             }

             //arête vers le bas
             edges.add(new Edge(u, u + width, interest[(u-1) / width][(u-1) % width]));

             if (u % (width + 1) == 0) {
                 //arête vers bas droite
                 edges.add(new Edge(u, (u + width) + 1, interest[(u-1) / width][(u-1) % width]));
             }
         }

	     return edges;
		      
	 }
@SuppressWarnings("unchecked")
   public Iterable<Edge> prev(int u)
	 {
	     ArrayList<Edge> edges = new ArrayList();

         if (u == vertices() - 1) {
             //dernier sommet
             for (int i = vertices() - width - 1; i < vertices() - 1; i++) {
                 edges.add(new Edge(i, u, interest[i / width][i % width]));
             }
         } else if (u < 1 + width && u >= 1) {
            //premier ligne de sommets
             edges.add(new Edge(u, 0, 0));
         } else {
             //sommets intermédiaires

             if (u % width == 0) {
                 //arête vers bas droite
                 edges.add(new Edge((u - width) - 1, u, interest[(((u-1) - width) - 1) / width][(((u-1) - width) - 1) % width]));
             }

             //arête vers le bas
             edges.add(new Edge(u + width, u, interest[((u-1) + width) / width][((u-1) + width) % width]));

             if (u % (width + 1) == 0) {
                 //arête vers bas gauche
                 edges.add(new Edge((u - width) + 1, u, interest[((u-1) - width) + 1 / width][((u-1) - width) + 1 % width]));
             }
         }

	     return edges;
		      
	 }

    
}
