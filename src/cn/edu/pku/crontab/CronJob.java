package cn.edu.pku.crontab;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.crawler.Zhilian;
import cn.edu.pku.service.CreateRecruitmentService;
import cn.edu.pku.service.PositionIndexService;
import cn.edu.pku.util.TimeUtil;

@Component
public class CronJob {
	
	PositionIndexService positionIndexservice;

	public PositionIndexService getPositionIndexservice() {
		return positionIndexservice;
	}

	@Resource
	public void setPositionIndexservice(PositionIndexService positionIndexservice) {
		this.positionIndexservice = positionIndexservice;
	}

	@Scheduled(cron = "0 29 20 * * ?")
	public void zhiLianJob() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		
		//过期n天前的数据，当前n为90
		System.out.println("info:	数据过期	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		Zhilian zl = new Zhilian();
		zl.expireRecruitObject();
		System.out.println("info:	数据过期	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//爬取招聘页面保存到本地
		System.out.println("info:	数据抓取	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.crawlRecruitPageBatch(zc);
		System.out.println("info:	数据抓取	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//解析本地的招聘页面，保存到服务器的mysql，重复数据不保存
		System.out.println("info:	数据保存	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.parseRecruitPageBatch(zc);
		System.out.println("info:	数据保存	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//删除重复数据
		System.out.println("info:	删除重复数据	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		zl.removeDuplicateObject();
		System.out.println("info:	删除重复数据	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		
		//构建索引
		System.out.println("info:	构建索引	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		positionIndexservice.create();
		System.out.println("info:	构建索引	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
	}
	
}