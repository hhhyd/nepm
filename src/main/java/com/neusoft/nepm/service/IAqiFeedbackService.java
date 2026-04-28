package com.neusoft.nepm.service;

import java.util.List;

import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.po.AqiFeedback;

public interface IAqiFeedbackService {

	/********************** neps工程 **********************/
	public int saveAqiFeedback(AqiFeedback aqiFeedback);
	public List<AqiFeedback> listAqiFeedbackByTelId(String telId);
	
	/********************** neps工程 **********************/
	
	/********************** nepm工程 **********************/
	public PageResponseDto<AqiFeedback> listAqiFeedbackPage(AfPageRequestDto afPageRequestDto);
	public AqiFeedback getAqiFeedbackById(Integer afId);
	public int updateAqiFeedbackAssign(AqiFeedback aqiFeedback);
	
	/********************** nepm工程 **********************/
	
	/********************** nepg工程 **********************/
	public List<AqiFeedback> listAqiFeedbackByGmId(AqiFeedback aqiFeedback);
	public int updateAqiFeedbackState(AqiFeedback aqiFeedback);
	
	/********************** nepg工程 **********************/
	
	
}
