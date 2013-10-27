package scc.lex_auto;

import scc.utils.*;
import java.util.HashSet;

import scc.lex_auto.Edge;
import scc.lex_auto.NFA;
//note: about the transfer char
//meta char: [] () | + * ? \
//transfer char: \n \r \b \f \t
// \' used in '', \" used in ""
//transfer char always need to transfer
//in [] not need to transfer: + * ? 
public class NFABuilder {
	private char[] buf;
	
	public NFA build(String con, String attr){
		if( con == null ) return null;
		buf = con.toCharArray();
		NFA nfa = buildNFA(buf, 0, buf.length);
		nfa.end.setStatus("end");
		nfa.end.setAttr(attr);
		return nfa;
	}
	//return the first | position or end position
	int findSplitChar(char[] buf, int start, int end){
		int brace = 0;
		int i = start;
		while(i < end ){
			if(buf[i] == '|' && brace == 0 ){ 
				break;
			}else if(buf[i] == '(' ){ 
				brace ++; 
			}else if(buf[i] == ')' ){ 
				brace --; 
			}else if(buf[i] == '\\' && i+1 < end && buf[i+1] == '(' ){ 
				i++; 
			}else if (buf[i] == '\\' && i+1 < end && buf[i+1] == ')'){
				i++;
			}else if(buf[i] == '\\' && i+1 < end && buf[i+1] == '|' ){
				i++;
			}
			i++;
		}
		return i;
	}
	private NFA buildNFA(char[] buf, int start, int end){
		int i = start;
		int last = start;
		NFA nfa = null;
		while( i < end){
			i = findSplitChar(buf, i, end);
			NFA newNfa = buildOneNFA(buf, last, i);
			nfa = NFA.mergeNFA(nfa, newNfa);
			i++;
			last = i;
		}
		Assert.assertTrue(nfa != null , "no NFA for string from:" + start + " to:" + end);
		return nfa;
	}
	private NFA buildOneNFA(char[] buf, int start, int end){
		int i = start;
		NFA one = new NFA();
		one.start = new Node();
		Node pre = one.start;
		while(i < end ){//add an edge and a node 
			while( i < end && isWhiteSpace(buf[i]) ){ i++; }
			if( i >= end ){ break; }
			int right = 0;
			if(buf[i] == '[' ){//not allow null
				right = findRightBracket(buf, i+1, end);
				HashSet<String> conditions = getBracketCond(buf, i, right);
				pre = addEdge(pre, conditions, getTimesChar(buf, ++right, end));
			}else if( buf[i] == '(' ){//not allow null
				right = findRightParentheses(buf, i+1, end);
				NFA nfa = buildNFA(buf, i+1, right);
				pre = addNFA(pre, nfa, getTimesChar(buf, ++right, end));
			}else{
				right = findTransferChar(buf, i, end);
				HashSet<String> conditions = getCondition(buf, i, right);
				pre = addEdge(pre, conditions, getTimesChar(buf, right, end));
			}
			i = adjustTimesChar(buf, right, end);
		}
		one.end = pre;
		return one.start == one.end ? null : one; 
	}
	private HashSet<String> getCondition(char[] buf, int start, int end ){
		int i = start;
		if( i >= end ){ Assert.assertMust("no char left at:" + start); }
		HashSet<String> cond = new HashSet<>();
		if( buf[i] != '\\'){
			cond.add(String.valueOf(buf[i]));
		}else if( buf[i] == '\\' && i+1 < end && transferChar(buf[i+1]) != 0xff){
			cond.add( String.valueOf( transferChar(buf[i+1]) ));
		}else{ Assert.assertMust("unknow transfer-char at:" + start); }
		return cond;
	}
	private int adjustTimesChar(char[] buf, int i, int end){
		if( i < end ){
			if( buf[i] == '*' || buf[i] == '?' || buf[i] == '+' ){
				return i+1;
			}
		}
		return i;
	}
	private char getTimesChar(char[] buf, int i, int end){
		if( i < end ){
			if( buf[i] == '*' || buf[i] == '?' || buf[i] == '+' ){
				return buf[i];
			}
		}
		return 0x00;	
	}
	private Node addNFA(Node preNode, NFA nfa, char mode){
		if( nfa == null ){ return preNode; }
		preNode.edges().add(new Edge(nfa.start));
		Node node = nfa.end;
		if( mode == '?'){
			preNode.edges().add(new Edge(node));
		}else if( mode == '+' ){
			node.edges().add(new Edge(preNode));
		}else if( mode == '*' ){
			preNode.edges().add(new Edge(node));
			node.edges().add(new Edge(preNode));
		}
		return node;
	}
	private Node addEdge(Node preNode, HashSet<String> conditions, char mode){
		Node node = new Node();
		Edge edge = new Edge(conditions, node);
		preNode.edges().add(edge);
		//check for + ? *
		if( mode == '?'){
			preNode.edges().add(new Edge(node));
		}else if( mode == '+' ){
			node.edges().add(new Edge(preNode));
		}else if( mode == '*' ){
			preNode.edges().add(new Edge(node));
			node.edges().add(new Edge(preNode));
		}
		return node;
	}
	private HashSet<String> getBracketCond(char[] buf, int start, int end ){
		int i = start + 1;
		Assert.assertTrue(i < end, "no char in []");
		if(buf[i] == '^' ){ 
			HashSet<String> complSet = getConditions(buf, i+1, end);
			HashSet<String> resSet = getUniversalSet();
			resSet.remove(complSet);
			return resSet;
		}else{
			return getConditions(buf, i, end);
		}
	}
	private HashSet<String> getConditions(char[] buf, int start, int end){
		HashSet<String> set = new HashSet<>();
		int i = start;
		while( i < end){
			if( buf[i] == '\\' && i+1 < end ){
				set.add(String.valueOf(bracketTransferChar(buf, i)) );
				i++;
			}else if( buf[i] == '\\' ){
				Assert.assertMust("expected a char behind \\ at:" + i);
			}else{
				set.add(String.valueOf(buf[i]));
			}
			i++;
		}
		return set;
	}
	private char transferChar(char ch){
		char res = 0xff;
		if(ch == 'n' ){ res = '\n'; }
		else if( ch == 'r'){ res = '\r'; }
		else if( ch == 't'){ res = '\t'; }
		else if( ch == 'f'){ res = '\f'; }
		else if( ch == 'b'){ res = '\b'; }
		else if( ch == '\\'){ res = '\\'; }
		else if( ch == '('){ res = '('; }
		else if( ch == ')'){ res = ')'; }
		else if( ch == '['){ res = '['; }
		else if( ch == ']'){ res = ']'; }
		else if( ch == '+'){ res = '+'; }
		else if( ch == '*'){ res = '*'; }
		else if( ch == '?'){ res = '?'; }
		else if( ch == '|'){ res = '|'; }
		return res;
	}
	private char bracketTransferChar(char[] buf, int i ){
		Assert.assertTrue(buf[i] == '\\', "bad transfer char at:" + i);
		i++;
		char ch = 0;
		if(buf[i] == 'n' ){ ch = '\n'; }
		else if( buf[i] == 'r'){ ch = '\r'; }
		else if( buf[i] == 't'){ ch = '\t'; }
		else if( buf[i] == 'f'){ ch = '\f'; }
		else if( buf[i] == 'b'){ ch = '\b'; }
		else if( buf[i] == '\\'){ ch = '\\'; }
		else if( buf[i] == '('){ ch = '('; }
		else if( buf[i] == ')'){ ch = ')'; }
		else if( buf[i] == '['){ ch = '['; }
		else if( buf[i] == ']'){ ch = ']'; }
		else if( buf[i] == '|'){ ch = '|'; }
		else{ Assert.assertMust("unknown transfer-char in [] at:" + i);}
		return ch;
	}
	private HashSet<String> getUniversalSet(){
		HashSet<String> set = new HashSet<>();
		for(char ch = 9; ch <=13 ; ch++ ){
			set.add(String.valueOf(ch));
		}
		for(char ch = 32; ch < 127; ch++ ){
			set.add(String.valueOf(ch));
		}
		return set;
	}
	private int findRightParentheses(char[] buf, int start, int end){
		int i = start;
		while( i< end){
			if(buf[i] == ')'){
				break;
			}else if( buf[i] == '\\' && i+1 < end && buf[i+1] == ')'){
				i+=2;
			}else{ i++; }
		}
		Assert.assertTrue(i < end, "can't find the ) from pos:" + start);
		return i;
	}
	private int findRightBracket(char[] buf, int start, int end ){
		int i = start;
		while( i < end ){
			if( buf[i] == ']' ){
				break;
			}else if( buf[i] == '\\' && i+1 < end && buf[i+1] == ']' ){
				i+=2;
			}else{ i++; }
		}
		Assert.assertTrue(i < end, "can't find the ] from pos:" + start);
		return i;
	}
	private int findTransferChar(char[] buf, int start, int end){
		int i = start;
		if( i < end ){
			if( buf[i] == '\\' && i+1 < end ){
				return i+2;
			}
		}else{ Assert.assertMust("no char needs to transfer at:" + start); }
		return i+1;
	}
	private boolean isWhiteSpace(char ch){
		return ch == ' ' || ch == '\t';
	}
}
