package scc.syntax_auto;

public class ProductItem {
	public String name = "";
	public String attr = "";
	public ProductItem(){}
	public ProductItem(String name){
		this.name = name;
	}
	public ProductItem(String name, String attr){
		this.name = name;
		this.attr = attr;
	}
	public String toString(){
		if( attr.equals(ProdConst.ATTR_NONTERMINAL) ){
			return "'" + name + "'";
		}else{
			return name;
		}
	}
}
