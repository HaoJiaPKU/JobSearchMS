package crontab;

import apps.Zhilian;
import conf.ZhilianConf;

import java.io.File;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {
	
	@Scheduled(cron = "10 6 1 * * ?")
	public void CrawZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
//		zl.getRecruitPageBatch(zc);
		zl.parseRecruitPageBatch(zc);
	}
}