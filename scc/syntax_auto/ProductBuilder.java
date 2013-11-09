package scc.syntax_auto;

import java.util.ArrayList;
import scc.utils.*;

public class ProductBuilder {
	public static ArrayList<Product> build(String con){
		Assert.assertTrue(con != null, "null string pointer");
		Assert.assertTrue(con.matches("\\w+:.*"), "bad product head of string:" + con);
		ArrayList<Product> prodList = new ArrayList<>();
		String[] res = con.split(":", 2);
		char[] buf = res[1].toCharArray();
		for(int i = 0; i < buf.length; ){
			int right = findSplitChar(buf, i, buf.length);
			ArrayList<ProductItem> itemList = getProductItems(buf, i, right);
			Product prod = getProduct(new ProductItem(res[0], ProdConst.ATTR_TERMINAL), itemList);
			prodList.add(prod);
		}
		return prodList;
	}
	private static int findSplitChar(char[] buf, int start, int end){
		boolean inQuote = false;
		int i = start;
		for(; i < end; i++){
			if( buf[i] == '|' && inQuote == false){ 
				return i;
			}
			if( buf[i] == '\'' ){ inQuote = ! inQuote; }
		}
		return i;
	}
	private static ArrayList<ProductItem> getProductItems(char[] buf, int start, int end ){
		ArrayList<ProductItem> items = new ArrayList<>();
		for( int i = start; i < end;){
			while( CharUtils.isWhiteSpace(buf[i]) ){ i++; }
			if(i >= end ){ break; }
			ProductItem item = new ProductItem();
			if(buf[i] == '\'' ){ 
				int pre = i+1;
				while( buf[i] != '\'' ){ i++; }
				item.name = String.copyValueOf(buf, pre, i-pre);
				item.attr = ProdConst.ATTR_NONTERMINAL;
				i++;
			}else{
				int pre = i;
				while( CharUtils.isWhiteSpace(buf[i]) == false) { i++; }
				item.name = String.valueOf(buf, pre, i-pre);
				item.attr = ProdConst.ATTR_TERMINAL;
			}
		}
		Assert.assertTrue(items.size() != 0 , "no content for product from:" + start + " to:" + end);
		return items;
	}
	private static Product getProduct(ProductItem head, ArrayList<ProductItem> items){
		ArrayList<ProductItem> list = new ArrayList<>();
		list.add(head);
		list.addAll(items);
		return new Product(list);
	}
}
