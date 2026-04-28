package com.neusoft.nepm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.nepm.po.Aqi;
import com.neusoft.nepm.service.IAqiService;

@RestController
@RequestMapping("/nepm/aqi")
public class AqiController {

	@Autowired
	private IAqiService aqiService;
	
	@RequestMapping("/listAqiAll")
	public List<Aqi> listAqiAll(){
		return aqiService.listAqiAll();
	}
}
