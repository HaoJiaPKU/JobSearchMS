package cn.edu.pku.test;

import cn.edu.pku.util.SegmenterUtil;

public class TestSegmenterUtil {
	
	public static void testSegmentation() {
		int[] indices = {2};
		SegmenterUtil.segmentation(
				"../lda-model/test.txt",
				"../lda-model/lda.dat",
				indices);
	}
	
	public static void main(String [] args) {
		testSegmentation();
	}
}
