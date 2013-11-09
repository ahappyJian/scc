package scc.lex_auto;

import scc.utils.*;
import java.util.HashSet;

public class LexAnalyzer {
	DFANode curStatus = null;
	DFANode start = null;
	LexAnalyzer(DFANode start){
		this.start = start;
	}
	public void reset(){
		curStatus = start;
	}
	public boolean digest(String one){
		Assert.assertTrue(one != null , "Null string pointer for input content");
		DFANode node = curStatus.edges().get(one);
		if(node == null ) {
			return false;
		}else{
			curStatus = node; 
			return true;
		}
	}
	public HashSet<String> getAttr(){
		return curStatus.getAttr();
	}
}
