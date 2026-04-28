package com.neusoft.nepm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.po.AqiFeedback;

public interface AqiFeedbackMapper extends BaseMapper<AqiFeedback>{

	/*************************** neps工程 *************************/
	public List<AqiFeedback> listAqiFeedbackByTelId(String telId);
	
	/*************************** neps工程 *************************/
	
	/*************************** nepm工程 *************************/
	//分页接口
	public int getAqiFeedbackCount(AfPageRequestDto afPageRequestDto);
	public List<AqiFeedback> listAqiFeedbackPage(AfPageRequestDto afPageRequestDto);
	public AqiFeedback getAqiFeedbackById(Integer afId);
	
	/*************************** nepm工程 *************************/
	
	/*************************** nepg工程 *************************/
	public List<AqiFeedback> listAqiFeedbackByGmId(AqiFeedback aqiFeedback);
	
	/*************************** nepg工程 *************************/
}
