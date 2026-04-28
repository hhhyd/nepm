package com.neusoft.nepm.service;

import java.util.List;

import com.neusoft.nepm.dto.AqiDistributeTotalStatisDto;
import com.neusoft.nepm.dto.AqiTrendTotalStatisDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.dto.ProvinceItemTotalStatisDto;
import com.neusoft.nepm.dto.StatisticsPageRequestDto;
import com.neusoft.nepm.po.Statistics;

public interface IStatisticsService {

	/********************* nepg工程 *********************/
	public int saveStatistics(Statistics statistics);
	
	/********************* nepg工程 *********************/
	
	/********************* nepm工程 *********************/
	public PageResponseDto<Statistics> listStatisticsPage(StatisticsPageRequestDto statisticsPageRequestDto);
	public Statistics getStatisticsById(Integer id);
	
	/********************* nepm工程 *********************/
	
	/********************* nepv工程 *********************/
	public List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis();
	public List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis();
	public List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis();
	
	public long getAqiCount();
	public long getAqiGoodCount();
	public String getProvinceCoverage();
	public String getCityCoverage();
	
	/********************* nepv工程 *********************/
}
