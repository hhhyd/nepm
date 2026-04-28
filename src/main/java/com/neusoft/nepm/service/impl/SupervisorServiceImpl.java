package com.neusoft.nepm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.nepm.mapper.SupervisorMapper;
import com.neusoft.nepm.po.Supervisor;
import com.neusoft.nepm.service.ISupervisorService;

@Service
public class SupervisorServiceImpl implements ISupervisorService{

	@Autowired
	private SupervisorMapper supervisorMapper;
	
	/****************** neps 工程 ********************/
	@Override
	public Supervisor getSupervisorByIdByPass(Supervisor supervisor) {
		return supervisorMapper.getSupervisorByIdByPass(supervisor);
	}
	
	@Override
	public int saveSupervisor(Supervisor supervisor) {
		return supervisorMapper.insert(supervisor);
	}
	
	@Override
	public Supervisor getSupervisorById(String telId) {
		return supervisorMapper.selectById(telId);
	}
	
	/****************** neps 工程 ********************/
}
