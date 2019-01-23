package modelisationReduction.algorythms;

import modelisationReduction.Graphs.Edge;
import modelisationReduction.Graphs.Graph;
import modelisationReduction.Graphs.GraphArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

class DFS
{
    
    public static void botched_dfs1(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	visited[s] = true;	    	
	while (!stack.empty()){
	    int u = stack.pop();
	    System.out.println(u);
	    for (Edge e: g.next(u))
		if (!visited[e.to])
		    {
			visited[e.to] = true;
			stack.push(e.to);
		    }
	}
    }

    public static void botched_dfs2(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	System.out.println(s);
	visited[s] = true;	    	
	while (!stack.empty()){
	    int u = stack.pop();
	    for (Edge e: g.next(u))
		if (!visited[e.to])
		    {
			System.out.println(e.to);			
			visited[e.to] = true;
			stack.push(e.to);
		    }
	}
    }
    
    public static void botched_dfs3(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	while (!stack.empty()){
	    int u = stack.pop();
	    if (!visited[u]){
		visited[u] = true;
		System.out.println(u);
		for (Edge e: g.next(u))
		    if (!visited[e.to])
			   stack.push(e.to);
		
	    }
	}
    }

    
    public static void botched_dfs4(Graph g, int s){
        Stack<Integer> stack = new Stack<Integer>();
        boolean visited[] = new boolean[g.vertices()];
        stack.push(s);
        visited[s] = true;
        System.out.println(s);
        while (!stack.empty()){
            boolean end = true;
            /* (a) Soit u le sommet en haut de la pile */
            /* (b) Si u a un voisin non visité, alors */
            /*     (c) on le visite et on l'ajoute sur la pile */
            /* Sinon */
            /*     (d) on enlève u de la pile */

            /* (a) */
            int u = stack.peek();
            for (Edge e: g.next(u))
                if (!visited[e.to]) /* (b) */
                {
                    visited[e.to] = true;
                    System.out.println(e.to);
                    stack.push(e.to); /*(c) */
                    end = false;
                    break;
                }
            if (end) /*(d)*/
                stack.pop();
        }
        System.out.println(stack.capacity());
    }
    
    public static void testGraph()
    {
	int n = 5;
	int i,j;
	GraphArrayList g = new GraphArrayList(6);
	g.addEdge(new Edge(0, 1, 1));
	g.addEdge(new Edge(0, 2, 1));
	g.addEdge(new Edge(0, 3, 1));
	g.addEdge(new Edge(1, 4, 1));
	g.addEdge(new Edge(4, 3, 1));
	g.addEdge(new Edge(3, 5, 1));
	g.addEdge(new Edge(5, 1, 1));

	botched_dfs1(g, 0);
        System.out.println();
	botched_dfs2(g, 0);
        System.out.println();
	botched_dfs3(g, 0);
        System.out.println();
	botched_dfs4(g, 0);
    }

    /**
     * tableau de booléens indiquant si le sommet u à été visité ou non
     */
    private static boolean[] visited;

    /**
     * liste d'entiers (sommets) dans l'ordre du tri topologique
     */
    private static ArrayList<Integer> tritopo;

    /**
     * Fonction de parcours en profondeur récursif (Depth First Search) sur un graphe à partir d'un sommet renvoyant le tri topologique de l'itération actuelle
     * @param g graphe à parcourir
     * @param s sommet de départ
     */
    public static void dfs (Graph g, int s) {
        visited[s] = true;                      //on indique que ce sommet a été visité

        for (Edge e : g.next(s)) {              //pour chaque arêtes partante de s
            if (!visited[e.to]) {                   //si le sommet ciblé par l'arête n'a pas encore été visité
                dfs(g, e.to);                           //parcours en profondeur de celui ci
            }
        }

        //plus aucun sommets à parcourir depuis s

        tritopo.add(s);                         //ajout du sommet s à la liste
    }

    /**
     * Fonction de parcours en profondeur sur un graphe donné
     * @param g graphe à parcourir
     * @return liste d'entiers
     */
    public static ArrayList<Integer> depthFirstSearch (Graph g) {
        visited = new boolean[g.vertices() + 1];        //tableau de visite (initialisé à false par défaut)
        tritopo = new ArrayList<>();                    //initialisation de la liste d'entiers (sommets) du tri topologique

        for (int u = 0; u < g.vertices(); u++) {        //pour chaque sommets du graphe
            if (!visited[u]) {
                dfs(g, u);                                      //parcours de celui ci
            }
        }

        Collections.reverse(tritopo);                   //inversion de la liste
        return tritopo;
	}
    
    public static void main(String[] args)
    {
	testGraph();
    }
}
