package com.neusoft.nepm.service;

import java.util.List;

import com.neusoft.nepm.po.GridCity;

public interface IGridCityService {

	public List<GridCity> listGridCityByProvinceId(Integer provinceId);
}
