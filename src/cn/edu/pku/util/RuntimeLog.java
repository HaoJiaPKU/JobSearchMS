package cn.edu.pku.util;

import java.io.IOException;

public class RuntimeLog {
	public static long startTime = 0;
	public static long endTime = 0;
	public static FileOutput fo;
	
	public static void init(String path) {
		fo = new FileOutput(path);
	}
	
	public static void write(String str) {
		try {
			fo.t3.write(str);
			fo.t3.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		try {
			fo.t3.write(String.valueOf(endTime - startTime));
			fo.t3.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startTime = System.currentTimeMillis();
	}
	
	public static void close() {
		fo.closeOutput();
	}
}
