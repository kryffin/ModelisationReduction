package modelisationReduction;

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

    
    public static ArrayList<Integer> botched_dfs4(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	visited[s] = true;
	System.out.println(s);
	ArrayList<Integer> tritopo = new ArrayList<>();
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
        if (!visited[e.to]) /* (b) */ {
            visited[e.to] = true;
            System.out.println(e.to);
            stack.push(e.to); /*(c) */
            end = false;
            break;
        }
        if (end) /*(d)*/ {
            tritopo.add(stack.peek()); //ajout de l'élément si il fini à la liste
            stack.pop();
        }
	}
	System.out.println(stack.capacity());

	Collections.reverse(tritopo);
	return tritopo;
    }

    public static ArrayList<Integer> dfs (Graph g) {
        //pour une quelconque raison seul le 4eme dfs est juste
        return botched_dfs4(g, 0);
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
    
    public static void main(String[] args)
    {
	testGraph();
    }
}
