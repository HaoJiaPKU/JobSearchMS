package cn.edu.pku.cronjob;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.crawler.Zhilian;
import cn.edu.pku.service.PositionIndexService;
import cn.edu.pku.service.ResumeIndexService;
import cn.edu.pku.util.TimeUtil;

@Component
public class ResumeIndexJob {
	
	ResumeIndexService resumeIndexService;

	public ResumeIndexService getResumeIndexService() {
		return resumeIndexService;
	}

	@Resource
	public void setResumeIndexService(ResumeIndexService resumeIndexService) {
		this.resumeIndexService = resumeIndexService;
	}

	@Scheduled(cron = "0 30 8 * * ?")
	public void executePipeline() {
		
		//构建索引
		System.out.println("info:	开始 构建简历索引	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		resumeIndexService.create();
		System.out.println("info:	完成 构建简历索引	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
	}
}