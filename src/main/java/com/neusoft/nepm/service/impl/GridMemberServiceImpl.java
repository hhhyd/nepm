package com.neusoft.nepm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.nepm.mapper.GridMemberMapper;
import com.neusoft.nepm.po.GridMember;
import com.neusoft.nepm.service.IGridMemberService;

@Service
public class GridMemberServiceImpl implements IGridMemberService {
	
	@Autowired
	private GridMemberMapper gridMemberMapper;
	
	/********************** nepm工程 ***********************/
	@Override
	public List<GridMember> listGridMemberByProvinceId(GridMember gridMember) {
		QueryWrapper<GridMember> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("province_id", gridMember.getProvinceId());
		queryWrapper.eq("city_id", gridMember.getCityId());
		queryWrapper.eq("state", gridMember.getState());
		return gridMemberMapper.selectList(queryWrapper);
	}
	
	/********************** nepm工程 ***********************/
	
	/********************** nepg工程 ***********************/
	@Override
	public GridMember getGridMemberByCodeByPass(GridMember gridMember) {
		return gridMemberMapper.getGridMemberByCodeByPass(gridMember.getGmCode(), gridMember.getPassword());
	}


	/********************** nepg工程 ***********************/

}
