package scc.lex_auto;

import java.util.LinkedList;

public class Node implements Comparable<Object> {
	private static int count = 0;
	private final int name = count++;
	private String attr = "";
	private String status = "idle";
	private LinkedList<Edge> outEdges = new LinkedList<>(); 
	public Node() {}
	
	public void setStatus(String status){ this.status = status; }
	public String getStatus(){ return status; }
	public void setAttr(String attr){ this.attr = attr; }
	public String getAttr(){ return attr; }
	public LinkedList<Edge> edges(){ return outEdges; }
	
	public String getName(){ return String.valueOf(name); }
	public String toString(){ return getName(); }
	
	public int hashCode(){
		return name;
	}

	@Override
	public int compareTo(Object o) {
		Node node = (Node)o;
		int res = this.name < node.name ? -1 : (this.name > node.name) ? 1 : 0;
		return res;
	}
}
