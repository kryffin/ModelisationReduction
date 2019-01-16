package modelisationReduction.Graphs;

import java.io.*;

public abstract class Graph
{
   public abstract int vertices();
   

   public abstract Iterable<Edge> next(int v);
   public abstract Iterable<Edge> prev(int v);

    public void writeFile(String s)
    {
	try
	    {			 
		PrintWriter writer = new PrintWriter(s, "UTF-8");
		writer.println("digraph G{");
		int u;
		int n = vertices();
		for (u = 0; u < n;  u++)
		    for (Edge e: next(u))
			writer.println(e.from + "->" + e.to + "[label=\"" + e.cost + "\"];");
		writer.println("}");
		writer.close();
	    }
	catch (IOException e)
	    {
	    }						
    }
  
}
