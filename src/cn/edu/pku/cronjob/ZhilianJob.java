package cn.edu.pku.cronjob;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.crawler.Zhilian;
import cn.edu.pku.service.PositionIndexService;
import cn.edu.pku.util.TimeUtil;

@Component
public class ZhilianJob {
	
	public void executePipeline() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();

		/**
		//过期n天前的数据，当前n为90，可以不执行这一步，而是设定处理的数据都在过期日期之后
		System.out.println("info:	" + ZhilianConf.getSource() + "开始 数据过期	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.expirePositionObject();
		System.out.println("info:	" + ZhilianConf.getSource() + "完成 数据过期	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		*/
		
		//爬取招聘页面保存到本地
		System.out.println("info:	" + ZhilianConf.getSource() + "开始 数据抓取	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.crawlPositionPageBatch(zc, "utf-8");
		System.out.println("info:	" + ZhilianConf.getSource() + "完成 数据抓取	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//解析本地的招聘页面，保存到服务器的mysql，重复数据不保存
		System.out.println("info:	" + ZhilianConf.getSource() + "开始 数据保存	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.parsePositionPageBatch(zc);
		System.out.println("info:	" + ZhilianConf.getSource() + "完成 数据保存	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//删除重复数据
		System.out.println("info:	" + ZhilianConf.getSource() + "开始 删除重复数据	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.removeDuplicate();
		System.out.println("info:	" + ZhilianConf.getSource() + "完成 删除重复数据	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
	}
}