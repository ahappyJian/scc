package scc.lex_auto;

import java.util.HashSet;

public class Edge {
	public HashSet<String> conditions = new HashSet<>();
	public Node pointTo = null;
	Edge(){}
	Edge(Node node ){
		pointTo = node;
	}
	Edge(String condition, Node node){
		conditions.add(condition);
		pointTo = node;
	}
	Edge(HashSet<String> conditions, Node node){
		this.conditions = conditions;
		pointTo = node;
	}
}
