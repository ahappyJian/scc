package scc.syntax_auto;

import java.util.ArrayList;

import scc.utils.Assert;
import scc.utils.FileUtils;

public class ProductFileProcess {
	public static ArrayList<Product> getProducts(String fileName){
		ArrayList<Product> prods = new ArrayList<>();
		ArrayList<String> lines = FileUtils.readLines(fileName);
		lines = cleanLines(lines);
		lines = formatLines(lines);
		for(String line : lines){
			prods.addAll(ProductBuilder.build(line));
		}
		return prods;
	}
	private static ArrayList<String> formatLines(ArrayList<String> lines){
		ArrayList<String> res = new ArrayList<>();
		for(int i = 0; i < lines.size(); ){
			if( lines.get(i).matches("\\w+:.*") ){
				do{
					res.add(lines.get(i));
					i++;
				}while( ! lines.get(i).matches("\\w+:.*") );
			}else{
				Assert.assertMust("bad product begins at line:" + i);
			}
		}
		return res;
	}
	private static ArrayList<String> cleanLines(ArrayList<String> lines){
		ArrayList<String> res = new ArrayList<>();
		for(String one : lines){
			if( one.startsWith("#") || one.matches("\\s*") ){
				res.add("");//empty line 
			}else{ res.add(one); }
		}
		return res;
	}
}
