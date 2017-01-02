package cn.edu.pku.util;

import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.object.AbstractObj;

public class Test {

	public static void testAbstractObj() {
		String[] sources = {ZhilianConf.getSource()};
		String[] date = {"2016-12-01"};
		String[] industries = {"计算机软件", "互联网/电子商务"};
		String[] keys = {"pos_title", "pos_url"};
		AbstractObj.FeildsToText("test.txt",
				sources,
				date,
				industries,
				keys);
	}
	
	public static void main(String [] args) {
		testAbstractObj();
	}
}
