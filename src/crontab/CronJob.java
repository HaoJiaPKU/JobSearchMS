package crontab;

import apps.Zhilian;
import conf.ZhilianConf;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {
	@Scheduled(cron = "0 17 23 * * ?")//每天夜间1点执行定时爬取任务
	public void CrawZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		zl.batch(zc);
	}
}