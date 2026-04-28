package com.neusoft.nepm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.nepm.mapper.GridCityMapper;
import com.neusoft.nepm.po.GridCity;
import com.neusoft.nepm.service.IGridCityService;

@Service
public class GridCityServiceImpl implements IGridCityService{

	@Autowired
	private GridCityMapper gridCityMapper;
	
	@Override
	public List<GridCity> listGridCityByProvinceId(Integer provinceId) {
		QueryWrapper<GridCity> qw = new QueryWrapper<>();
		qw.eq("province_id", provinceId);
		return gridCityMapper.selectList(qw);
	}
}
