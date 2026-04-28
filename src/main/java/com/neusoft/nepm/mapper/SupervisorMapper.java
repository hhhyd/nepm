package com.neusoft.nepm.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.Supervisor;

public interface SupervisorMapper extends BaseMapper<Supervisor>{

	/****************** neps 工程 ********************/
	//公众监督员登录 
	@Select("select * from supervisor where tel_id=#{telId} and password=#{password}")
	public Supervisor getSupervisorByIdByPass(Supervisor supervisor);
	
	/****************** neps 工程 ********************/
}
