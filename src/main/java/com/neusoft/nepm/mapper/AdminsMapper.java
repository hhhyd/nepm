package com.neusoft.nepm.mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.po.Admins;

public interface AdminsMapper extends BaseMapper<Admins>{

	/********************* nepm工程 *********************/
	//登录接口
	@Select("select * from admins where admin_code=#{adminCode} and password=#{password}")
	public Admins getAdminsByCodeByPass(@Param("adminCode") String adminCode, @Param("password") String password);


	/********************* nepm工程 *********************/
}
