package scc.lex_auto;

import scc.utils.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import scc.lex_auto.NFA;

public class NFAtoDFA {	
	public static DFA convertNFAToDFA(NFA nfa){
		Assert.assertTrue(nfa != null , "bad NFA pointer");
		Assert.assertTrue(nfa.start != null && nfa.end != null , "empty NFA");
		
		DFA dfa = new DFA();
		HashSet<String> sets = new HashSet<>();
		LinkedList<TreeSet<Node>> unmarkedSets = new LinkedList<>();
		//init
		TreeSet<Node> start = new TreeSet<>();
		start.add(nfa.start);
		start = closure(start, null);
		unmarkedSets.add(start);
		sets.add(toString(start));
		//run
		while(unmarkedSets.size() > 0){
			TreeSet<Node> one = unmarkedSets.pop();
			HashSet<String> allConditions = getAllEdgeConditions(one);
			for(String condition : allConditions){
				TreeSet<Node> toNodes = closure(one, condition);//check all nodes
				toNodes = closure(toNodes, null);
				connectTwoNodes(dfa, one, condition, toNodes);//add edge
				String name = toString(toNodes);
				if( sets.contains(name) == false){
					sets.add(name);
					unmarkedSets.add(toNodes);
				}				
			}
		}
		dfa.start = toString(start);
		return dfa;
	}
	private static void connectTwoNodes(DFA dfa, TreeSet<Node> a, String condition, TreeSet<Node> b){
		DFANode nodeA = mustGetNode(dfa, a);
		DFANode nodeB = mustGetNode(dfa, b);
		nodeA.edges().put(condition, nodeB);
	}
	private static DFANode mustGetNode(DFA dfa, TreeSet<Node> nodeSet){
		String name = toString(nodeSet);
		DFANode node = dfa.nodes.get(name);
		if(node == null){
			node  = new DFANode();
			node.getAttr().addAll(getAttr(nodeSet));
			dfa.nodes.put(name, node);
		}
		return node;
	}
	private static HashSet<String> getAttr(TreeSet<Node> set){
		HashSet<String> attrs = new HashSet<>();
		for(Node node : set){
			if( node.getAttr().length() != 0 ){
				attrs.add(node.getAttr());
			}
		}
		return attrs;
	}
	private static String toString(TreeSet<Node> set){
		StringBuilder builder = new StringBuilder();
		for(Node node : set){
			builder.append(":");
			builder.append(node.getName());
		}
		return builder.toString();
	}
	private static TreeSet<Node> closure(TreeSet<Node> nodeSet, String condition){
		TreeSet<Node> newSet = new TreeSet<Node>();
		for(Node node : nodeSet){
			for(Edge edge : node.edges() ){
				if( condition == null && edge.conditions.size() == 0 
						|| condition != null && edge.conditions.contains(condition)){
					newSet.add(edge.pointTo);
				}				;
			}
		}
		return newSet;
	}
	private static HashSet<String> getAllEdgeConditions(TreeSet<Node> set){
		HashSet<String> conditions = new HashSet<>();
		for( Node node : set){
			for(Edge edge : node.edges() ){
				conditions.addAll(edge.conditions);
			}
		}
		return conditions;
	}
}
