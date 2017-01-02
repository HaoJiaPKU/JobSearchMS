package cn.edu.pku.test;

import cn.edu.pku.conf.DatabaseConf;
import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.object.AbstractObj;
import cn.edu.pku.util.TimeUtil;

public class TestAbstractObj {

	public static void testFeildsToText() {
		String[] sources = {ZhilianConf.getSource()};
		String[] date = {TimeUtil.getDate(DatabaseConf.getExpiredate())};
		String[] industries = {
				"计算机软件",
				"互联网/电子商务",
				"IT服务(系统/数据/维护)",
				"计算机硬件"};
		String[] keys = {
				"pos_title",
				"pos_category",
				"pos_description"};
		AbstractObj.FeildsToText("../lda-model/test.txt",
				sources,
				date,
				industries,
				keys);
	}
	
	public static void main(String [] args) {
		testFeildsToText();
	}
}
