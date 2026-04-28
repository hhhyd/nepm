package com.neusoft.nepm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.nepm.po.GridMember;
import com.neusoft.nepm.service.IGridMemberService;

@RestController
@RequestMapping("/nepm/gridMember")
public class GridMemberController {

	@Autowired
	private IGridMemberService gridMemberService;
	
	/************************** nepm工程 **************************/
	@RequestMapping("/listGridMemberByProvinceId")
	public List<GridMember> listGridMemberByProvinceId(@RequestBody GridMember gridMember){
		return gridMemberService.listGridMemberByProvinceId(gridMember);
	}
	
	/************************** nepm工程 **************************/
	
	/************************** nepg工程 **************************/
	@PostMapping("/getGridMemberByCodeByPass")
	public GridMember getGridMemberByCodeByPass(@RequestBody GridMember gridMember){
		return gridMemberService.getGridMemberByCodeByPass(gridMember);
	} 
	
	/************************** nepg工程 **************************/
}
