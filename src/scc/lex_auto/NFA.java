package scc.lex_auto;

import java.util.List;

import scc.lex_auto.Edge;

public class NFA {
	public String name = "";
	public Node start = null;
	public Node end = null;
	
	static NFA mergeNFA(NFA a, NFA b){
		if( a.start == null ){ return b; }
		if( b.start == null ){ return a; }
		Edge edgeStart = new Edge(b.start);
		a.start.edges().add(edgeStart);
		Edge edgeEnd = new Edge(a.end);
		b.end.edges().add(edgeEnd);
		return a;
	}
	
	static NFA mergeAllNFAs(List<NFA> nfas){
		NFA res = null;
		for(NFA one : nfas){
			res = NFA.mergeNFA(res, one);
		}
		return res;
	}
}
