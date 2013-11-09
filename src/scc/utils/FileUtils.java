package scc.utils;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FileUtils {
	public static ArrayList<String> readLines(String fileName){
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			ArrayList<String> lines = new ArrayList<String>();
			String line = null;
			while( (line = in.readLine() ) != null ){
				lines.add(line);
			}
			in.close();
			return lines;
		} catch ( IOException e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
	}
}
