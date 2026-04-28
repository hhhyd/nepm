package com.neusoft.nepm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.dto.AqiDistributeTotalStatisDto;
import com.neusoft.nepm.dto.AqiTrendTotalStatisDto;
import com.neusoft.nepm.dto.ProvinceItemTotalStatisDto;
import com.neusoft.nepm.dto.StatisticsPageRequestDto;
import com.neusoft.nepm.po.Statistics;

public interface StatisticsMapper extends BaseMapper<Statistics>{

	/********************** nepm工程 *********************/ 
	public int getStatisticsCount(StatisticsPageRequestDto statisticsPageRequestDto);
	public List<Statistics> listStatisticsPage(StatisticsPageRequestDto statisticsPageRequestDto);
	
	public Statistics getStatisticsById(Integer id);
	/********************** nepm工程 *********************/
	
	/********************** nepv工程 *********************/
	//省分组分项检查数据统计
	public List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis();
	//AQI控股权指数指数级别分布统计
	public List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis();
	//十二个月AQI超标趋势统计
	public List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis(List<AqiTrendTotalStatisDto> list);
	
	/********************** nepv工程 *********************/
}
