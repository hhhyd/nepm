package com.neusoft.nepm.service;

import java.util.List;

import com.neusoft.nepm.po.GridMember;

public interface IGridMemberService {

	/******************** nepm工程 *********************/
	public List<GridMember> listGridMemberByProvinceId(GridMember gridMember);
	
	/******************** nepm工程 *********************/
	
	/******************** nepg工程 *********************/
	public GridMember getGridMemberByCodeByPass(GridMember gridMember);
	
	/******************** nepg工程 *********************/
}
