package scc.utils;

public class Assert {
	public static void assertEquals(Object actual, Object expected, String msg){
		if( actual.equals(expected)){ return ;}
		errorOut("expected:" + expected + " got:" + actual + " " + msg);
		assert false;
	}
	public static void assertTrue(boolean res, String msg){
		if( res == true ){ return; }
		errorOut(msg);
		assert false;
	}
	public static void assertMust(String msg){
		errorOut(msg);
		assert false;
	}
	public static void errorOut(String text){
		System.err.println("[ERROR]" + text);
	}
	public static void infoOut(String text){
		System.out.println("[INFO ]" + text);
	}
}
