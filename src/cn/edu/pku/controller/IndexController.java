package cn.edu.pku.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.pku.service.PositionIndexService;
import cn.edu.pku.service.ResumeIndexService;

@Controller
@RequestMapping("index")
public class IndexController {

	PositionIndexService positionIndexService;
	ResumeIndexService resumeIndexService;
	
	public PositionIndexService getPositionIndexService() {
		return positionIndexService;
	}

	@Resource
	public void setPositionIndexService(PositionIndexService positionIndexService) {
		this.positionIndexService = positionIndexService;
	}

	public ResumeIndexService getResumeIndexService() {
		return resumeIndexService;
	}

	@Resource
	public void setResumeIndexService(ResumeIndexService resumeIndexService) {
		this.resumeIndexService = resumeIndexService;
	}

	@RequestMapping(value = "/createPositionIndex", method = RequestMethod.POST)
	public String createPositionIndex() {
		positionIndexService.create();
		return "index.jsp";
	}
	
	@RequestMapping(value = "/createResumeIndex", method = RequestMethod.POST)
	public String createResumeIndex() {
		resumeIndexService.create();
		return "index.jsp";
	}
}
