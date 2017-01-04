package cn.edu.pku.test;

import cn.edu.pku.util.SegmenterUtil;

public class TestSegmenterUtil {
	
	public static void testSegmentation() {
		int[] indices = {2};
		SegmenterUtil.segmentation(
				"../processing/text.txt",
				"../processing/tokens.dat",
				indices);
	}
	
	public static void main(String [] args) {
		testSegmentation();
	}
}
