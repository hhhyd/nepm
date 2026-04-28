package com.neusoft.nepm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.nepm.mapper.AdminsMapper;
import com.neusoft.nepm.po.Admins;
import com.neusoft.nepm.service.IAdminsService;

@Service
public class AdminsServiceImpl implements IAdminsService {
	
	@Autowired
	private AdminsMapper adminsMapper;

	@Override
	public Admins getAdminsByCodeByPass(Admins admins) {
		return adminsMapper.getAdminsByCodeByPass(admins.getAdminCode(), admins.getPassword());
	}


}
