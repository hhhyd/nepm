package com.neusoft.nepm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.nepm.dto.AqiDistributeTotalStatisDto;
import com.neusoft.nepm.dto.AqiTrendTotalStatisDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.dto.ProvinceItemTotalStatisDto;
import com.neusoft.nepm.dto.StatisticsPageRequestDto;
import com.neusoft.nepm.mapper.GridCityMapper;
import com.neusoft.nepm.mapper.GridProvinceMapper;
import com.neusoft.nepm.mapper.StatisticsMapper;
import com.neusoft.nepm.po.AqiFeedback;
import com.neusoft.nepm.po.Statistics;
import com.neusoft.nepm.service.IStatisticsService;

@Service
public class StatisticsService implements IStatisticsService {
	
	//获取全局数据
	@Value("${global-data.province-number}")
	private Integer provinceNumber;
	@Value("${global-data.city-number}")
	private Integer cityNumber;

	@Autowired
	private StatisticsMapper statisticsMapper;
	@Autowired
	private GridProvinceMapper gridProvinceMapper;
	@Autowired
	private GridCityMapper gridCityMapper;

	/********************* nepg工程 *********************/
	@Override
	public int saveStatistics(Statistics statistics) {
		return statisticsMapper.insert(statistics);
	}

	/********************* nepg工程 *********************/

	/********************* nepm工程 *********************/
	@Override
	public PageResponseDto<Statistics> listStatisticsPage(StatisticsPageRequestDto request) {
		PageResponseDto<Statistics> response = new PageResponseDto<>();

		// 获取总行数
		int totalRow = statisticsMapper.getStatisticsCount(request);
		response.setTotalRow(totalRow);

		// 如果总行数为0，那么直接返回
		if (totalRow == 0) {
			return response;
		}

		// 计算总页数
		int totalPageNum = 0;
		if (totalRow % request.getMaxPageNum() == 0) {
			totalPageNum = totalRow / request.getMaxPageNum();
		} else {
			totalPageNum = totalRow / request.getMaxPageNum() + 1;
		}
		response.setTotalPageNum(totalPageNum);

		// 计算上一页和下一页
		int pageNum = request.getPageNum();
		response.setPreNum(pageNum);
		response.setNextNum(pageNum);
		if (pageNum > 1) {
			response.setPreNum(pageNum - 1);
		}
		if (pageNum < totalPageNum) {
			response.setNextNum(pageNum + 1);
		}

		// 计算开始查询记录数
		request.setBeginNum((pageNum - 1) * request.getMaxPageNum());

		// 查询业务数据
		List<Statistics> list = statisticsMapper.listStatisticsPage(request);
		// 给返回值填充余下数据
		response.setPageNum(pageNum);
		response.setMaxPageNum(request.getMaxPageNum());
		response.setList(list);

		return response;
	}
	
	@Override
	public Statistics getStatisticsById(Integer id) {
		return statisticsMapper.getStatisticsById(id);
	}

	/********************* nepm工程 *********************/
	
	/********************* nepv工程 *********************/
	@Override
	public List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis() {
		return statisticsMapper.listProvinceItemTotalStatis();
	}
	
	@Override
	public List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis() {
		return statisticsMapper.listAqiDistributeTotalStatis();
	}
	
	@Override
	public List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis() {
		//获取当前12个月列表，作为查询参数
		List<AqiTrendTotalStatisDto> parameList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		for(int i=0;i<12;i++) {
			//计算当前日期的前一个月
			calendar.add(Calendar.MONTH, -1);
			AqiTrendTotalStatisDto dto = new AqiTrendTotalStatisDto();
			dto.setMonth(sdf.format(calendar.getTime()));
			parameList.add(dto);
		}
		return statisticsMapper.listAqiTrendTotalStatis(parameList);
	}
	
	@Override
	public long getAqiCount() {
		return statisticsMapper.selectCount(null);
	}
	
	@Override
	public long getAqiGoodCount() {
		QueryWrapper<Statistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.le("aqi_id", 2);
		return statisticsMapper.selectCount(queryWrapper);
	}
	
	@Override
	public String getProvinceCoverage() {
		long provinceNum = gridProvinceMapper.selectCount(null);
		return String.format("%.2f",(provinceNum/(double)this.provinceNumber)*100);
	}
	
	@Override
	public String getCityCoverage() {
		long cityNum = gridCityMapper.selectCount(null);
		return String.format("%.2f",(cityNum/(double)this.cityNumber)*100);
	}
	
	/********************* nepv工程 *********************/

}
