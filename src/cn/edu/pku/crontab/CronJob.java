package cn.edu.pku.crontab;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.pku.apps.Zhilian;
import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.service.CreateRecruitmentService;
import cn.edu.pku.util.TimeUtil;

@Component
public class CronJob {
	
//	//过期n天前的数据，当前n为90
//	@Scheduled(cron = "0 10 0 * * ?")
//	public void expireZhiLian() {
//		System.out.println("info:	数据过期	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//		ZhilianConf zc = new ZhilianConf();
//		zc.run();
//		Zhilian zl = new Zhilian();
//		zl.expireRecruitObject();
//		System.out.println("info:	数据过期	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//	}
//	
//	//爬取招聘页面保存到本地
//	@Scheduled(cron = "0 20 0 * * ?")
//	public void crawZhiLian() {
//		System.out.println("info:	数据抓取	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//		ZhilianConf zc = new ZhilianConf();
//		zc.run();
//		Zhilian zl = new Zhilian();
//		zl.crawlRecruitPageBatch(zc);
//		System.out.println("info:	数据抓取	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//	}
//	
//	//解析本地的招聘页面，保存到服务器的mysql，重复数据不保存
//	@Scheduled(cron = "0 0 2 * * ?")
//	public void parseZhiLian() {
//		System.out.println("info:	数据保存	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//		ZhilianConf zc = new ZhilianConf();
//		zc.run();
//		Zhilian zl = new Zhilian();
//		zl.parseRecruitPageBatch(zc);
//		System.out.println("info:	数据保存	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//	}
//	
//	//删除重复数据
//	@Scheduled(cron = "0 0 3 * * ?")
//	public void removeDuplicate() {
//		System.out.println("info:	删除重复数据	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//		Zhilian zl = new Zhilian();
//		zl.removeDuplicateObject();
//		System.out.println("info:	删除重复数据	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//	}
//	
//	//构建索引
//	@Scheduled(cron = "0 30 3 * * ?")
//	public void makeIndex() {
//		System.out.println("info:	构建索引	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//		CreateRecruitmentService service = new CreateRecruitmentService();
//		service.create();
//		System.out.println("info:	构建索引	"
//				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
//	}
	
}