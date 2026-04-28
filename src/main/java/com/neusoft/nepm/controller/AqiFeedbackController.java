package com.neusoft.nepm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.nepm.dto.AfPageRequestDto;
import com.neusoft.nepm.dto.PageResponseDto;
import com.neusoft.nepm.po.AqiFeedback;
import com.neusoft.nepm.service.IAqiFeedbackService;

@RestController
@RequestMapping("/nepm/aqiFeedback")
public class AqiFeedbackController {

	@Autowired
	private IAqiFeedbackService aqiFeedbackService;
	
	/********************** neps工程 **********************/
	@RequestMapping("/saveAqiFeedback")
	public int saveAqiFeedback(@RequestBody AqiFeedback aqiFeedback) {
		return aqiFeedbackService.saveAqiFeedback(aqiFeedback);
	}
	
	@RequestMapping("/listAqiFeedbackByTelId")
	public List<AqiFeedback> listAqiFeedbackByTelId(@RequestBody AqiFeedback aqiFeedback) {
		return aqiFeedbackService.listAqiFeedbackByTelId(aqiFeedback.getTelId());
	}
	
	/********************** neps工程 **********************/
	
	/********************** nepm工程 **********************/
	@RequestMapping("/listAqiFeedbackPage")
	public PageResponseDto<AqiFeedback> listAqiFeedbackPage(@RequestBody AfPageRequestDto afPageRequestDto){
		return aqiFeedbackService.listAqiFeedbackPage(afPageRequestDto);
	}
	
	@RequestMapping("/getAqiFeedbackById")
	public AqiFeedback getAqiFeedbackById(@RequestBody AqiFeedback aqiFeedback){
		return aqiFeedbackService.getAqiFeedbackById(aqiFeedback.getAfId());
	}
	
	@RequestMapping("/updateAqiFeedbackAssign")
	public int updateAqiFeedbackAssign(@RequestBody AqiFeedback aqiFeedback){
		return aqiFeedbackService.updateAqiFeedbackAssign(aqiFeedback);
	}
	
	/********************** nepm工程 **********************/
	
	/********************** nepg工程 **********************/
	@RequestMapping("/listAqiFeedbackByGmId")
	public List<AqiFeedback> listAqiFeedbackByGmId(@RequestBody AqiFeedback aqiFeedback){
		return aqiFeedbackService.listAqiFeedbackByGmId(aqiFeedback);
	}
	
	@RequestMapping("/updateAqiFeedbackState") 
	public int updateAqiFeedbackState(@RequestBody AqiFeedback aqiFeedback){
		return aqiFeedbackService.updateAqiFeedbackState(aqiFeedback);
	}
	
	/********************** nepg工程 **********************/
}
