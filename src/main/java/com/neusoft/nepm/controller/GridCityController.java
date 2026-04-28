package com.neusoft.nepm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.nepm.po.GridCity;
import com.neusoft.nepm.service.IGridCityService;

@RestController
@RequestMapping("/nepm/gridCity")
public class GridCityController {

	@Autowired
	private IGridCityService gridCityService;
	
	/****************** neps 工程 ********************/
	//根据省编号查询市信息接口
	@RequestMapping("/listGridCityByProvinceId")
	public List<GridCity> listGridCityByProvinceId(@RequestBody GridCity gridCity){
		return gridCityService.listGridCityByProvinceId(gridCity.getProvinceId());
	}
	
	/****************** neps 工程 ********************/
}
