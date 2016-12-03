package crontab;

import apps.Zhilian;
import conf.ZhilianConf;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {
	
	@Scheduled(cron = "10 19 1 * * ?")
	public void CrawZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		//把招聘页面保存到本地
//		zl.saveRecruitPageBatch(zc);
		//解析本地的招聘页面，保存到服务器的mysql
//		zl.parseRecruitPageBatch(zc);
		//过期n天前的数据
		zl.expireRecruitObject();
	}
}