package com.neusoft.nepm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.mapper.AqiFeedbackMapper;
import com.neusoft.nepm.po.AqiFeedback;
import com.neusoft.nepm.service.IAqiFeedbackService;

@Service
public class AqiFeedbackServiceImpl implements IAqiFeedbackService {
	
	@Autowired
	private AqiFeedbackMapper aqiFeedbackMapper;

	/********************** neps工程 **********************/
	@Override
	public int saveAqiFeedback(AqiFeedback aqiFeedback) {
		return aqiFeedbackMapper.insert(aqiFeedback);
	}
	
	@Override
	public List<AqiFeedback> listAqiFeedbackByTelId(String telId) {
		return aqiFeedbackMapper.listAqiFeedbackByTelId(telId);
	}
	/********************** neps工程 **********************/
	
	/********************** nepm工程 **********************/
	@Override
	public PageResponseDto<AqiFeedback> listAqiFeedbackPage(AfPageRequestDto request) {
		PageResponseDto<AqiFeedback> response = new PageResponseDto<>();
		
		//获取总行数
		int totalRow = aqiFeedbackMapper.getAqiFeedbackCount(request);
		response.setTotalRow(totalRow);
		
		//如果总行数为0，那么直接返回
		if(totalRow==0) {
			return response;
		}
		
		//计算总页数
		int totalPageNum = 0;
		if(totalRow%request.getMaxPageNum()==0) {
			totalPageNum = totalRow/request.getMaxPageNum();
		}else {
			totalPageNum = totalRow/request.getMaxPageNum()+1;
		}
		response.setTotalPageNum(totalPageNum);
		
		//计算上一页和下一页
		int pageNum = request.getPageNum();
		response.setPreNum(pageNum);
		response.setNextNum(pageNum);
		if(pageNum>1) {
			response.setPreNum(pageNum-1);
		}
		if(pageNum<totalPageNum) {
			response.setNextNum(pageNum+1);
		}
		
		//计算开始查询记录数
		request.setBeginNum((pageNum-1)*request.getMaxPageNum());
		
		//查询业务数据
		List<AqiFeedback> list = aqiFeedbackMapper.listAqiFeedbackPage(request);
		//给返回值填充余下数据
		response.setPageNum(pageNum);
		response.setMaxPageNum(request.getMaxPageNum());
		response.setList(list);
		
		System.out.println(totalRow);
		System.out.println(list.size());
		
		return response;
	}
	
	@Override
	public AqiFeedback getAqiFeedbackById(Integer afId) {
		return aqiFeedbackMapper.getAqiFeedbackById(afId);
	}
	
	@Override
	public int updateAqiFeedbackAssign(AqiFeedback aqiFeedback) {
		UpdateWrapper<AqiFeedback> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("gm_id", aqiFeedback.getGmId());
		updateWrapper.set("assign_date", aqiFeedback.getAssignDate());
		updateWrapper.set("assign_time", aqiFeedback.getAssignTime());
		updateWrapper.set("state", aqiFeedback.getState());
		updateWrapper.eq("af_id", aqiFeedback.getAfId());
		return aqiFeedbackMapper.update(null, updateWrapper);
	}
	
	/********************** nepm工程 **********************/
	
	/********************** nepg工程 **********************/
	@Override
	public List<AqiFeedback> listAqiFeedbackByGmId(AqiFeedback aqiFeedback) {
		return aqiFeedbackMapper.listAqiFeedbackByGmId(aqiFeedback);
	}
	
	@Override
	public int updateAqiFeedbackState(AqiFeedback aqiFeedback) {
		UpdateWrapper<AqiFeedback> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("af_id", aqiFeedback.getAfId());
		updateWrapper.set("state", aqiFeedback.getState());
		return aqiFeedbackMapper.update(null, updateWrapper);
	}
	/********************** nepg工程 **********************/

}
