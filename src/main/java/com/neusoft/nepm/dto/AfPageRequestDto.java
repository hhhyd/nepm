package com.neusoft.nepm.dto;

/**
 * 分页查询公众监督员反馈信息的请求数据
 */
public class AfPageRequestDto {

	//业务查询条件的请求参数
	private Integer provinceId;
	private Integer cityId;
	private Integer estimatedGrade;
	private String afDate;
	private Integer state;
	
	//分页请求参数
	private Integer pageNum;        //当前页
	private Integer maxPageNum;     //每页显示最大行数
	private Integer beginNum;       //开始查询记录数
	
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getEstimatedGrade() {
		return estimatedGrade;
	}
	public void setEstimatedGrade(Integer estimatedGrade) {
		this.estimatedGrade = estimatedGrade;
	}
	public String getAfDate() {
		return afDate;
	}
	public void setAfDate(String afDate) {
		this.afDate = afDate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getMaxPageNum() {
		return maxPageNum;
	}
	public void setMaxPageNum(Integer maxPageNum) {
		this.maxPageNum = maxPageNum;
	}
	public Integer getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(Integer beginNum) {
		this.beginNum = beginNum;
	}
}
