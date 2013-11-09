package scc.syntax_auto;

import java.util.ArrayList;

public class Product {
	private ArrayList<ProductItem> prod = null;
	
	public Product(ArrayList<ProductItem> product){
		this.prod = product;
	}
	public ArrayList<ProductItem> getItemList() {
		return prod;
	}
	public void setItemList(ArrayList<ProductItem> product) {
		this.prod = product;
	}
	public String toString(){
		if(prod == null){ return ""; }
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < prod.size(); i++){
			if(i == 0){ 
				builder.append(prod.get(i) + ": "); 
			}else {
				builder.append(" " + prod.get(i));
			}
		}
		return builder.toString();
	}
}
