package com.neusoft.nepm.service;

import com.neusoft.nepm.po.Supervisor;

public interface ISupervisorService {

	/****************** neps 工程 ********************/
	public Supervisor getSupervisorByIdByPass(Supervisor supervisor);
	public int saveSupervisor(Supervisor supervisor);
	public Supervisor getSupervisorById(String telId);
	
	/****************** neps 工程 ********************/
}
