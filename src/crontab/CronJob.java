package crontab;

import apps.Zhilian;
import conf.ZhilianConf;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {
	@Scheduled(cron = "0 0 2 * * ?")//ÿ��ҹ��2��ִ�ж�ʱ��ȡ����
	public void CrawZhiLian() {
		ZhilianConf zc = new ZhilianConf();
		zc.run();
		Zhilian zl = new Zhilian();
		zl.batch(zc);
	}
}