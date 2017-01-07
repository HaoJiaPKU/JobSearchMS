package cn.edu.pku.test;

import cn.edu.pku.util.SegmenterUtil;

public class TestSegmenterUtil {
	
	public static void testSegmentation() {
		int[] indices = {2};
		SegmenterUtil.segmentation(
				"../processing/text",
				"../processing/tokens",
				indices);
	}
	
	public static void main(String [] args) {
		testSegmentation();
	}
}
