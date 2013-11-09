package scc.lex_auto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import scc.lex_auto.LexProductParser;

public class LexAutoBuilder {
	private HashMap<String, NFA> nfaTable;
	
	public void build(String fileName){
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			LinkedList<String> con = new LinkedList<String>();
			String line;
			while( (line = in.readLine() ) != null ){
				if(line.startsWith("#") || line.matches("\\s*") ){ continue; }
				con.add(line);
			}
			in.close();
			nfaTable = LexProductParser.parse(con);
		} catch ( IOException e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
	}
	HashMap<String, NFA> getMapTable(){
		return nfaTable;
	}
	ArrayList<NFA> getList(){
		ArrayList<NFA> nfaList = new ArrayList<>();
		for( NFA one : nfaTable.values()){
			nfaList.add(one);
		}
		return nfaList;
	}
}
