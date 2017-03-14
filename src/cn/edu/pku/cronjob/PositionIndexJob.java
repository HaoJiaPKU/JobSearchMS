package cn.edu.pku.cronjob;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.pku.conf.ZhilianConf;
import cn.edu.pku.crawler.Zhilian;
import cn.edu.pku.service.PositionIndexService;
import cn.edu.pku.util.TimeUtil;

@Component
public class PositionIndexJob {
	
	ZhilianJob zhilianJob;
	BdwmJob bdwmJob;
	PositionIndexService positionIndexservice;

	public PositionIndexService getPositionIndexservice() {
		return positionIndexservice;
	}

	@Resource
	public void setPositionIndexservice(PositionIndexService positionIndexservice) {
		this.positionIndexservice = positionIndexservice;
	}
	
	public BdwmJob getBdwmJob() {
		return bdwmJob;
	}

	@Resource
	public void setBdwmJob(BdwmJob bdwmJob) {
		this.bdwmJob = bdwmJob;
	}

	public ZhilianJob getZhilianJob() {
		return zhilianJob;
	}
	
	@Resource
	public void setZhilianJob(ZhilianJob zhilianJob) {
		this.zhilianJob = zhilianJob;
	}

	@Scheduled(cron = "30 3 15 * * ?")
	public void executePipeline() {
//		//执行智联招聘数据的处理
//		zhilianJob.executePipeline();
		
		bdwmJob.executePipeline();
		
		//构建索引
		System.out.println("info:	开始 构建索引	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
		positionIndexservice.create();
		System.out.println("info:	完成 构建索引	"
				+ TimeUtil.getCurrentTime("yyyy/MM/dd HH:mm:ss"));
	}
}