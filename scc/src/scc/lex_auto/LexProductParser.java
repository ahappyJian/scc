package scc.lex_auto;

import scc.utils.*;
import java.util.List;
import java.util.HashMap;

import scc.lex_auto.NFABuilder;

public class LexProductParser {
	
	public static HashMap<String, NFA> parse(List<String> lines){
		HashMap<String, NFA> table = new HashMap<>();
		NFABuilder builder = new NFABuilder();
		for( String line : lines){
			String[] con = line.split(":", 1);
			Assert.assertTrue(con.length == 2 , "bad format lex product:" + line);
			String attr = con[0].trim();
			String text = con[1].trim();
			NFA nfa = builder.build(text, attr);
			table.put(attr, nfa);
		}
		return table;
	}
}
