package com.neusoft.nepm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.nepm.po.Supervisor;
import com.neusoft.nepm.service.ISupervisorService;

@RestController
@RequestMapping("/nepm/supervisor")
public class SupervisorController {

	@Autowired
	private ISupervisorService supervisorService;

	/****************** neps 工程 ********************/
	// 公众监督员登录接口
	@RequestMapping("/getSupervisorByIdByPass")
	public Supervisor getSupervisorByIdByPass(@RequestBody Supervisor supervisor) {
		return supervisorService.getSupervisorByIdByPass(supervisor);
	}

	// 公众监督员注册接口
	@RequestMapping("/saveSupervisor")
	public int saveSupervisor(@RequestBody Supervisor supervisor) {
		return supervisorService.saveSupervisor(supervisor);
	}
	
	//验证公众监督员端手机号码是否被注册接口
	@RequestMapping("/getSupervisorById")
	public Supervisor getSupervisorById(@RequestBody Supervisor supervisor) {
		return supervisorService.getSupervisorById(supervisor.getTelId());
	}

	/****************** neps 工程 ********************/
}
