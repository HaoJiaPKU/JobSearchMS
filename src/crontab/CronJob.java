package crontab;

import apps.Zhilian;
import conf.ZhilianConf;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {
	
	@Scheduled(cron = "0 0 1 * * ?")
	public void crawZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		//把招聘页面保存到本地
		zl.crawlRecruitPageBatch(zc);
	}
	
	@Scheduled(cron = "30 32 11 * * ?")
	public void parseZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		//解析本地的招聘页面，保存到服务器的mysql，重复数据不保存
		zl.parseRecruitPageBatch(zc);
	}
	
	@Scheduled(cron = "0 30 0 * * ?")
	public void expireZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		//过期n天前的数据，当前n为90
		zl.expireRecruitObject();
	}
}