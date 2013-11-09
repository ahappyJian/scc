package scc.syntax_auto;

import java.util.List;
import java.util.HashSet;

public class LRItem {
	private Product prod = null;
	private int index = 0;
	private HashSet<String> searchSymbol = new HashSet<>();
	public LRItem(Product prod, HashSet<String> set) {
		this.prod = prod;
		this.searchSymbol = set;
	}
	public LRItem(Product prod){
		this.prod = prod;
	}
	public int getPos(){ return index; }
	public HashSet<String> searchSet(){ return searchSymbol; }
	public String toString(){
		if( prod == null ){ return ""; }
		StringBuilder builder = new StringBuilder();
		List<ProductItem> list = prod.getItemList();
		for( int i = 0; i < list.size(); i++){
			if( i == 0 ){ 
				builder.append(list.get(i) + ": "); 
			}else{
				builder.append( " " + list.get(i));
			}
			if( i == index ){ builder.append(" #"); }
		}
		builder.append(" , " + searchSymbol);
		return builder.toString();
	}
}
