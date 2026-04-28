package com.neusoft.nepm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.nepm.mapper.GridProvinceMapper;
import com.neusoft.nepm.po.GridProvince;
import com.neusoft.nepm.service.IGridProvinceService;

@Service
public class GridProvinceServiceImpl implements IGridProvinceService {
	
	@Autowired
	private GridProvinceMapper gridProvinceMapper;

	@Override
	public List<GridProvince> listGridProvinceAll() {
		return gridProvinceMapper.selectList(null);
	}

}
