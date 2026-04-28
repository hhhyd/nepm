package com.neusoft.nepm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.nepm.mapper.AqiMapper;
import com.neusoft.nepm.po.Aqi;
import com.neusoft.nepm.po.GridCity;
import com.neusoft.nepm.service.IAqiService;

@Service
public class AqiServiceImpl implements IAqiService{

	@Autowired
	private AqiMapper aqiMapper;
	
	@Override
	public List<Aqi> listAqiAll() {
		QueryWrapper<Aqi> qw = new QueryWrapper<>();
		qw.orderByAsc("aqi_id");
		return aqiMapper.selectList(qw);
	}
}
