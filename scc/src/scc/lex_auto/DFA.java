package scc.lex_auto;

import java.util.List;
import java.util.HashMap;
import java.util.Set;

import scc.lex_auto.DFANode;

public class DFA {
	public String name = "";
	public String start = "";
	public HashMap<String, DFANode> nodes = new HashMap<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void loadDFA(List<String> lines){
		//unimplemented
	}
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("start:" + start + "\n");
		for(String name : nodes.keySet() ){
			DFANode node = nodes.get(name);
			builder.append("name:" + name + "\n");
			builder.append("status:" + node.getStatus() + "\n");
			builder.append("attrs:" + formatSet(node.getAttr()) + "\n");
			builder.append("edges:" + formatEdge(node.edges()) + "\n");
		}
		return builder.toString();
	}
	private String formatSet(Set<String> set){
		StringBuilder builder = new StringBuilder();
		for(String one : set){
			builder.append(" " + one);
		}
		return builder.toString();
	}
	private String formatEdge(HashMap<String, DFANode> edges){
		StringBuilder builder = new StringBuilder();
		for(String condition : edges.keySet()){
			builder.append(" [" + condition + "]=>" + edges.get(condition));
		}
		return builder.toString();
	}
}
