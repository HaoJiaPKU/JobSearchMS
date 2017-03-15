package cn.edu.pku.cronjob;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.pku.conf.QianchengConf;
import cn.edu.pku.conf.QianchengConf;
import cn.edu.pku.crawler.Qiancheng;
import cn.edu.pku.crawler.Qiancheng;
import cn.edu.pku.service.PositionIndexService;
import cn.edu.pku.util.TimeUtil;

@Component
public class QianchengJob {
	
	public void executePipeline() {
		QianchengConf bc = new QianchengConf();
		bc.run();
		Qiancheng qc = new Qiancheng();

		/**
		//过期n天前的数据，当前n为90，可以不执行这一步，而是设定处理的数据都在过期日期之后
		System.out.println("info:	" + QianchengConf.getSource() + "开始 数据过期	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		bdwm.expirePositionObject();
		System.out.println("info:	" + QianchengConf.getSource() + "完成 数据过期	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		*/
		
		//爬取招聘页面保存到本地
		System.out.println("info:	" + QianchengConf.getSource() + "开始 数据抓取	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		qc.crawlPositionPageBatch(bc, "GB2312");
		System.out.println("info:	" + QianchengConf.getSource() + "完成 数据抓取	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//解析本地的招聘页面，保存到服务器的mysql，重复数据不保存
		System.out.println("info:	" + QianchengConf.getSource() + "开始 数据保存	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		qc.parsePositionPageBatch(bc);
		System.out.println("info:	" + QianchengConf.getSource() + "完成 数据保存	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//删除重复数据
		System.out.println("info:	" + QianchengConf.getSource() + "开始 删除重复数据	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		qc.removeDuplicate();
		System.out.println("info:	" + QianchengConf.getSource() + "完成 删除重复数据	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
	}
}