package com.neusoft.nepm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.nepm.po.GridProvince;
import com.neusoft.nepm.service.IGridProvinceService;

@RestController
@RequestMapping("/nepm/gridProvince")
public class GridProvinceController {

	@Autowired
	private IGridProvinceService gridProvinceService;
	
	/****************** neps 工程 ********************/
	//全查询省信息接口
	@RequestMapping("/listGridProvinceAll")
	public List<GridProvince> listGridProvinceAll(){
		return gridProvinceService.listGridProvinceAll();
	}
	
	
	/****************** neps 工程 ********************/
}
