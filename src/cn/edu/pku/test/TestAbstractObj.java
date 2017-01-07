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
//				"IT服务(系统/数据/维护)",
//				"计算机硬件"
//				
//				"基金/证券/期货/投资",
//				"保险",
//				"银行",
				
//				"医药/生物工程",
				
//				"房地产/建筑/建材/工程"，
				
//				"媒体/出版/影视/文化传播",
				
//				"专业服务/咨询(财会/法律/人力资源等)"
				};
		String[] fields = {
				"id",
				"pos_title",
				"pos_category",
				"pos_description"
				};
		AbstractObj.FeildsToText("../processing/text", "	",
				sources,
				date,
				industries,
				fields);
	}
	
	public static void main(String [] args) {
		testFeildsToText();
	}
}
