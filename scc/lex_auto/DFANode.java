package scc.lex_auto;

import java.util.HashMap;
import java.util.HashSet;

public class DFANode {
	private static int count = 0;
	private final int name = count++;
	private HashSet<String> attrSet = new HashSet<>();
	private String status = "idle";
	private HashMap<String, DFANode> edgesMap = new HashMap<>();
	public DFANode() {}
	
	public void setStatus(String status){ this.status = status; }
	public String getStatus(){ return status; }
	public void setAttr(String attr){ this.attrSet.add(attr); }
	public HashSet<String> getAttr(){ return attrSet; }
	public HashMap<String, DFANode> edges(){ return edgesMap; }
	
	public String getName(){ return String.valueOf(name); }
	public String toString(){ return getName(); }
}
