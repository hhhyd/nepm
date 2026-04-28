package com.neusoft.nepm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.neusoft.nepm.po.Admins;
import com.neusoft.nepm.service.IAdminsService;
@RestController
@RequestMapping("/nepm/admins")

public class AdminsController {

	@Autowired
	private IAdminsService adminsService;

	@PostMapping("/getAdminsByCodeByPass")
	public Admins getAdminsByCodeByPass(@RequestBody Admins admins) {
		return adminsService.getAdminsByCodeByPass(admins);
	}
	@GetMapping("/test")
	public String test() {
		return "Admins接口测试成功！时间：" + new java.util.Date();
	}
}
